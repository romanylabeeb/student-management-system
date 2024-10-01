package com.boubyan.api.controller;
import com.boubyan.api.dto.CourseRegistrationDto;
import com.boubyan.api.model.CourseRegistration;
import com.boubyan.api.service.CourseRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class CourseRegistrationController {

    @Autowired
    private CourseRegistrationService courseRegistrationService;

    // Register a student for a course
    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/{studentId}")
    public ResponseEntity<CourseRegistration> registerCourse(@PathVariable Long studentId,
                                                             @Valid @RequestBody CourseRegistrationDto registrationDto) {
        CourseRegistration registration = courseRegistrationService.registerCourse(studentId, registrationDto);
        return registration != null ? ResponseEntity.status(HttpStatus.CREATED).body(registration) : ResponseEntity.notFound().build();
    }

    // Get registrations for a student
    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/{studentId}")
    public ResponseEntity<List<CourseRegistration>> getRegistrations(@PathVariable Long studentId) {
        List<CourseRegistration> registrations = courseRegistrationService.getRegistrationsByStudent(studentId);
        return ResponseEntity.ok(registrations);
    }


}
