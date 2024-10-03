# Student Management Microservices Application
- This is a microservices-based student management system built using Spring Boot and Spring Cloud.
- The application includes several services that work together to manage students, courses, and authentication.

## Services Overview
## 1. sm-domain-service
-  Contains the common data models used across the microservices, such as Student, Course, and Course Registration.
## 2. auth-service
-  Handles authentication and authorization.
- Endpoints:
    - Admin Login
    - Admin Registration
    - Token Refresh
## 3. sm-service-registery
- Acts as a Eureka server for service discovery.
- Usage: All services register themselves here for communication and load balancing.
## 4. sm-gateway-service
- API Gateway for routing requests to appropriate services.
- Acts as a central entry point, forwarding requests to sm-student-service, sm-course-service, or auth-service based on the route.
## 5. sm-student-service
- Manages student operations.
- Features:
    - View student information
    - Export the list of registered courses for students
## 6. sm-course-service
- Manages course-related operations.
- Features:
    - Create, update, and delete courses
    - Register and unregister students for courses
    - Export student enrollments for a given course

## Architecture

### How to Run
1. Start Eureka Server (sm-service-registery):
    - `mvn spring-boot:run -pl sm-service-registery`
2. Start other services
    - `mvn spring-boot:run -pl auth-service`
    - `mvn spring-boot:run -pl sm-student-service`
    - `mvn spring-boot:run -pl sm-course-service`
3. Start API Gateway (sm-gateway-service):
    -`mvn spring-boot:run -pl sm-gateway-service`


## API Endpoints
Gateway URL: All endpoints are accessed via the API Gateway (sm-gateway-service).
 - Auth Service: /auth/**
 - Student Service: /students/**
 - Course Service: /courses/**
## Future Enhancements
- Separate Course Registration Microservice: Create a new microservice just for handling course registration.
- PDF Export Service: Build a service to generate and export PDF files for reports.
- Dedicated Databases: Use a separate database for each microservice.
- Message Broker: Implement a message broker for better communication between services.