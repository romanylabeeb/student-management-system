package com.boubyan.api.controller;

import com.boubyan.api.dto.StudentDetailsDto;
import com.boubyan.api.dto.StudentDto;
import com.boubyan.api.exception.StudentException;
import com.boubyan.api.model.Course;
import com.boubyan.api.model.Student;
import com.boubyan.api.service.CourseRegistrationService;
import com.boubyan.api.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final CourseRegistrationService courseRegistrationService;

    // Get all students
    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // Get a student by ID
    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailsDto> getStudentById(@PathVariable Long id) throws StudentException {
        Student student = studentService.findStudentById(id);
        List<Course> registeredCourses = courseRegistrationService.findRegisteredCoursesByStudentId(id);
        StudentDetailsDto dto = new StudentDetailsDto(student, registeredCourses);
        return ResponseEntity.ok(dto);
    }

    // Get all registered courses for a student
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Student')")
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<Course>> getRegisteredCourses(@PathVariable Long id) {
        List<Course> registeredCourses = courseRegistrationService.findRegisteredCoursesByStudentId(id);
        return ResponseEntity.ok(registeredCourses);
    }

    // Get unregistered courses for a student by course name
    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Student')")
    @GetMapping("/{id}/courses/unregistered")
    public ResponseEntity<List<Course>> searchUnregisteredCourses(
            @PathVariable Long id,
            @RequestParam(value = "key", required = false) String courseName) {

        List<Course> unregisteredCourses = courseRegistrationService.getUnregisteredCoursesForStudent(id, courseName);
        return ResponseEntity.ok(unregisteredCourses);
    }


    // Create a new student
    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentDto studentDto) {
        Student createdStudent = studentService.createStudent(studentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    // Admin can update any student
    @PreAuthorize("hasAuthority('Admin')")
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto studentDto) throws StudentException {
        Student updatedStudent = studentService.updateStudent(id, studentDto);
        return updatedStudent != null ? ResponseEntity.ok(updatedStudent) : ResponseEntity.notFound().build();
    }
}

