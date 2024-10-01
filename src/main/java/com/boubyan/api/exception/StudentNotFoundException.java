package com.boubyan.api.exception;

// Specific Exception for Student Not Found
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
}