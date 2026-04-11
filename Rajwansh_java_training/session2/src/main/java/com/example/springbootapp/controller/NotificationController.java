package com.example.springbootapp.controller;

import com.example.springbootapp.component.NotificationComponent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationComponent notificationComponent;

    public NotificationController(NotificationComponent notificationComponent) {
        this.notificationComponent = notificationComponent;
    }

   // Endpoint to send a notification. Example: /notification?name=John&event=login
    @GetMapping
    public ResponseEntity<String> sendNotification(
            @RequestParam String name,
            @RequestParam String event) {

        String response = notificationComponent.send(name, event);

        return ResponseEntity.ok(response);
    }
}