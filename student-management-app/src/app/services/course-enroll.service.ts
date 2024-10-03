import { Injectable } from '@angular/core';
import { ApiService } from './api-service';

@Injectable({
  providedIn: 'root',
})
export class CourseEnrollService {
  private enrollsUrl = 'api/courses';

  constructor(private apiService: ApiService) {}

  async enrollCourse(courseId: string, studentId: string) {
    return this.apiService.post(
      `${this.enrollsUrl}/${courseId}/students/${studentId}`,
      {}
    );
  }
  async disenrollCourse(courseId: string, studentId: string) {
    return this.apiService.delete(
      `${this.enrollsUrl}/${courseId}/students/${studentId}`
    );
  }
}
