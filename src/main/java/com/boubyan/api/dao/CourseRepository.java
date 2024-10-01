package com.boubyan.api.dao;

import com.boubyan.api.model.Course;
import com.boubyan.api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // To find a CourseRegistration by courseId and studentId

    @Query(value = "SELECT c.* FROM course c " +
            "JOIN course_registration cr ON c.course_id = cr.course_id " +
            "WHERE cr.student_id = :studentId", nativeQuery = true)
    List<Student> findRegisteredCoursesByStudentId(@Param("studentId") Long courseId);
}

