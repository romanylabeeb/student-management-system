package com.boubyan.api.service;

import com.boubyan.api.dao.StudentDao;
import com.boubyan.api.dto.StudentDto;
import com.boubyan.api.model.Student;
import com.boubyan.api.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentDao studentDao;
    private final UserService userService;

    @Autowired
    public StudentService(StudentDao studentDao, UserService userService) {
        this.studentDao = studentDao;
        this.userService = userService;
    }

    public List<Student> getAllStudents() {
        return studentDao.findAll();
    }

    public Student findStudentById(Long studentId) {
        return studentDao.findById(studentId)
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
        return studentDao.save(student);
    }

    public Student updateStudent(Long id, StudentDto studentDetails) {
        Student student = findStudentById(id);
        if (student != null) {
            student.setFirstName(studentDetails.getFirstName());
            student.setLastName(studentDetails.getLastName());
            return studentDao.save(student);
        }
        return null; // Optionally throw HTTP 404 exception
    }

    public Student getStudentByUserId(Long userId) {
        return studentDao.findByUserId(userId)
                .orElse(null);
    }

    public List<Student> findRegisteredStudentsByCourseId(Long courseId) {
        return studentDao.findRegisteredStudentsByCourseId(courseId);
    }

    public void deleteStudent(Long id) {
        studentDao.deleteById(id);
    }
}
