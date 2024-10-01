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
        CourseRegistration registration = buildCourseRegistrationByCourseIdAndStudentId(courseId, studentId);
        if (courseRegistrationDao.existsById(registration.getId())) {
            throw new CustomException("The student is already registered in this course.");
        }
        return courseRegistrationDao.save(registration);
    }

    @Override
    public boolean removeStudentFromCourse(Long courseId, Long studentId) throws Exception {
        CourseRegistration registration = buildCourseRegistrationByCourseIdAndStudentId(courseId, studentId);
        if (!courseRegistrationDao.existsById(registration.getId())) {
            throw new CustomException("The student is not registered in this course.");
        }
        courseRegistrationDao.deleteById(registration.getId());
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
        return courseDao.findRegisteredCoursesByStudentId(studentId);
    }

    @Override
    public List<Student> findRegisteredStudentsByCourseId(Long courseId) {
        return studentDao.findRegisteredStudentsByCourseId(courseId);
    }

    @Override
    public List<Course> getUnregisteredCoursesForStudent(Long studentId, String courseName) {
        if (courseName != null) {
            return courseDao.findUnregisteredCoursesByStudentIdAndCourseNameContaining(studentId, courseName);
        }
        return courseDao.findUnregisteredCoursesByStudentId(studentId);
    }

    @Override
    public List<Student> getUnregisteredStudentsForCourse(Long courseId, String studentName) {
        if (studentName != null) {
            return studentDao.findUnregisteredStudentsByCourseIdAndStudentNameContaining(courseId, studentName);
        }
        return studentDao.findUnregisteredStudentsByCourseId(courseId);
    }

    @Override
    public ResponseEntity<byte[]> exportPdf(Long courseId) throws Exception {
        Course course = courseDao.findById(courseId)
                .orElseThrow(() -> new CourseException("Course not found."));

        List<Student> registeredStudents = findRegisteredStudentsByCourseId(courseId);
        if (registeredStudents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No students registered for this course.".getBytes());
        }

        // Define headers for the PDF
        List<String> pdfColumns = Arrays.asList("ID", "First Name", "Last Name");
        List<List<String>> data = convertStudentsToListOfLists(registeredStudents);
        String pdfTitle = String.format("Registered Students for Course: %s", course.getCourseName());
        ByteArrayOutputStream pdfStream = PdfUtils.generatePdfStream(pdfTitle, pdfColumns, data);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=registered_students.pdf");
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
}
