import { Component } from '@angular/core';
import { Student } from '../../models/student';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentService } from '../../services/student.service';
import { Course } from '../../models/course';
import { MessageInfo } from '../../models/message';
import { CourseEnrollService } from '../../services/course-enroll.service';

@Component({
  selector: 'app-student-details',
  templateUrl: './student-details.component.html',
  styleUrl: './student-details.component.css',
})
export class StudentDetailsComponent {
  student: Student | null = null;
  studentId: string | null = '';
  showEnroll = false;
  courses: Course[] = [];
  constructor(
    private route: ActivatedRoute,
    private studentService: StudentService,
    private courseEnrollService: CourseEnrollService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.studentId = this.route.snapshot.paramMap.get('id');
    this.loadStudentDetails(this.studentId); 
  }
  onShowEnroll() {
    this.showEnroll = true;
  }
  async onEnrollAction(event: MessageInfo) {
    this.showEnroll = false;
    await this.loadStudentDetails(this.studentId);
  }
  async loadStudentDetails(id: string | null) {
    if (id) {
      try {
        this.student = await this.studentService.getStudentById(id);
        this.courses = this.student.registeredCourses || [];
      } catch (error) {
        console.error('Error fetching student details', error);
      }
    }
  }
  hideEnroll() {
    this.showEnroll = false;
  }
  async onDisenrollCourseAction(course: Course) {
    try {
      await this.courseEnrollService.disenrollCourse(
        course!.courseId!,
        this.studentId!
      );
      await this.loadStudentDetails(this.studentId);
    } catch (error) {}
  }
  public download() {
    const fileName = `${this.student?.firstName}_${this.student?.lastName}_details`;
    this.studentService.downloadPdf(this.studentId, fileName);
  }
  goBack() {
    this.router.navigate(['/groups']);
  }
}
