package com.boubyan.api.dto;

import com.boubyan.api.model.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseDto {
    @NotNull(message = "Course Name is required")
    private String courseName;

    private String description;

    public Course getCourse() {
        Course course = new Course();
        course.setCourseName(courseName);
        course.setDescription(description);
        return course;
    }
}