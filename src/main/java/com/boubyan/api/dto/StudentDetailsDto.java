package com.boubyan.api.dto;

import com.boubyan.api.model.Course;
import com.boubyan.api.model.Student;
import lombok.Data;

import java.util.List;


@Data
public class StudentDetailsDto {
    private Long studentId;
    private String firstName;
    private String lastName;
    private List<Course> registeredCourses;

    public StudentDetailsDto() {
    }

    public StudentDetailsDto(Student student, List<Course> registeredCourses) {
        this.studentId = student.getStudentId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.registeredCourses = registeredCourses;

    }
}
