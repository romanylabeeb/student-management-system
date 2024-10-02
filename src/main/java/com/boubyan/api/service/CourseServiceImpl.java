package com.boubyan.api.service;

import com.boubyan.api.dao.CourseDao;
import com.boubyan.api.dto.CourseDto;
import com.boubyan.api.exception.CourseException;
import com.boubyan.api.exception.StudentException;
import com.boubyan.api.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public Course getCourseById(Long courseId) throws CourseException {
        return courseDao.findById(courseId)
                .orElseThrow(() -> new CourseException("Course not found with ID: " + courseId));
    }

    @CacheEvict(value = "courses", allEntries = true)
    @Override
    public Course createCourse(CourseDto courseDto) {
        return courseDao.save(buildCourseFromDto(courseDto));
    }

    @CacheEvict(cacheNames = {"course", "courses"}, allEntries = true)
    @Override
    public Course updateCourse(Long courseId, CourseDto courseDto) throws CourseException {
        Course existingCourse = getCourseById(courseId);
        updateCourseFromDto(existingCourse, courseDto);
        return courseDao.save(existingCourse);
    }

    @CacheEvict(cacheNames = {"course", "courses"}, allEntries = true)
    @Override
    public void deleteCourse(Long courseId) throws CourseException {
        if (notExist(courseId)) {
            throw new CourseException("Course not found with ID: " + courseId);
        }
        courseDao.deleteById(courseId);
    }

    private Course buildCourseFromDto(CourseDto courseDto) {
        Course course = new Course();
        course.setCourseName(courseDto.getCourseName());
        course.setDescription(courseDto.getDescription());
        return course;
    }

    private void updateCourseFromDto(Course course, CourseDto courseDto) {
        course.setCourseName(courseDto.getCourseName());
        course.setDescription(courseDto.getDescription());
    }

    private boolean notExist(long id) {
        return !courseDao.existsById(id);
    }
}
