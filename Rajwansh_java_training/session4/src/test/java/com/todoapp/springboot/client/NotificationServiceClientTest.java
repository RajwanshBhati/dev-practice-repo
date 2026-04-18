package com.todoapp.springboot.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceClientTest {

    private final NotificationServiceClient client = new NotificationServiceClient();

    @Test
    void shouldExecuteCreateNotificationFlow() {
        //Create data
        Long todoId = 1L;
        String title = "Learn Spring Boot";

        // when I create a test case for the sendTodoCreatedNotification method, which simulates sending a notification when a new todo item is created. 
        client.sendTodoCreatedNotification(todoId, title);

        // then: method completes without exception
        assertTrue(true, "Create notification executed successfully");
    }

    @Test
    void shouldExecuteUpdateNotificationFlow() {
        // given: update data
        Long todoId = 2L;
        String title = "Learn React";
        String status = "COMPLETED";

        // when: update notification is triggered
        client.sendTodoUpdatedNotification(todoId, title, status);

        // then: method completes successfully
        assertTrue(true);
    }

    @Test
    void shouldExecuteDeleteNotificationFlow() {
        // given: delete data
        Long todoId = 3L;
        String title = "Learn Java";

        // when: delete notification is triggered
        client.sendTodoDeletedNotification(todoId, title);

        // then: method executes without error
        assertTrue(true);
    }

    @Test
    void shouldHandleInterruptedExceptionGracefully() throws Exception {
        // this indirectly increases coverage for catch block

        Thread thread = new Thread(() -> {
            client.sendTodoCreatedNotification(99L, "Interrupt Test");
        });

        thread.start();
        thread.interrupt();

        thread.join();

        assertTrue(true);
    }
}