import { Component } from '@angular/core';
import { Course } from '../../models/course';
import { Student } from '../../models/student';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseService } from '../../services/course.service';
import { CourseEnrollService } from '../../services/course-enroll.service';
import { MessageInfo } from '../../models/message';

@Component({
  selector: 'app-course-details',
  templateUrl: './course-details.component.html',
  styleUrl: './course-details.component.css',
})
export class CourseDetailsComponent {
  course: Course | null = null;
  courseId: string | null = '';
  showEnroll = false;
  students: Student[] = [];
  constructor(
    private route: ActivatedRoute,
    private courseService: CourseService,
    private courseEnrollService: CourseEnrollService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.courseId = this.route.snapshot.paramMap.get('id');
    this.loadCourseDetails(this.courseId);
  }
  onShowEnroll() {
    this.showEnroll = true;
  }
  async onEnrollAction(event: MessageInfo) {
    this.showEnroll = false;
    await this.loadCourseDetails(this.courseId);
  }
  async loadCourseDetails(id: string | null) {
    if (id) {
      try {
        this.course = await this.courseService.getCourseById(id);
        this.students = this.course.registeredStudents || [];
      } catch (error) {
        console.error('Error fetching course details', error);
      }
    }
  }
  hideEnroll() {
    this.showEnroll = false;
  }
  async onDisenrollCourseAction(student: Student) {
    try {
      await this.courseEnrollService.disenrollCourse(
        this.courseId!,
        student.studentId!
      );
      await this.loadCourseDetails(this.courseId);
    } catch (error) {}
  }
  public download() {
    const fileName = `${this.course?.courseName}_details`;
    this.courseService.downloadPdf(this.courseId, fileName);
  }
  goBack() {
    this.router.navigate(['/groups']);
  }
}
