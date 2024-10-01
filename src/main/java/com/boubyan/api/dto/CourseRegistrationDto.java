package com.boubyan.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CourseRegistrationDto {
    @NotNull(message = "courseId is required")
    @Positive(message = "courseId must be positive")
    private Long courseId;
    private long studentId;

}
