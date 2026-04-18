package com.todoapp.springboot.service;
import com.todoapp.springboot.dto.TodoRequestDTO;
import com.todoapp.springboot.dto.TodoResponseDTO;
import com.todoapp.springboot.enums.TodoStatus;
import com.todoapp.springboot.entity.Todo;
import com.todoapp.springboot.repository.TodoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.todoapp.springboot.exception.TodoNotFoundException;
import com.todoapp.springboot.exception.InvalidStatusTransitionException;
import com.todoapp.springboot.dto.DeleteConfirmationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import com.todoapp.springboot.client.NotificationServiceClient;




@Service
public class TodoServiceImpl implements TodoService {

    private static final Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);

    public final TodoRepository todoRepository;
    private final NotificationServiceClient notificationServiceClient;

    public TodoServiceImpl(TodoRepository todoRepository, NotificationServiceClient notificationServiceClient) {
        this.todoRepository = todoRepository;
        this.notificationServiceClient = notificationServiceClient;
    }

    @Override
    public TodoResponseDTO createTodo(TodoRequestDTO requestDTO) {
        // Here I implement the createTodo method, which takes a TodoRequestDTO as input, creates a new Todo entity, saves it to the database using the TodoRepository, and returns a TodoResponseDTO with the created todo item details.
       
       logger.info("Creating new TODO with title: {}", requestDTO.getTitle());
     
    Todo todo = new Todo(
    requestDTO.getTitle(),
    requestDTO.getDescription(),
    requestDTO.getStatus() != null ? requestDTO.getStatus() : TodoStatus.PENDING,
    LocalDateTime.now()
    ) ;

    Todo savedTodo = todoRepository.save(todo);
    logger.info("TODO created successfully with ID: {}", savedTodo.getId());

    // After successfully creating the todo item, I call the sendTodoCreatedNotification method of the NotificationServiceClient to send a notification about the new todo item. I pass the ID and title of the created todo item to the notification method.
    notificationServiceClient.sendTodoCreatedNotification(savedTodo.getId(), savedTodo.getTitle());

    return convertToResponseDTO(savedTodo);
    }


   @Override
   public List<TodoResponseDTO> getAllTodos() {
        // Here I implement the getAllTodos method, which retrieves all todo items from the database using the TodoRepository, converts each Todo entity to a TodoResponseDTO, and returns a list of TodoResponseDTOs.
        logger.info("Fetching all TODOs");
        
        List<Todo> todos = todoRepository.findAll();

        logger.info("Total TODOs fetched: {}", todos.size());
        return todos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TodoResponseDTO getTodoById(Long id) {
        // Here I implement the getTodoById method, which retrieves a todo item by its ID. It uses the TodoRepository to find the todo item in the database, and if found, converts it to a TodoResponseDTO and returns it. If the todo item is not found, it throws a RuntimeException with an message.
        logger.info("Fetching TODO with ID: {}", id);
        
        Todo todo = findTodoOrThrow(id);
        return convertToResponseDTO(todo);
    }

  @Override
    public TodoResponseDTO updateTodo(Long id, TodoRequestDTO requestDTO) {
        logger.info("Updating TODO with ID: {}", id);
        Todo existingTodo = findTodoOrThrow(id);

        // Validate status transition if a new status is provided.
        if (requestDTO.getStatus() != null) {
            logger.info("Validating status transition from {} to {}",
                existingTodo.getStatus(), requestDTO.getStatus());
            validateStatusTransition(existingTodo.getStatus(), requestDTO.getStatus());
            existingTodo.setStatus(requestDTO.getStatus());
        }

        // Update text fields when provided by the client.
        if (requestDTO.getTitle() != null) {
            existingTodo.setTitle(requestDTO.getTitle());
        }

        if (requestDTO.getDescription() != null) {
            existingTodo.setDescription(requestDTO.getDescription());
        }

        Todo updatedTodo = todoRepository.save(existingTodo);

        logger.info("TODO updated successfully with ID: {}", id);
        
        // After successfully updating the todo item, I call the sendTodoUpdatedNotification method of the NotificationServiceClient to send a notification about the updated todo item. I pass the ID, title, and new status of the updated todo item to the notification method.
        notificationServiceClient.sendTodoUpdatedNotification(
                updatedTodo.getId(),
                updatedTodo.getTitle(),
                updatedTodo.getStatus().name()
        );
        return convertToResponseDTO(updatedTodo);
    }


    @Override
    public DeleteConfirmationDTO getDeleteConfirmation(Long id, String baseUrl) {
        logger.info("Generating delete confirmation for TODO with ID: {}", id);
        Todo todo = findTodoOrThrow(id);
        String confirmUrl = baseUrl + "/todos/" + id + "?confirmed=true";
        return new DeleteConfirmationDTO(todo.getId(), todo.getTitle(), confirmUrl);
    }
    
     @Override
    public void deleteTodoById(Long id) {
        // Verify existence first so we can throw a meaningful 404. If we just call deleteById and the ID doesn't exist, it will silently do nothing, which is not ideal for my API.
        logger.info("Deleting TODO with ID: {}", id);
        
        findTodoOrThrow(id);
        todoRepository.deleteById(id);

        logger.info("TODO deleted successfully with ID: {}", id);
        
        // After successfully deleting the todo item, I call the sendTodoDeletedNotification method of the NotificationServiceClient to send a notification about the deleted todo item. I pass the ID and title of the deleted todo item to the notification method.
        notificationServiceClient.sendTodoDeletedNotification(id, todo.getTitle());
    }

    private Todo findTodoOrThrow(Long id) {
        
        return todoRepository.findById(id)
                .orElseThrow(() ->{
                logger.error("TODO not found with ID: {}", id);
                return new TodoNotFoundException(id);
            });
    }
   

     private void validateStatusTransition(TodoStatus currentStatus, TodoStatus requestedStatus) {
        boolean isValidTransition =
                (currentStatus == TodoStatus.PENDING && requestedStatus == TodoStatus.COMPLETED)
                || (currentStatus == TodoStatus.COMPLETED && requestedStatus == TodoStatus.PENDING);

        if (!isValidTransition) {
            logger.error("Invalid status transition from {} to {}", currentStatus, requestedStatus);
            throw new InvalidStatusTransitionException(currentStatus, requestedStatus);
        }
    }


    public TodoResponseDTO convertToResponseDTO(Todo todo) {
        return new TodoResponseDTO(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getStatus(),
                todo.getCreatedAt()
        );
    }
}