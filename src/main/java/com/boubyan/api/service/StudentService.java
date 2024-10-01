package com.boubyan.api.service;

import com.boubyan.api.dao.StudentRepository;
import com.boubyan.api.dto.StudentDto;
import com.boubyan.api.exception.StudentNotFoundException;
import com.boubyan.api.model.Student;
import com.boubyan.api.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserService userService;

    @Autowired
    public StudentService(StudentRepository studentRepository, UserService userService) {
        this.studentRepository = studentRepository;
        this.userService = userService;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElse(null);
    }

    public Student getStudentDetailsById(Long id) {
        return findStudentById(id); // Reuse existing method for consistency
    }

    @Transactional
    public Student createStudent(StudentDto studentDto) {
        // Create and save User entity for authentication
        User user = userService.saveUser(studentDto.getUserDto());

        // Create and save Student entity
        Student student = studentDto.getStudent(user.getUserId());
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, StudentDto studentDetails) {
        Student student = findStudentById(id);
        if (student != null) {
            student.setFirstName(studentDetails.getFirstName());
            student.setLastName(studentDetails.getLastName());
            return studentRepository.save(student);
        }
        return null; // Optionally throw HTTP 404 exception
    }

    public Student getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .orElse(null);
    }

    public List<Student> findRegisteredStudentsByCourseId(Long courseId) {
        return studentRepository.findRegisteredStudentsByCourseId(courseId);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
