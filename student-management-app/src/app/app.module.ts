import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppComponent } from './components/app-component/app.component';
import { GroupsComponent } from './components/groups/groups.component';
import { MaterialModule } from './material.module';
import { AppRoutingModule } from './app.routes';
import { CoursesComponent } from './components/courses/courses.component';
import { StudentsComponent } from './components/students/students.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './components/login/login.component';
import { FormsModule } from '@angular/forms';
import { ApiService } from './services/api-service';
import { AuthInterceptor } from './services/auth.interceptor';
import { AddEditStudentComponent } from './components/add-edit-student/add-edit-student.component';
import { StudentDetailsComponent } from './components/student-details/student-details.component';
import { StudentTableComponent } from './components/student-table/student-table.component';
import { CourseTableComponent } from './components/course-table/course-table.component';
import { RegisterCourseComponent } from './components/register-course/register-course.component';
import { AddEditCourseComponent } from './components/add-edit-course/add-edit-course.component';
import { CourseDetailsComponent } from './components/course-details/course-details.component';
import { EnrollStudentComponent } from './components/enroll-student/enroll-student.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    GroupsComponent,
    StudentsComponent,
    StudentDetailsComponent,
    AddEditStudentComponent,
    StudentTableComponent,
    CoursesComponent,
    CourseTableComponent,
    AddEditCourseComponent,
    CourseDetailsComponent,
    RegisterCourseComponent,
    EnrollStudentComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MaterialModule,
    FormsModule,
    AppRoutingModule,
  ],
  providers: [
    ApiService,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }, 
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
