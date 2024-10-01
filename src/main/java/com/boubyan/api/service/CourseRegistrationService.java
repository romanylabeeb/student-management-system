package com.boubyan.api.service;

import com.boubyan.api.dao.CourseRegistrationRepository;
import com.boubyan.api.dao.CourseRepository;
import com.boubyan.api.dao.StudentRepository;
import com.boubyan.api.dto.CourseDto;
import com.boubyan.api.dto.CourseRegistrationDto;
import com.boubyan.api.model.Course;
import com.boubyan.api.model.CourseRegistration;
import com.boubyan.api.model.CourseRegistrationId;
import com.boubyan.api.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CourseRegistrationService {

    private final CourseRegistrationRepository courseRegistrationRepository;
    private final CourseService courseService;
    private final StudentService studentService;

    @Autowired
    public CourseRegistrationService(CourseRegistrationRepository courseRegistrationRepository,
                                     CourseService courseService,
                                     StudentService studentService) {
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    public CourseRegistration registerCourse(Long studentId, CourseRegistrationDto registrationDto) {
        Course course = courseService.findCourseById(registrationDto.getCourseId()); // Using CourseService
        Student student = studentService.findStudentById(studentId);  // Using StudentService

        if (course != null && student != null) {
            if (courseRegistrationRepository.findById(course.getCourseId(), studentId) != null) {
                return null; // Course already registered
            }

            CourseRegistration registration = new CourseRegistration(student, course);
            return courseRegistrationRepository.save(registration);
        }

        return null; // Course or student not found
    }

    public List<CourseRegistration> getRegistrationsByStudent(Long studentId) {
        Student student = studentService.findStudentById(studentId);  // Using StudentService
        return student != null ? courseRegistrationRepository.findByStudent(student) : new ArrayList<>();
    }

    public List<CourseRegistration> getRegistrationsByCourse(Long courseId) {
        Course course = courseService.findCourseById(courseId);  // Using CourseService
        return course != null ? courseRegistrationRepository.findByCourse(course) : new ArrayList<>();
    }
}


