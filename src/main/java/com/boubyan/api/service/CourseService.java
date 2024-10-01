package com.boubyan.api.service;

import com.boubyan.api.dao.CourseRepository;
import com.boubyan.api.dto.CourseDetailsDto;
import com.boubyan.api.dto.CourseDto;
import com.boubyan.api.model.Course;
import com.boubyan.api.model.Student;
import com.boubyan.api.pdf.PdfUtils;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentService studentService;

    @Autowired
    public CourseService(CourseRepository courseRepository, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.studentService = studentService;
    }

    public Course createCourse(CourseDto courseDto) {
        return courseRepository.save(courseDto.getCourse());
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElse(null);
    }

    public CourseDetailsDto getCourseDetailsById(Long courseId) {
        Course course = findCourseById(courseId);
        if (course == null) {
            return null; // throw HTTP 404 exception if needed
        }
        List<Student> registeredStudents = studentService.findRegisteredStudentsByCourseId(courseId);
        return new CourseDetailsDto(course, registeredStudents);
    }

    public Course updateCourse(Long id, CourseDto courseDto) {
        Course course = findCourseById(id);
        if (course != null) {
            course.setCourseName(courseDto.getCourseName());
            course.setDescription(courseDto.getDescription());
            return courseRepository.save(course);
        }
        return null; // throw HTTP 404 exception if needed
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public ResponseEntity<byte[]> exportPdf(Long courseId) throws  DocumentException {
        Course course = findCourseById(courseId);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No students registered for this course.".getBytes());
        }
        List<Student> registeredStudents = studentService.findRegisteredStudentsByCourseId(courseId);
        if (registeredStudents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No students registered for this course.".getBytes());
        }

        // Define headers for the PDF
        List<String> pdfColumns = Arrays.asList("ID", "First Name", "Last Name");
        List<List<String>> data = convertStudentsToListOfLists(registeredStudents);
        String pdfTitle = String.format("Registered Students for Course :%s", course.getCourseName());
        ByteArrayOutputStream pdfStream = PdfUtils.generatePdfStream(pdfTitle, pdfColumns, data);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=query_results.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }

    // Conversion method
    private List<List<String>> convertStudentsToListOfLists(List<Student> registeredStudents) {

        List<List<String>> result = new ArrayList<>();

        for (Student student : registeredStudents) {
            List<String> studentData = new ArrayList<>();
            studentData.add(String.valueOf(student.getStudentId())); // Convert ID to String
            studentData.add(student.getFirstName());
            studentData.add(student.getLastName());
            // Add more properties as needed

            result.add(studentData);
        }

        return result;
    }
}


