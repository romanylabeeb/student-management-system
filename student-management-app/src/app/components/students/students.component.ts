import { Component, OnInit } from '@angular/core';
import { StudentService } from '../../services/student.service';
import { Student } from '../../models/student';
import { MessageInfo } from '../../models/message';
import { Router } from '@angular/router';

@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
})
export class StudentsComponent implements OnInit {
  students: Student[] = [];
  selectedStudent: Student = {};
  isEditMode: boolean = false;
  showAddEditForm = false;
  constructor(private studentService: StudentService, private router: Router) {}

  ngOnInit() {
    this.loadStudents();
  }

  async loadStudents() {
    this.students = await this.studentService.getStudents();
  }
  viewStudentDetails(studentId: number) {
    this.router.navigate(['/students', studentId]);
  }
  addNewStudent() {
    this.selectedStudent = {};
    this.isEditMode = false;
    this.showAddEditForm = true;
  }

  editStudent(student: any) {
    this.selectedStudent = { ...student };
    this.isEditMode = true;
    this.showAddEditForm = true;
  }

  handleStudentAdded(event: MessageInfo) {
    console.log('event:', event);
    if (event.type == 'Success') {
      this.loadStudents();
    }
    this.showAddEditForm = false;
    this.selectedStudent = {};
    this.isEditMode = false;
  }

  handleAddEditStudent(event: MessageInfo) {
    if (event.type == 'Success') {
      this.loadStudents();
    }
    this.showAddEditForm = false;
    this.selectedStudent = {};
    this.isEditMode = false;
  }
}
