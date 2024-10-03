package com.boubyan.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentUpdateDto {
    @NotNull(message = "FirstName is required")
    private String firstName;

    @NotNull(message = "LastName is required")
    private String lastName;
}