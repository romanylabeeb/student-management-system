package com.boubyan.api.service;

import com.boubyan.api.dao.StudentDao;
import com.boubyan.api.dto.StudentCreateDto;
import com.boubyan.api.dto.StudentUpdateDto;
import com.boubyan.api.exception.StudentException;
import com.boubyan.api.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;

    @Autowired
    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Cacheable(value = "students")
    @Override
    public List<Student> getAllStudents() {
        return studentDao.findAll();
    }

    @Cacheable(value = "student", key = "#studentId")
    @Override
    public Student getStudentById(Long studentId) throws StudentException {
        return studentDao.findById(studentId)
                .orElseThrow(() -> new StudentException("Student not found with ID: " + studentId));
    }

    @CacheEvict(value = "students", allEntries = true)
    @Override
    public Student createStudent(StudentCreateDto studentDto) {
        // Create and save User entity for authentication
//        User user = userService.saveUser(studentDto.getUserDto());

        // Create and save Student entity
        Student student = this.buildStudent(studentDto);
        return studentDao.save(student);
    }

    @CacheEvict(cacheNames = {"students", "student"}, allEntries = true)
    @Override
    public Student updateStudent(Long id, StudentUpdateDto studentDetails) throws StudentException {
        Student student = getStudentById(id); // Ensure student exists
        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        return studentDao.save(student);
    }

    @CacheEvict(cacheNames = {"students", "student"}, allEntries = true)
    @Override
    public void deleteStudent(Long id) throws StudentException {
        if (notExist(id)) {
            throw new StudentException("Student not found with ID: " + id);
        }
        studentDao.deleteById(id);
    }


    @Override
    public List<Student> findRegisteredStudentsByCourseId(Long courseId) {
        return studentDao.findRegisteredStudentsByCourseId(courseId);
    }

    private boolean notExist(long id) {
        return !studentDao.existsById(id);
    }
    private Student buildStudent(StudentCreateDto dto) {
        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        return student;
    }
}
