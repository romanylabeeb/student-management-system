package com.boubyan.api.dto;

import com.boubyan.api.dao.StudentRepository;
import com.boubyan.api.model.Course;
import com.boubyan.api.model.CourseRegistration;
import com.boubyan.api.model.Student;
import lombok.Data;

import java.util.List;
import java.util.Set;


@Data
public class CourseDetailsDto {
    private Long courseId;
    private String courseName;
    private String description;
    private List<Student> registeredStudents; // List of students in the course

    public CourseDetailsDto() {
    }

    public CourseDetailsDto(Course course, List<Student> registeredStudents) {
        this.courseId = course.getCourseId();
        this.courseName = course.getCourseName();
        this.description = course.getDescription();
        this.registeredStudents = registeredStudents;
    }
}
