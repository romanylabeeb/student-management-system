import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CourseService } from '../../services/course.service';
import { CourseEnrollService } from '../../services/course-enroll.service';
import { MessageInfo } from '../../models/message';
import { Student } from '../../models/student';

@Component({
  selector: 'app-enroll-student',
  templateUrl: './enroll-student.component.html',
  styleUrl: './enroll-student.component.css',
})
export class EnrollStudentComponent {
  @Input() courseId: any = '';
  @Output() onEnroll = new EventEmitter<MessageInfo>();
  @Output() onCancelEnroll = new EventEmitter<void>();

  studentName: string = '';
  unregisteredStudents: Student[] = [];
  searched = false;

  constructor(
    private courseService: CourseService,
    private courseEnrollService: CourseEnrollService
  ) {}

  
  async searchStudents() {
    if (!this.courseId) {
      return;
    }
    this.unregisteredStudents =
      await this.courseService.getUnregisteredStudents(
        this.courseId,
        this.studentName
      );
    this.searched = true;
  }
  cancel() {
    this.onCancelEnroll.emit();
  }
  
  async registerStudent(studentId: any) {
    await this.courseEnrollService.enrollCourse(this.courseId, studentId);
    this.onEnroll.emit({
      message: 'Successfully registered for the course',
      type: 'Success',
    });
  }
}
