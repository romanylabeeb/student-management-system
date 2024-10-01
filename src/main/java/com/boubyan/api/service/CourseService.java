package com.boubyan.api.service;

import com.boubyan.api.dto.CourseDto;
import com.boubyan.api.exception.CourseException;
import com.boubyan.api.model.Course;

import java.util.List;

public interface CourseService {

    Course createCourse(CourseDto courseDto);

    List<Course> getAllCourses();

    Course findCourseById(Long courseId) throws CourseException;

    Course updateCourse(Long courseId, CourseDto courseDto) throws CourseException;

    void deleteCourse(Long courseId) throws CourseException;
}
