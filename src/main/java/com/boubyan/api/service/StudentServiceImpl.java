package com.boubyan.api.service;

import com.boubyan.api.dao.StudentDao;
import com.boubyan.api.dto.StudentDto;
import com.boubyan.api.exception.StudentException;
import com.boubyan.api.model.Student;
import com.boubyan.api.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;
    private final UserService userService;

    @Autowired
    public StudentServiceImpl(StudentDao studentDao, UserService userService) {
        this.studentDao = studentDao;
        this.userService = userService;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDao.findAll();
    }

    @Override
    public Student findStudentById(Long studentId) throws StudentException {
        return studentDao.findById(studentId)
                .orElseThrow(() -> new StudentException("Student not found with ID: " + studentId));
    }

    @Override
    @Transactional
    public Student createStudent(StudentDto studentDto) {
        // Create and save User entity for authentication
        User user = userService.saveUser(studentDto.getUserDto());

        // Create and save Student entity
        Student student = studentDto.getStudent(user.getUserId());
        return studentDao.save(student);
    }

    @Override
    public Student updateStudent(Long id, StudentDto studentDetails) throws StudentException {
        Student student = findStudentById(id); // Ensure student exists
        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        return studentDao.save(student);
    }

    @Override
    public Student getStudentByUserId(Long userId) {
        return studentDao.findByUserId(userId)
                .orElse(null);
    }

    @Override
    public List<Student> findRegisteredStudentsByCourseId(Long courseId) {
        return studentDao.findRegisteredStudentsByCourseId(courseId);
    }

    @Override
    public void deleteStudent(Long id) {
        studentDao.deleteById(id);
    }
}
