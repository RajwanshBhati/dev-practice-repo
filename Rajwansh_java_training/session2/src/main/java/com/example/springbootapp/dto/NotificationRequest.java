package com.example.springbootapp.dto;

import jakarta.validation.constraints.NotBlank;

/* I am creating a DTO (Data Transfer Object) class is NotificationRequest that will be used to capture the data needed to send a notification
. This class has three fields: recipient, eventType, and additionalInfo.
*/
public class NotificationRequest {

    @NotBlank(message = "Recipient must not be blank")
    private String recipient;

    @NotBlank(message = "Event type must not be blank")
    private String eventType;

    private String additionalInfo;

    public NotificationRequest() {}

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
}
