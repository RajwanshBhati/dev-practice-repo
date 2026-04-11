package com.example.springbootapp.service;

import com.example.springbootapp.component.NotificationComponent;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationComponent notificationComponent;

    public NotificationService(NotificationComponent notificationComponent) {
        this.notificationComponent = notificationComponent;
    }

    public String notifyUser(String name, String event) {

        return notificationComponent.send(name, event);
    }
}