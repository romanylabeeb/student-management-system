package com.boubyan.api.controller;

import com.boubyan.api.dto.CourseDetailsDto;
import com.boubyan.api.dto.CourseDto;
import com.boubyan.api.exception.CourseException;
import com.boubyan.api.model.Course;
import com.boubyan.api.model.Student;
import com.boubyan.api.service.CourseRegistrationService;
import com.boubyan.api.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('Admin')")
public class CourseController {

    private final CourseService courseService;
    private final CourseRegistrationService courseRegistrationService;

    // get all courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    // get course by ID
    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailsDto> getCourseById(@PathVariable Long id) throws CourseException {
        Course course = courseService.getCourseById(id);
        List<Student> registeredStudents = courseRegistrationService.findRegisteredStudentsByCourseId(id);
        CourseDetailsDto courseDetails = new CourseDetailsDto(course, registeredStudents);
        return ResponseEntity.ok(courseDetails);
    }

    // create new course
    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CourseDto courseDto) {
        Course course = courseService.createCourse(courseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    // update course
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto courseDto) throws CourseException {
        Course updatedCourse = courseService.updateCourse(id, courseDto);
        return ResponseEntity.ok(updatedCourse);
    }

    // delete course
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) throws CourseException {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    // get all registered students for course
    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getRegisteredStudents(@PathVariable Long id) {
        List<Student> registeredStudents = courseRegistrationService.findRegisteredStudentsByCourseId(id);
        return ResponseEntity.ok(registeredStudents);
    }

    // search for unregistered students in course by student name as key
    @GetMapping("/{id}/students/unregistered")
    public ResponseEntity<List<Student>> searchUnregisteredCourses(
            @PathVariable Long id,
            @RequestParam(value = "key", required = false) String studentName) {
        List<Student> unregisteredCourses = courseRegistrationService.getUnregisteredStudentsForCourse(id, studentName);
        return ResponseEntity.ok(unregisteredCourses);
    }

    // export course details and enrolled students as PDF
    @GetMapping("{id}/pdf")
    public ResponseEntity<byte[]> exportCourseDetailsAsPdf(@PathVariable Long id) throws Exception {
        return courseRegistrationService.exportCourseDetailsAsPdf(id);
    }
}
