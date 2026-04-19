## TODO Application (Spring Boot)

A clean and structured Spring Boot REST API for managing TODO tasks.
This project demonstrates layered architecture, DTO usage, validation, JPA (Hibernate), and clean coding practices.

## Features

- Create TODO
- Get all TODOs
- Get TODO by ID
- Update TODO
- Delete TODO
- Status transition validation (PENDING ↔ COMPLETED)
- Input validation using @Valid
- Exception handling
- Logging using SLF4J
- Unit Testing with high code coverage
- External service simulation (Notification Service)

## Tech Stack

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA (Hibernate)
- Jakarta Validation
- JUnit 5
- Mockito
- SLF4J Logger


## Architecture

🔹 Layers Overview

**Controller Layer**
- Handles incoming HTTP requests
- Maps endpoints using annotations like @RestController
- Delegates business logic to Service layer

**Service Layer**
- Contains core business logic
- Performs filtering, validation, and processing
- Acts as a bridge between Controller and Repository

**Repository Layer**
- Manages data storage
- Uses in-memory list to store dummy user data
- No database integration

**Model Layer**
- Represents data structure (User class)
-Used across the application

**DTO Layer**
- Used for data transfer (Request/Response objects)
- Helps in validation and clean API design

**Exception Layer**
- Handles global exceptions
- Ensures proper error responses

---

## Logging

Logging is implemented using **SLF4J Logger** across the application.

### Used in:
- Controller Layer
- Service Layer
- NotificationServiceClient

### Purpose:
- Track API calls
- Debug issues
- Monitor flow of application

## External Service Simulation

A dummy service is created to simulate interaction with an external system.

Class:

NotificationServiceClient

**Responsibilities**:
- Simulates sending notifications
- Logs actions like:
- TODO Created
- TODO Updated
- TODO Deleted

Example Flow:

When a TODO is created:

Service layer processes request
Calls NotificationServiceClient
Logs: "Notification sent for new TODO"

## Unit Testing (85%+ Coverage)

Unit testing is implemented using:

- JUnit 5
- Mockito

**Covered Layers**:

- Controller
- Service
- Exception Handler
- Client Layer

**Features**:
- Mocking dependencies using Mockito
- Testing all success & failure scenarios
- Achieved ~95% code coverage

**Run Tests**:
mvn clean test

**Generate Coverage Report**:
mvn test

**Then open**:

target/site/jacoco/index.html

![alt text](screenshot/TestCoverage.png)

## Setup & Run    

1. Clone the repository
git clone https://github.com/RajwanshBhati/dev-practice-repo.git
cd Rajwansh_java_training/session4/
2. Run the application
mvn spring-boot:run


## Testing API

1. CREATE TODO (POST)

![alt text](screenshot/createtodo.png)

**Validation in Create Todos**

![alt text](screenshot/validationcreate.png)

2. GET ALL TODOS

![alt text](screenshot/GetALL.png)

3. GET TODO BY ID

![alt text](screenshot/GetBYID.png)

**Validation in GET TODO BY ID**

![alt text](screenshot/GetByidnotfound.png)

4. UPDATE TODO

![alt text](screenshot/update.png)

5. DELETE TODO

![alt text](screenshot/deletesucc.png)

**Validation Delete**
![alt text](screenshot/deletetodo.png)

**Confirm Delete**
![alt text](screenshot/Deleteconfirm.png)


## Author

**Rajwansh Bhati**
