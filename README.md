# Student Management System

## This repository contains two main parts:

1. student-management-app: An Angular application that provides the user interface for managing students and courses.
2. restApi_app: A Spring Boot REST API that serves as the backend for handling student/course data and business logic.
3. microservice_app: A set of Spring Boot microservices for managing student operations, course operations, authentication. 

## Getting Started
### Back-end Setup (Spring Boot) 
 - [restApi_app](./restApi_app)
## Front-end Setup 
- [student-management-app](./student-management-app)
    - Navigate to the student-management-ap/ directory:
        `cd student-management-app`
    - Install dependencies `npm install`
    - Update apiUrl in ApiService file to point your springboot app `localhost:8080`

## student-management-microservices 
 -  [microservice_app](./microservice_app) This includes:
    - sm-domain-service: Contains the data model.
    - auth-service: Manages admin login, registration, and token refresh.
    - sm-service-registery: Eureka server for service discovery.
    - sm-gateway-service: API gateway for routing requests.
    - sm-student-service: Manages student operations and exports registered courses.
    - sm-course-service: Manages course operations, student enrollments, and registrations.
 


