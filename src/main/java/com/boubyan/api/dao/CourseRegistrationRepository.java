package com.boubyan.api.dao;

import com.boubyan.api.dto.CourseRegistrationDto;
import com.boubyan.api.model.Course;
import com.boubyan.api.model.CourseRegistration;
import com.boubyan.api.model.CourseRegistrationId;
import com.boubyan.api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, CourseRegistrationId> {
    default CourseRegistration findById(Long courseId, Long studentId) {
        return findById(new CourseRegistrationId(courseId, studentId)).orElse(null);
    }
    List<CourseRegistration> findByStudent(Student student);

    List<CourseRegistration> findByCourse(Course course);
}

