package com.example.springbootapp.component;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationComponent {

    public String send(String recipient, String eventType) {

        String message =
                "[" + LocalDateTime.now() + "] " +
                "Notification -> " + recipient +
                " | Event: " + eventType.toUpperCase();

        System.out.println("NOTIFICATION: " + message);

        return "Notification sent";
    }
}