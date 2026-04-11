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

    //Endpoint to trigger a notification. Validates the request body and sends a notification using the component.
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