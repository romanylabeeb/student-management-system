import { Injectable } from '@angular/core';
import { ApiService } from './api-service';
import { Student } from '../models/student';
import { PdfService } from './pdf-service';

@Injectable({
  providedIn: 'root',
})
export class StudentService {
  private studentsUrl = 'api/students';

  constructor(private apiService: ApiService, private pdfService: PdfService) {}

  async getStudents(): Promise<any> {
    return this.apiService.get(this.studentsUrl);
  }
  async getStudentById(id: any): Promise<Student> {
    return this.apiService.get(`${this.studentsUrl}/${id}`);
  }
  public async addStudent(student: Student) {
    return this.apiService.post(this.studentsUrl, student);
  }
  async updateStudent(student: Student) {
    return this.apiService.put(
      `${this.studentsUrl}/${student.studentId}`,
      student
    );
  }

  async getUnregisteredCourses(
    studentId: string,
    courseName: string
  ): Promise<any> {
    return this.apiService.get(
      `${this.studentsUrl}/${studentId}/courses/unregistered?key=${courseName}`
    );
  }

  downloadPdf(id: any, fileName: string) {
    this.pdfService.downloadPdf(`${this.studentsUrl}/${id}/pdf`, fileName);
  }
}
