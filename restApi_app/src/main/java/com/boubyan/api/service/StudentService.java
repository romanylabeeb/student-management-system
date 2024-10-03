package com.boubyan.api.service;

import com.boubyan.api.dto.StudentCreateDto;
import com.boubyan.api.dto.StudentUpdateDto;
import com.boubyan.api.exception.StudentException;
import com.boubyan.api.model.Student;

import java.util.List;

public interface StudentService {

    List<Student> getAllStudents();

    Student getStudentById(Long studentId) throws StudentException;

    Student createStudent(StudentCreateDto studentDto);

    Student updateStudent(Long id, StudentUpdateDto studentDetails) throws StudentException;
    void deleteStudent(Long id) throws StudentException;

    List<Student> findRegisteredStudentsByCourseId(Long courseId);

}
