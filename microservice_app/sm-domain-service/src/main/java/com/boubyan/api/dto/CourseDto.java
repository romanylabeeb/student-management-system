package com.boubyan.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseDto {
    @NotNull(message = "Course Name is required")
    private String courseName;

    private String description;

}