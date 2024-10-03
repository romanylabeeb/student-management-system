import { Injectable } from '@angular/core';
import { ApiService } from './api-service';
import { PdfService } from './pdf-service';
import { Course } from '../models/course';

@Injectable({
  providedIn: 'root',
})
export class CourseService {
  private coursesUrl = 'api/courses';

  constructor(private apiService: ApiService, private pdfService: PdfService) {}

  async getCourses(): Promise<any> {
    return this.apiService.get(this.coursesUrl);
  }
  async getCourseById(id: any): Promise<Course> {
    return this.apiService.get(`${this.coursesUrl}/${id}`);
  }
  public async addCourse(student: Course) {
    return this.apiService.post(this.coursesUrl, student);
  }
  async updateCourse(course: Course) {
    return this.apiService.put(`${this.coursesUrl}/${course.courseId}`, course);
  }

  async getUnregisteredStudents(
    courseId: string,
    studentName: string
  ): Promise<any> {
    return this.apiService.get(
      `${this.coursesUrl}/${courseId}/students/unregistered?key=${studentName}`
    );
  }

  downloadPdf(id: any, fileName: string) {
    this.pdfService.downloadPdf(`${this.coursesUrl}/${id}/pdf`, fileName);
  }
}
