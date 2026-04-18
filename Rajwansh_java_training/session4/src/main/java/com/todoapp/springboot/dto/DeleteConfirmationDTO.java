package com.todoapp.springboot.dto;

//I am creating this DTO to send a confirmation message when a user tries to delete a todo item.
public class DeleteConfirmationDTO {

    private Long todoId;
    private String todoTitle;
    private String message;
    private String confirmUrl;

    public DeleteConfirmationDTO(Long todoId, String todoTitle, String confirmUrl) {
        this.todoId    = todoId;
        this.todoTitle = todoTitle;
        this.confirmUrl = confirmUrl;
        this.message   = "Are you sure you want to delete the todo '"
                         + todoTitle
                         + "'? Send DELETE to the confirmUrl to proceed.";
    }

    public Long getTodoId()       { return todoId; }
    public String getTodoTitle()  { return todoTitle; }
    public String getMessage()    { return message; }
    public String getConfirmUrl() { return confirmUrl; }
}
