package com.boubyan.api.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private String details;

    public ErrorResponse() {

    }

    public ErrorResponse( String message, String details) {
        super();
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.details = details;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


}
