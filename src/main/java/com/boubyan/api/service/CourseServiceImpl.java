package com.boubyan.api.service;

import com.boubyan.api.dao.CourseDao;
import com.boubyan.api.dto.CourseDto;
import com.boubyan.api.exception.CourseException;
import com.boubyan.api.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;

    @Autowired
    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public Course createCourse(CourseDto courseDto) {
        return courseDao.save(courseDto.getCourse());
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDao.findAll();
    }

    @Override
    public Course findCourseById(Long courseId) throws CourseException {
        return courseDao.findById(courseId)
                .orElseThrow(() -> new CourseException("Course not found with ID: " + courseId));
    }

    @Override
    public Course updateCourse(Long courseId, CourseDto courseDto) throws CourseException {
        Course course = findCourseById(courseId); // Ensure course exists
        course.setCourseName(courseDto.getCourseName());
        course.setDescription(courseDto.getDescription());
        return courseDao.save(course);
    }

    @Override
    public void deleteCourse(Long courseId) throws CourseException {
        Course course = findCourseById(courseId); // Ensure course exists before deleting
        courseDao.deleteById(course.getCourseId());
    }
}

