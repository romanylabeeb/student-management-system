import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Course } from '../../models/course';
import { Router } from '@angular/router';

@Component({
  selector: 'app-course-table',
  templateUrl: './course-table.component.html',
  styleUrl: './course-table.component.css',
})
export class CourseTableComponent {
  @Input() public courses: Course[] = [];

  @Input() showEdit: boolean = false;
  @Input() showDisenroll: boolean = false;

  @Output() onEdit = new EventEmitter<Course>();
  @Output() onDisenroll = new EventEmitter<Course>();

  columnNames: string[] = [];
  constructor(private router: Router) {}
  ngOnInit(): void {
    this.columnNames = ['courseName', 'description', 'actions'];
  }

  viewCourseDetails(courseId: number) {
    this.router.navigate(['/courses', courseId]);
  }
  onEditCourse(course: Course) {
    this.onEdit?.emit(course);
  }
  onDisenrollCourse(course: Course) {
    this.onDisenroll?.emit(course);
  }
}
