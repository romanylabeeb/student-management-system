package com.boubyan.api.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Data
@Entity
@Table(name = "course_registration")
public class CourseRegistration {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long registrationId;

    @EmbeddedId
    CourseRegistrationId id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    public CourseRegistration() {
    }
    public CourseRegistration( Course course,Student student) {
        this.student = student;
        this.course = course;
        this.id = new CourseRegistrationId(course.getCourseId(), student.getStudentId());
    }

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate = LocalDate.now();


}
