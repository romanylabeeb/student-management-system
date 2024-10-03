import { Student } from './student';

export interface Course {
  courseId?: string;
  courseName?: string;
  description?: string;
  registeredStudents?: Student[];
}
