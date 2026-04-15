package com.springrest.restapi.dto;

// Here I am creating a SubmitRequest class to represent the structure of the data that will be submitted through the POST endpoint in the UserController. This class contains fields for title, description, category, and submittedBy, along with their respective getters and setters. This allows me to easily map incoming JSON data to this object when handling POST requests.
public class SubmitRequest {

    private String title;
    private String description;
    private String category;
    private String submittedBy;

    public SubmitRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(String submittedBy) { this.submittedBy = submittedBy; }
}