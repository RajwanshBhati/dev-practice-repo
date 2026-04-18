package com.todoapp.springboot.service;
import com.todoapp.springboot.dto.TodoRequestDTO;
import com.todoapp.springboot.dto.TodoResponseDTO;
import com.todoapp.springboot.enums.TodoStatus;
import com.todoapp.springboot.entity.Todo;
import com.todoapp.springboot.repository.TodoRepository;
import org.springframework.stereotype.Service;


@Service
public class TodoServiceImpl implements TodoService {

    public final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public TodoResponseDTO createTodo(TodoRequestDTO requestDTO) {
        // Here I implement the createTodo method, which takes a TodoRequestDTO as input, creates a new Todo entity, saves it to the database using the TodoRepository, and returns a TodoResponseDTO with the created todo item details.
     Todo todo = new Todo(
    requestDTO.getTitle(),
    requestDTO.getDescription(),
    requestDTO.getStatus() != null ? requestDTO.getStatus() : TodoStatus.PENDING
    LocalDateTime.now()
    ) ;

    Todo savedTodo = todoRepository.save(todo);
    return ConvertToResponseDTO(savedTodo);
    }


   @override
   public List<TodoResponseDTO> getAllTodos() {
        // Here I implement the getAllTodos method, which retrieves all todo items from the database using the TodoRepository, converts each Todo entity to a TodoResponseDTO, and returns a list of TodoResponseDTOs.
        List<Todo> todos = todoRepository.findAll();
        return todos.stream()
                .map(this::ConvertToResponseDTO)
                .collect(Collectors.toList());
    }

    @override
    public TodoResponseDTO getTodoById(Long id) {
        // Here I implement the getTodoById method, which retrieves a todo item by its ID. It uses the TodoRepository to find the todo item in the database, and if found, converts it to a TodoResponseDTO and returns it. If the todo item is not found, it throws a RuntimeException with an appropriate message.
        Todo todo = findTodoOrThrow(id);
        return ConvertToResponseDTO(todo);
    }

  @Override
    public TodoResponseDTO updateTodo(Long id, TodoRequestDTO requestDTO) {
        Todo existingTodo = findTodoOrThrow(id);

        // Validate status transition if a new status is provided.
        if (requestDTO.getStatus() != null) {
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
        return convertToResponseDTO(updatedTodo);
    }

     @Override
    public void deleteTodo(Long id) {
        // Verify existence first so we can throw a meaningful 404.
        findTodoOrThrow(id);
        todoRepository.deleteById(id);
    }

    private Todo findTodoOrThrow(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }
   

     private void validateStatusTransition(TodoStatus currentStatus, TodoStatus requestedStatus) {
        boolean isValidTransition =
                (currentStatus == TodoStatus.PENDING && requestedStatus == TodoStatus.COMPLETED)
                || (currentStatus == TodoStatus.COMPLETED && requestedStatus == TodoStatus.PENDING);

        if (!isValidTransition) {
            throw new InvalidStatusTransitionException(currentStatus, requestedStatus);
        }
    }


    public ConvertToResponseDTO(Todo todo) {
        return new TodoResponseDTO(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getStatus(),
                todo.getCreatedAt()
        );
    }
}