package com.boubyan.api.dao;

import com.boubyan.api.model.CourseRegistration;
import com.boubyan.api.model.CourseRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRegistrationDao extends JpaRepository<CourseRegistration, CourseRegistrationId> {
//    default CourseRegistration findById(Long courseId, Long studentId) {
//        return findById(new CourseRegistrationId(courseId, studentId)).orElse(null);
//    }
//    List<CourseRegistration> findByStudent(Student student);
//
//    List<CourseRegistration> findByCourse(Course course);
//
//    @Query(value = "SELECT cr.* FROM course_registration cr " +
//            "WHERE cr.course_id = cr.course_id AND cr.student_id = :studentId", nativeQuery = true)
//     CourseRegistration findByCourseIdAndStudentId(Long courseId, Long studentId);
}

