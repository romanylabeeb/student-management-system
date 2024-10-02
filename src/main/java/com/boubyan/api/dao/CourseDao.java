package com.boubyan.api.dao;

import com.boubyan.api.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDao extends JpaRepository<Course, Long> {

    // To find a CourseRegistration by studentId

    @Query(value = "SELECT c.* FROM course c " +
            "JOIN course_registration cr ON c.course_id = cr.course_id " +
            "WHERE cr.student_id = :studentId", nativeQuery = true)
    List<Course> findRegisteredCoursesByStudentId(@Param("studentId") Long studentId);

    // Query to find all unregistered courses for a student
    @Query(value = "SELECT c.* FROM course c " +
            "WHERE c.course_id NOT IN (SELECT cr.course_id FROM course_registration cr " +
            "WHERE cr.student_id = :studentId)", nativeQuery = true)
    List<Course> findUnregisteredCoursesByStudentId(@Param("studentId") Long studentId);

    // Query to find unregistered courses for a student by course name
    @Query(value = "SELECT c.* FROM course c " +
            "WHERE LOWER(c.course_name) LIKE LOWER(CONCAT('%', :courseName, '%')) AND " +
            "c.course_id NOT IN (SELECT cr.course_id FROM course_registration cr " +
            "WHERE cr.student_id = :studentId)", nativeQuery = true)
    List<Course> findUnregisteredCoursesByStudentIdAndCourseNameContaining(@Param("studentId") Long studentId,
                                                                           @Param("courseName") String courseName);

}

