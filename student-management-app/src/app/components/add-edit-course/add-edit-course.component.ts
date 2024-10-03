import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { MessageInfo } from '../../models/message';
import { Course } from '../../models/course';
import { CourseService } from '../../services/course.service';
@Component({
  selector: 'app-add-edit-course',
  templateUrl: './add-edit-course.component.html',
  styleUrl: './add-edit-course.component.css',
})
export class AddEditCourseComponent {
  @Input() course: Course = {};
  @Input() isEditMode: boolean = false;
  @Output() courseEventAction = new EventEmitter<MessageInfo>();

  constructor(private courseService: CourseService, private router: Router) {}

  ngOnInit(): void {
    if (this.isEditMode && !this.course) {
      this.course = {
        courseName: '',
        description: '',
      };
    }
  }

  async onSubmit() {
    if (this.isEditMode) {
      try {
        const response = await this.courseService.updateCourse(this.course);

        console.log('Course updated successfully', response);
        this.courseEventAction.emit({
          message: 'Course updated successfully',
          type: 'Success',
        });
      } catch (error) {
        console.error('Error updating course', error);
        this.courseEventAction.emit({
          message: 'Error updating course',
          type: 'Error',
        });
      }
    } else {
      try {
        const response = await this.courseService.addCourse(this.course);

        console.log('Course added successfully', response);
        this.courseEventAction.emit({
          message: 'Course added successfully',
          type: 'Success',
        });
      } catch (error) {
        console.error('Error adding course', error);
        this.courseEventAction.emit({
          message: 'Error adding course',
          type: 'Error',
        });
      }
    }
  }

  onCancel() {
    this.courseEventAction.emit({
      message: 'Cancel operation',
      type: 'Cancel',
    });
  }
}
