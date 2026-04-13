package com.example.springbootapp.service;

import com.example.springbootapp.component.NotificationComponent;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationComponent notificationComponent;
    

    // I am injecting the NotificationComponent into the NotificationService using constructor injection. 
    public NotificationService(NotificationComponent notificationComponent) {
        this.notificationComponent = notificationComponent;
    }
     
    // I implemented the notifyUser method to send a notification to a user based on their name and the event that occurred. This method calls the send method of the NotificationComponent, passing the user's name and event type, and returns the result of the notification 
    public String notifyUser(String name, String event) {

        return notificationComponent.send(name, event);
    }
}