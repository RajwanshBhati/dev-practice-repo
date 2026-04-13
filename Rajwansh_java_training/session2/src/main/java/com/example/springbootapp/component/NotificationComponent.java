package com.example.springbootapp.component;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// Here I am creating a NotificationComponent class that is annotated with @Component, making it a Spring-managed bean. The send method takes a recipient and an event type as parameters, constructs a notification message with the current timestamp, and prints it to the console. and it returns a confirmation string indicating that the notification has been sent.
@Component
public class NotificationComponent {

    public String send(String recipient, String eventType) {
        
         String message = String.format(
        "[%s] Notification -> %s | Event: %s",
        LocalDateTime.now(),
        recipient,
        eventType.toUpperCase()
);
        System.out.println("NOTIFICATION: " + message);

        return "Notification sent";
    }
}