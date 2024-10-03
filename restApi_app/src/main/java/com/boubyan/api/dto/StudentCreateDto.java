package com.boubyan.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentCreateDto {
    @NotNull(message = "FirstName is required")
    private String firstName;

    @NotNull(message = "LastName is required")
    private String lastName;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
}


