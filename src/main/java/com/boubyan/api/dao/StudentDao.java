package com.boubyan.api.dao;

import com.boubyan.api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentDao extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);

    Optional<Student> findByUserId(Long userId);

    @Query(value = "SELECT s.* FROM student s " +
            "JOIN course_registration cr ON s.student_id = cr.student_id " +
            "WHERE cr.course_id = :courseId", nativeQuery = true)
    List<Student> findRegisteredStudentsByCourseId(@Param("courseId") Long courseId);
}
