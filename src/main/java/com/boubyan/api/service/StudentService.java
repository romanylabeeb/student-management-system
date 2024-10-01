package com.boubyan.api.service;

import com.boubyan.api.dto.StudentDto;
import com.boubyan.api.exception.StudentException;
import com.boubyan.api.model.Student;

import java.util.List;

public interface StudentService {

    List<Student> getAllStudents();

    Student findStudentById(Long studentId) throws StudentException;

    Student createStudent(StudentDto studentDto);

    Student updateStudent(Long id, StudentDto studentDetails) throws StudentException;

    Student getStudentByUserId(Long userId);

    List<Student> findRegisteredStudentsByCourseId(Long courseId);

    void deleteStudent(Long id);
}
