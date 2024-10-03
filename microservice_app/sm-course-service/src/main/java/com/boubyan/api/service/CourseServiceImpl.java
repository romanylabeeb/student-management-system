package com.boubyan.api.service;

import com.boubyan.api.dao.CourseDao;
import com.boubyan.api.dto.CourseDto;
import com.boubyan.api.exception.CourseException;
import com.boubyan.api.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);

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

        this.LOGGER.info(String.format("get course details by courseId: %d", courseId));

        return courseDao.findById(courseId)
                .orElseThrow(() -> new CourseException("Course not found with ID: " + courseId));
    }

    @CacheEvict(value = "courses", allEntries = true)
    @Override
    public Course createCourse(CourseDto courseDto) {
        this.LOGGER.info(String.format("creating course courseName: %s", courseDto.getCourseName()));
        return courseDao.save(buildCourseFromDto(courseDto));
    }

    @CacheEvict(cacheNames = {"course", "courses"}, allEntries = true)
    @Override
    public Course updateCourse(Long courseId, CourseDto courseDto) throws CourseException {
        this.LOGGER.info(String.format("updating course courseName: %s", courseDto.getCourseName()));

        Course existingCourse = getCourseById(courseId);
        updateCourseFromDto(existingCourse, courseDto);
        return courseDao.save(existingCourse);
    }

    @CacheEvict(cacheNames = {"course", "courses"}, allEntries = true)
    @Override
    public void deleteCourse(Long courseId) throws CourseException {
        this.LOGGER.info(String.format("deleting course courseId: %d", courseId));

        if (notExist(courseId)) {
            this.LOGGER.warn(String.format("Course not found with courseId: %d", courseId));
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
