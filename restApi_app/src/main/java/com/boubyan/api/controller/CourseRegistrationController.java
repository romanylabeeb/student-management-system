package com.boubyan.api.controller;

import com.boubyan.api.model.CourseRegistration;
import com.boubyan.api.service.CourseRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses/{courseId}/students")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('Admin')")
public class CourseRegistrationController {

    private final CourseRegistrationService courseRegistrationService;


    // assign a student to course
    @PostMapping("/{studentId}")
    public ResponseEntity<CourseRegistration> assignStudentToCourse(@PathVariable Long courseId, @PathVariable Long studentId) throws Exception {
        CourseRegistration assigned = courseRegistrationService.assignStudentToCourse(courseId, studentId);
        return ResponseEntity.ok(assigned);
    }

    // remove student from course
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> removeStudentFromCourse(@PathVariable Long courseId, @PathVariable Long studentId) throws Exception {
        boolean removed = courseRegistrationService.removeStudentFromCourse(courseId, studentId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
