import { Course } from './course';

export interface Student {
  studentId?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  registeredCourses?: Course[];
}
