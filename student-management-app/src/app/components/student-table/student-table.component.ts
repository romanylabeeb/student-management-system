import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Student } from '../../models/student';
import { Router } from '@angular/router';

@Component({
  selector: 'app-student-table',
  templateUrl: './student-table.component.html',
  styleUrl: './student-table.component.css',
})
export class StudentTableComponent implements OnInit {
  @Input() public students: Student[] = [];
  @Input() showEdit: boolean = false;
  @Input() showDisenroll: boolean = false;

  @Output() onEdit = new EventEmitter<Student>();
  @Output() onDisenroll = new EventEmitter<Student>();

  columnNames: string[] = ['firstName', 'lastName', 'email', 'actions'];
  constructor(private router: Router) {}
  ngOnInit(): void {
   
  }

  viewStudentDetails(studentId: number) {
    this.router.navigate(['/students', studentId]);
  }
  onEditStudent(student: Student) {
    this.onEdit.emit(student);
  }
  onDisenrollStudent(student: Student) {
    this.onDisenroll?.emit(student);
  }
}
