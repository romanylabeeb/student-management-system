import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { StudentService } from '../../services/student.service';
import { Student } from '../../models/student';
import { MessageInfo } from '../../models/message';
@Component({
  selector: 'app-add-edit-student',
  templateUrl: './add-edit-student.component.html',
  styleUrl: './add-edit-student.component.css',
})
export class AddEditStudentComponent {
  @Input() student: Student = {};
  @Input() isEditMode: boolean = false;
  @Output() studentEventAction = new EventEmitter<MessageInfo>();

  constructor(private studentService: StudentService, private router: Router) {}

  ngOnInit(): void {
    // init student object if in edit mode
    if (this.isEditMode && !this.student) {
      this.student = {
        firstName: '',
        lastName: '',
        email: '',
      };
    }
  }

  async onSubmit() {
    if (this.isEditMode) {
      try {
        const response = await this.studentService.updateStudent(this.student);

        console.log('Student updated successfully', response);
        this.studentEventAction.emit({
          message: 'Student updated successfully',
          type: 'Success',
        });
      } catch (error) {
        console.error('Error updating student', error);
        this.studentEventAction.emit({
          message: 'Error updating student',
          type: 'Error',
        });
      }
    } else {
      try {
        const response = await this.studentService.addStudent(this.student);

        console.log('Student added successfully', response);
        this.studentEventAction.emit({
          message: 'Student added successfully',
          type: 'Success',
        });
      } catch (error) {
        console.error('Error adding student', error);
        this.studentEventAction.emit({
          message: 'Error adding student',
          type: 'Error',
        }); 
      }
    }
  }

  onCancel() {
    this.studentEventAction.emit({
      message: 'Cancel operation',
      type: 'Cancel',
    }); 
  }
}
