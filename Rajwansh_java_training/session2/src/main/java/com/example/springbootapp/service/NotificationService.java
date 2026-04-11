package com.example.springbootapp.service;

import com.example.springbootapp.component.NotificationComponent;
import com.example.springbootapp.dto.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationComponent notificationComponent;

    public NotificationService(NotificationComponent notificationComponent) {
        this.notificationComponent = notificationComponent;
    }

    public String triggerNotification(NotificationRequest request) {
        String message = notificationComponent.generateMessage(
                request.getRecipient(),
                request.getEventType(),
                request.getAdditionalInfo()
        );

        return notificationComponent.dispatch(message);
    }
}
