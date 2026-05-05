# Interview Process Tracking Web App

A full-stack web application designed to manage the complete recruitment lifecycle from job creation to final selection. The system provides a centralized platform for HR, Candidates, and Panel Members to interact efficiently and securely.

---

## Project Overview

The Interview Tracking System simplifies the hiring process by replacing manual tracking with an automated workflow. It allows HR to manage job descriptions, track candidate progress across multiple interview stages, and collect structured feedback.

Candidates can apply for jobs and monitor their application status, while panel members can review assigned interviews and submit feedback.

---

## Key Features

### HR Module
- Create, update, and manage Job Descriptions (JDs)
- Track candidate progress across stages
- Schedule interviews and assign panel members
- Review feedback and take final hiring decisions

### Candidate Module
- View available job opportunities
- Apply for jobs with profile and resume
- Track application status (Screening → L1 → L2 → HR)
- View interview schedules

### Panel Module
- Secure onboarding using activation link
- View assigned interviews
- Submit structured feedback (comments, rating, decision)

---

## Tech Stack

### Frontend
- HTML5
- CSS3
- JavaScript (Vanilla)

### Backend
- Java 17
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA

### Database
- PostgreSQL

### Tools & Testing
- Postman
- Maven
- JUnit & Mockito
- JaCoCo (Code Coverage)

---

## System Architecture

    Frontend (HTML/CSS/JS)
            ↓
    REST API Calls
            ↓
    Spring Boot Backend (Controller → Service → Repository)
            ↓
    PostgreSQL Database

---

## Security

- JWT-based authentication
- Role-based access control (HR / Panel / Candidate)
- Password flow:
  - Frontend → Base64 encoding
  - Backend → Decode + BCrypt hashing
- Secure token-based activation for new users

---

## API Overview

Base URL:
http://localhost:8080/api

### Authentication
- POST /api/auth/login
- POST /api/auth/refresh
- POST /api/auth/logout
- POST /api/auth/activate

### Job Description
- POST /api/hr/jd
- GET /api/hr/jd
- PUT /api/hr/jd/{id}
- DELETE /api/hr/jd/{id}

### Candidate
- POST /api/candidates/register
- POST /api/candidates/apply
- GET /api/candidates/my-status

### Panel
- POST /api/v1/panel/create
- POST /api/v1/panel/activate
- GET /api/v1/panel/interviews

### Interview
- POST /api/interview/schedule
- POST /api/interview/feedback

---


## Project Structure

    Capstone_Interview_Tracking_System/
    │
    ├── frontend/
    │   ├── pages/
    │   ├── scripts/
    │   └── styles/
    │
    ├── backend/
    │   ├── controller/
    │   ├── service/
    │   ├── repository/
    │   ├── entity/
    │   ├── security/
    │   └── config/
    │
    └── README.md

---

## Setup Instructions

### Backend

1. Clone the repository

       git clone https://github.com/RajwanshBhati/dev-practice-repo.git

2. Navigate to backend folder

       cd Rajwansh_java_training/Capstone_Interview_Tracking_System/backend

3. DataBase Setup

- Create PostgreSQL database

      CREATE DATABASE its_db;

4. Backend Configuration

- Update the file
backend/src/main/resources/application.properties

      spring.datasource.url=jdbc:postgresql://localhost:5432/its_db
      spring.datasource.username=your_username
      spring.datasource.password=your_password

- for Email Configuration

      spring.mail.username=your_email@gmail.com
      spring.mail.password=your_app_password


5. Run the backend application

       mvn spring-boot:run

       Backend runs on:
       http://localhost:8080

---

### Frontend

    cd Rajwansh_java_training/Capstone_Interview_Tracking_System/frontend

Open: http://127.0.0.1:5500

---

## Testing

- Use Postman collection to test APIs
- JWT token required for protected APIs
- Code coverage using JaCoCo (~85%)

---

## Workflow Example

     HR creates Job Description
                ↓
     Candidate applies
                ↓
     HR schedules interview
                ↓
     Panel submits feedback
                ↓
     HR takes final decision

---

## Highlights

- Clean layered architecture
- Secure authentication using JWT
- Role-based access control
- Modular and scalable design
- Real-world recruitment workflow

---

## Conclusion

This system provides a structured and efficient way to manage recruitment processes. It ensures better tracking, improved transparency, and secure communication between HR, candidates, and panel members.

---

## Developed By

Rajwansh Bhati

