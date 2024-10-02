package com.boubyan.api.dao;


import com.boubyan.api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao extends JpaRepository<Student, Long> {

    @Query(value = "SELECT s.* FROM student s " +
            "JOIN course_registration cr ON s.student_id = cr.student_id " +
            "WHERE cr.course_id = :courseId", nativeQuery = true)
    List<Student> findRegisteredStudentsByCourseId(@Param("courseId") Long courseId);


    // Query to find all unregistered courses for a student
    @Query(value = "SELECT s.* FROM student s " +
            "WHERE s.student_id NOT IN (SELECT cr.student_id FROM course_registration cr " +
            "WHERE cr.course_id = :courseId)", nativeQuery = true)
    List<Student> findUnregisteredStudentsByCourseId(@Param("courseId") Long courseId);


    @Query(value = "SELECT s.* FROM student s " +
            "WHERE (LOWER(s.first_name) LIKE LOWER(CONCAT('%', :studentName, '%'))" +
            "OR LOWER(s.last_name) LIKE LOWER(CONCAT('%', :studentName, '%'))) AND " +
            "s.student_id NOT IN (SELECT cr.student_id FROM course_registration cr " +
            "WHERE cr.course_id = :courseId)", nativeQuery = true)
    List<Student> findUnregisteredStudentsByCourseIdAndStudentNameContaining(@Param("courseId") Long courseId,
                                                                             @Param("studentName") String studentName);
}
