import { Component, OnInit } from '@angular/core';
import { CourseService } from '../../services/course.service';
import { Course } from '../../models/course';
import { Router } from '@angular/router';
import { MessageInfo } from '../../models/message';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
})
export class CoursesComponent implements OnInit {
  courses: Course[] = [];
  selectedCourse: Course = {};
  isEditMode: boolean = false;
  showAddEditForm = false;
  constructor(private courseService: CourseService, private router: Router) {}

  ngOnInit() {
    this.loadCourses();
  }

  async loadCourses() {
    this.courses = await this.courseService.getCourses();
  }
  viewCourseDetails(courseId: number) {
    this.router.navigate(['/course', courseId]);
  }
  addNewCourse() {
    this.selectedCourse = {}; 
    this.isEditMode = false; 
    this.showAddEditForm = true;
  }

  editCourse(course: any) {
    this.selectedCourse = { ...course };
    this.isEditMode = true;
    this.showAddEditForm = true;
  }

  handleAddEditCourse(event: MessageInfo) {
    console.log('event:', event);
    if (event.type == 'Success') {
      this.loadCourses(); 
    }
    this.showAddEditForm = false;
    this.selectedCourse = {}; 
    this.isEditMode = false; 
  }
}
