import { Component, EventEmitter, Input, Output } from '@angular/core';
import { StudentService } from '../../services/student.service';
import { Course } from '../../models/course';
import { CourseEnrollService } from '../../services/course-enroll.service';
import { MessageInfo } from '../../models/message';

@Component({
  selector: 'app-register-course',
  templateUrl: './register-course.component.html',
  styleUrls: ['./register-course.component.css'],
})
export class RegisterCourseComponent {
  @Input() studentId: any = '';
  @Output() onEnroll = new EventEmitter<MessageInfo>();
  @Output() onCancelEnroll = new EventEmitter<void>();

  courseName: string = '';
  unregisteredCourses: Course[] = [];
  searched = false;

  constructor(
    private studentService: StudentService,
    private courseEnrollService: CourseEnrollService
  ) {}

  
  async searchCourses() {
    if (!this.studentId) {
      return;
    }
    this.unregisteredCourses = await this.studentService.getUnregisteredCourses(
      this.studentId,
      this.courseName
    );
    this.searched = true;
  }
  cancel() {
    this.onCancelEnroll.emit();
  }
  
  async registerCourse(courseId: any) {
    await this.courseEnrollService.enrollCourse(courseId, this.studentId);
    this.onEnroll.emit({
      message: 'Successfully registered for the course',
      type: 'Success',
    });
  }
}
