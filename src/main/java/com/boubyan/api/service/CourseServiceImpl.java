package com.boubyan.api.service;

import com.boubyan.api.dao.CourseDao;
import com.boubyan.api.dto.CourseDto;
import com.boubyan.api.exception.CourseException;
import com.boubyan.api.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;

    @Autowired
    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Cacheable(value = "courses")
    @Override
    public List<Course> getAllCourses() {
        return courseDao.findAll();
    }

    @Cacheable(value = "course", key = "#courseId")
    @Override
    public Course findCourseById(Long courseId) throws CourseException {
        return courseDao.findById(courseId)
                .orElseThrow(() -> new CourseException("Course not found with ID: " + courseId));
    }

    @CacheEvict(value = "courses", allEntries = true)
    @Override
    public Course createCourse(CourseDto courseDto) {
        return courseDao.save(courseDto.getCourse());
    }
    @CacheEvict(value = "course", key = "#courseId")
    @Override
    public Course updateCourse(Long courseId, CourseDto courseDto) throws CourseException {
        // Ensure course exists
        Course course = findCourseById(courseId);
        course.setCourseName(courseDto.getCourseName());
        course.setDescription(courseDto.getDescription());
        return courseDao.save(course);
    }
    @CacheEvict(cacheNames = {"course","courses"}, allEntries = true) //  Evict all courses from the cache
    @Override
    public void deleteCourse(Long courseId) throws CourseException {
        // Ensure course exists before deleting
        Course course = findCourseById(courseId);
        courseDao.deleteById(course.getCourseId());
    }
}

