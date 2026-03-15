#  Skyline Hotel Booking Service


Overview

    Skyline is a backend project built using Spring Boot.
    This project shows how to create secure REST APIs and manage application tasks in a clean way.




In this project we use:

    Spring Security for API security

    Cron Job for running automatic tasks

    Global Exception Handling for managing errors

    Soft Delete to safely delete data

    REST APIs to interact with the system

    This project follows a layered architecture, which makes the code clean and easy to maintain.




Technologies Used

    Backend

    Spring Boot

    Spring Web

    Security

    Spring Security



Database

    JPA / Hibernate

    MySQL / PostgreSQL

    

Other Tools

    Maven

    Lombok



Project Structure


    src/main/java

      controller
       - AuthController
       - UserController
       - EmployeeController

    service
     - AuthService
     - UserService
     - EmployeeService

    repository
     - UserRepository
     - EmployeeRepository

    entity
     - User
     - Employee

    security
     - SecurityConfig
     - JwtFilter

    exception
    - GlobalExceptionHandler
     - CustomException

    scheduler
     - CronJobService

    application
     - SkylineApplication

     
Explanation

    controller
       Controllers handle API requests from the client.
       They receive requests and call the service layer.

    Example:

       AuthController → handles login and authentication APIs

      UserController → manages user related APIs

     EmployeeController → manages employee related APIs

    service

      Service layer contains the main business logic of the application.

    repository

     Repository layer is used to communicate with the database using JPA.

    entity

    Entity classes represent database tables as Java classes.

    security

     Contains Spring Security configuration to protect APIs.

    exception

     Handles errors and exceptions in one place using a global exception handler.

    scheduler

     Contains Cron Jobs which run automatically at scheduled times.

    application

    The main class that starts the Spring Boot application.



Features

       1. Spring Security

      Spring Security is used to secure the APIs.

      It helps to:

    Protect endpoints

    Allow only authorized users

    Prevent unauthorized access

    Example:

    /api/public/**   -> accessible to everyone
    /api/admin/**    -> only admin can access
    
    2. Cron Job

    Cron jobs are used to run tasks automatically at a specific time.

    Example uses:

      Running background jobs

      Scheduled system tasks

     Automatic updates

    Example cron expression:

      0 0 * * * *

    This means the task runs every hour.


    3. Global Exception Handling

     Global exception handling is used to handle application errors in one place.

    Benefits:

    Cleaner code

    Consistent error messages

    Easier debugging

    Example error response:

      {
     "message": "Employee not found",
     "status": 404
    }


    4. Soft Delete

     Instead of deleting data permanently, this project uses Soft Delete.

     When a record is deleted:

     The record remains in the database

    A flag like deleted = true is set

    Example:

    deleted = true

    Benefits:

     Data is not permanently lost

    Records can be recovered

    Safer data management


    5. REST APIs

     The application provides REST APIs to manage data.

    Example APIs:

    Create Data
    POST /api/employees
    Get All Data
    GET /api/employees
    Update Data
    PUT /api/employees/{id}
    Delete Data (Soft Delete)
    DELETE /api/employees/{id}


    
Running the Project

     Requirements

     Java 17+

    Maven

    MySQL / PostgreSQL

    Steps
    
    1. Clone the project
    git clone <repository-url>
    
    2. Configure database

    Update database configuration in:

    application.properties

    Example:

     spring.datasource.url=jdbc:mysql://localhost:3306/skyline
    spring.datasource.username=root
    spring.datasource.password=password
    3. Build the project
    mvn clean install
    4. Run the application
    mvn spring-boot:run



Application will start at:

    http://localhost:8080
    Example API Request

    Create Employee

    POST /api/employees

    Request Body

    {
       "name": "John",
      "department": "IT"
    }

    Response

     Employee created successfully


     
Project Highlights

    Secure APIs using Spring Security

    Automated tasks using Cron Job

    Centralized Exception Handling

    Safe data deletion using Soft Delete

    Clean and structured Spring Boot architecture



Summary

    The Skyline project is a Spring Boot backend application that demonstrates how to build secure APIs and manage data properly.

    It includes important backend concepts like:

    Security

    Scheduling

    Error handling

    Soft Delete

   
