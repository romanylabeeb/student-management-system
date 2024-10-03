package com.boubyan.api.controller;

import com.boubyan.api.dto.StudentCreateDto;
import com.boubyan.api.dto.StudentDetailsDto;
import com.boubyan.api.dto.StudentUpdateDto;
import com.boubyan.api.exception.StudentException;
import com.boubyan.api.model.Course;
import com.boubyan.api.model.Student;
import com.boubyan.api.service.CourseRegistrationService;
import com.boubyan.api.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final CourseRegistrationService courseRegistrationService;

    // get all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // get a student by ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailsDto> getStudentById(@PathVariable Long id) throws StudentException {
        Student student = studentService.getStudentById(id);
        List<Course> registeredCourses = courseRegistrationService.findRegisteredCoursesByStudentId(id);
        StudentDetailsDto dto = new StudentDetailsDto(student, registeredCourses);
        return ResponseEntity.ok(dto);
    }


    // create new student
    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentCreateDto dto) {
        Student createdStudent = studentService.createStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    // update student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentUpdateDto studentDto) throws StudentException {
        Student updatedStudent = studentService.updateStudent(id, studentDto);
        return ResponseEntity.ok(updatedStudent);
    }

    // delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) throws StudentException {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // get all registered courses for student
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<Course>> getRegisteredCourses(@PathVariable Long id) {
        List<Course> registeredCourses = courseRegistrationService.findRegisteredCoursesByStudentId(id);
        return ResponseEntity.ok(registeredCourses);
    }

    // search for unregistered courses for student by course name
    @GetMapping("/{id}/courses/unregistered")
    public ResponseEntity<List<Course>> searchUnregisteredCourses(
            @PathVariable Long id,
            @RequestParam(value = "key", required = false) String courseName) {
        List<Course> unregisteredCourses = courseRegistrationService.getUnregisteredCoursesForStudent(id, courseName);
        return ResponseEntity.ok(unregisteredCourses);
    }
    // export course details and students as PDF
    @GetMapping("{id}/pdf")
    public ResponseEntity<byte[]> exportStudentDetailsAsPdf(@PathVariable Long id) throws Exception {
        return courseRegistrationService.exportStudentDetailsAsPdf(id);
    }
}

