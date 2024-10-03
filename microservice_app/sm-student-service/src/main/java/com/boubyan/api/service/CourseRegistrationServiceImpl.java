package com.boubyan.api.service;

import com.boubyan.api.dao.CourseDao;
import com.boubyan.api.dao.CourseRegistrationDao;
import com.boubyan.api.dao.StudentDao;
import com.boubyan.api.exception.CourseException;
import com.boubyan.api.exception.CustomException;
import com.boubyan.api.exception.StudentException;
import com.boubyan.api.model.Course;
import com.boubyan.api.model.CourseRegistration;
import com.boubyan.api.model.Student;
import com.boubyan.api.pdf.PdfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CourseRegistrationServiceImpl implements CourseRegistrationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseRegistrationServiceImpl.class);
    private final CourseDao courseDao;
    private final StudentDao studentDao;
    private final CourseRegistrationDao courseRegistrationDao;

    @Autowired
    public CourseRegistrationServiceImpl(CourseDao courseDao, CourseRegistrationDao courseRegistrationDao, StudentDao studentDao) {
        this.courseDao = courseDao;
        this.courseRegistrationDao = courseRegistrationDao;
        this.studentDao = studentDao;
    }

    @Override
    public CourseRegistration assignStudentToCourse(Long courseId, Long studentId) throws Exception {
        this.LOGGER.info(String.format("Register studentId: %d in courseId: %d", studentId, courseId));
        CourseRegistration registration = buildCourseRegistrationByCourseIdAndStudentId(courseId, studentId);
        if (courseRegistrationDao.existsById(registration.getId())) {
            this.LOGGER.warn(String.format("The studentId %d is already registered in courseId: %d", studentId, courseId));
            throw new CustomException("The student is already registered in this course.");
        }
        this.LOGGER.info(String.format("Registering student: %s in course: %s", registration.getStudent().getFirstName(), registration.getCourse().getCourseName()));

        return courseRegistrationDao.save(registration);
    }

    @Override
    public boolean removeStudentFromCourse(Long courseId, Long studentId) throws Exception {
        this.LOGGER.info(String.format("unregister studentId: %d in courseId: %d", studentId, courseId));

        CourseRegistration registration = buildCourseRegistrationByCourseIdAndStudentId(courseId, studentId);
        if (!courseRegistrationDao.existsById(registration.getId())) {
            this.LOGGER.warn(String.format("The studentId %d is not registered in courseId: %d", studentId, courseId));
            throw new CustomException("The student is not registered in this course.");
        }
        courseRegistrationDao.deleteById(registration.getId());
        this.LOGGER.info(String.format("unregistered student: %s in course: %s successfully.", registration.getStudent().getFirstName(), registration.getCourse().getCourseName()));
        return true;
    }

    private CourseRegistration buildCourseRegistrationByCourseIdAndStudentId(long courseId, long studentId) throws Exception {
        Course course = courseDao.findById(courseId)
                .orElseThrow(() -> new CourseException("Course not found."));
        Student student = studentDao.findById(studentId)
                .orElseThrow(() -> new StudentException("Student not found."));
        return new CourseRegistration(course, student);
    }

    @Override
    public List<Course> findRegisteredCoursesByStudentId(Long studentId) {
        this.LOGGER.info(String.format("find all registered courses for studentId: %d", studentId));
        return courseDao.findRegisteredCoursesByStudentId(studentId);
    }

    @Override
    public List<Student> findRegisteredStudentsByCourseId(Long courseId) {
        this.LOGGER.info(String.format("find all registered students for courseId: %d", courseId));
        return studentDao.findRegisteredStudentsByCourseId(courseId);
    }

    @Override
    public List<Course> getUnregisteredCoursesForStudent(Long studentId, String courseName) {
        if (courseName != null) {
            this.LOGGER.info(String.format("search by courseName: %s for all unregistered courses for studentId: %d", courseName, studentId));

            return courseDao.findUnregisteredCoursesByStudentIdAndCourseNameContaining(studentId, courseName);
        }
        this.LOGGER.info(String.format("search for all unregistered courses for studentId: %d", studentId));

        return courseDao.findUnregisteredCoursesByStudentId(studentId);
    }

    @Override
    public List<Student> getUnregisteredStudentsForCourse(Long courseId, String studentName) {
        if (studentName != null) {
            this.LOGGER.info(String.format("search by studentName: %s for all unregistered students for courseId: %d", studentName, courseId));

            return studentDao.findUnregisteredStudentsByCourseIdAndStudentNameContaining(courseId, studentName);
        }
        this.LOGGER.info(String.format("search for all unregistered students for courseId: %d", courseId));
        return studentDao.findUnregisteredStudentsByCourseId(courseId);
    }

    @Override
    public ResponseEntity<byte[]> exportStudentDetailsAsPdf(Long id) throws Exception {
        this.LOGGER.info(String.format("exporting studentDetails as pdf for studentId: %d", id));

        Student student = studentDao.findById(id)
                .orElseThrow(() -> new StudentException("Student not found."));

        List<Course> registeredCourses = findRegisteredCoursesByStudentId(id);
        if (registeredCourses.isEmpty()) {

            this.LOGGER.info(String.format("No courses registered for this student for studentId: %d", id));

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No courses registered for this student.".getBytes());
        }

        List<String> pdfColumns = Arrays.asList("ID", "Course Name", "Course Description");
        List<List<String>> data = convertCoursesToListOfLists(registeredCourses);
        String pdfTitle = String.format("Registered Courses for Student: %s", String.format("%s %s", student.getFirstName(), student.getLastName()));
        return exportPdf(pdfColumns, data, pdfTitle);
    }

    @Override
    public ResponseEntity<byte[]> exportCourseDetailsAsPdf(Long courseId) throws Exception {
        Course course = courseDao.findById(courseId)
                .orElseThrow(() -> new CourseException("Course not found."));

        List<Student> registeredStudents = findRegisteredStudentsByCourseId(courseId);
        if (registeredStudents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No students registered for this course.".getBytes());
        }

        List<String> pdfColumns = Arrays.asList("ID", "First Name", "Last Name");
        List<List<String>> data = convertStudentsToListOfLists(registeredStudents);
        String pdfTitle = String.format("Registered Students for Course: %s", course.getCourseName());
        return exportPdf(pdfColumns, data, pdfTitle);
    }

    private ResponseEntity<byte[]> exportPdf(List<String> pdfColumns, List<List<String>> data, String pdfTitle) throws Exception {
        ByteArrayOutputStream pdfStream = PdfUtils.generatePdfStream(pdfTitle, pdfColumns, data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sm_file.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }

    private List<List<String>> convertStudentsToListOfLists(List<Student> registeredStudents) {
        List<List<String>> result = new ArrayList<>();
        for (Student student : registeredStudents) {
            result.add(Arrays.asList(
                    String.valueOf(student.getStudentId()),
                    student.getFirstName(),
                    student.getLastName()
            ));
        }
        return result;
    }

    private List<List<String>> convertCoursesToListOfLists(List<Course> registeredCourses) {
        List<List<String>> result = new ArrayList<>();
        for (Course course : registeredCourses) {
            result.add(Arrays.asList(
                    String.valueOf(course.getCourseId()),
                    course.getCourseName(),
                    course.getDescription()
            ));
        }
        return result;
    }
}
