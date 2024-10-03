package com.boubyan.api.service;

import com.boubyan.api.model.Course;
import com.boubyan.api.model.CourseRegistration;
import com.boubyan.api.model.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseRegistrationService {
    CourseRegistration assignStudentToCourse(Long courseId, Long studentId) throws Exception;

    boolean removeStudentFromCourse(Long courseId, Long studentId) throws Exception;

    List<Course> findRegisteredCoursesByStudentId(Long studentId);

    List<Student> findRegisteredStudentsByCourseId(Long courseId);

    List<Course> getUnregisteredCoursesForStudent(Long studentId, String courseName);

    List<Student> getUnregisteredStudentsForCourse(Long courseId, String studentName);


    ResponseEntity<byte[]> exportStudentDetailsAsPdf(Long id)throws Exception;

    ResponseEntity<byte[]> exportCourseDetailsAsPdf(Long id)throws Exception;
}
