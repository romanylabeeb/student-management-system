package com.boubyan.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentDetailsDto {
    private Long studentId;
    private String firstName;
    private String lastName;
    private String email;
    private List<CourseDto> registeredCourses; // List of courses the student is registered for
}

