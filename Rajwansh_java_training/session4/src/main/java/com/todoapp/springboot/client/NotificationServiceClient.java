package com.todoapp.springboot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationServiceClient {
    
    // Here I create a NotificationServiceClient class that simulates sending notifications to an external notification service. 
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceClient.class);
    

    // The class contains methods to send notifications for different events (e.g., when a todo item is created, updated, or deleted). Each method logs the notification details and simulates a network call to the external service.
    public void sendTodoCreatedNotification(Long todoId, String todoTitle) {
        logger.info("[NotificationServiceClient] Sending CREATE notification — todoId={}, title='{}'",
                todoId, todoTitle);

        simulateNetworkCall();

        logger.info("[NotificationServiceClient] Notification sent successfully for new TODO: '{}'", todoTitle);
    }
    
    // I also implement methods to send notifications when a todo item is updated or deleted, following a similar pattern of logging the notification details and simulating a network call.
    public void sendTodoUpdatedNotification(Long todoId, String todoTitle, String newStatus) {
        logger.info("[NotificationServiceClient] Sending UPDATE notification — todoId={}, title='{}', newStatus={}",
                todoId, todoTitle, newStatus);

        simulateNetworkCall();

        logger.info("[NotificationServiceClient] Update notification sent successfully for TODO: '{}'", todoTitle);
    }
    

    //I implement the sendTodoDeletedNotification method, which logs the details of the deleted todo item and simulates a network call to send a delete notification to the external service.
    public void sendTodoDeletedNotification(Long todoId, String todoTitle) {
        logger.info("[NotificationServiceClient] Sending DELETE notification — todoId={}, title='{}'",
                todoId, todoTitle);

        simulateNetworkCall();

        logger.info("[NotificationServiceClient] Delete notification sent successfully for TODO: '{}'", todoTitle);
    }

    // The simulateNetworkCall method is a helper method that simulates a delay to mimic the time taken for a network call to an external service. 
    private void simulateNetworkCall() {
        try {
            Thread.sleep(50); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("[NotificationServiceClient] Network simulation interrupted");
        }
    }
}
