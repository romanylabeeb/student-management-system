package com.boubyan.api.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CourseRegistrationId implements Serializable {

  private Long courseId;
    private Long studentId;

    public CourseRegistrationId() {}

    public CourseRegistrationId(Long courseId, Long studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseRegistrationId that = (CourseRegistrationId) o;
        return Objects.equals(courseId, that.courseId) && Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, studentId);
    }
}
