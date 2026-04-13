package com.example.springbootapp.controller;

import com.example.springbootapp.component.NotificationComponent;
import com.example.springbootapp.dto.NotificationRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationComponent notificationComponent;

    public NotificationController(NotificationComponent notificationComponent) {
        this.notificationComponent = notificationComponent;
    }

    // I implemented the sendNotification method to handle POST requests to the "/notification/send" endpoint. This method takes a NotificationRequest object as input, which is validated using the @Valid annotation
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(
            @Valid @RequestBody NotificationRequest request) {

        String response = notificationComponent.send(
                request.getRecipient(),
                request.getEventType()
        );

        return ResponseEntity.ok(response);
    }
}