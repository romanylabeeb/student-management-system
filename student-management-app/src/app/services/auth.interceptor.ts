import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse,
  HttpResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import { catchError, map } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const authToken = localStorage.getItem('authToken');
    if (!authToken && !this.isLoginRequest(req.url)) {
      this.router.navigate(['/login']); // redirect to login page
      return throwError(() => new Error('Token is missing'));
    }

    let clonedReq = req;

    // add the Authorization header if token is present
    if (authToken) {
      clonedReq = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${authToken}`),
      });
    }
    return next.handle(clonedReq).pipe(
      map((event: HttpEvent<any>) => {
        // show snackbar
        if (
          event instanceof HttpResponse &&
          this.isWriteRequest(req) &&
          !this.isLoginRequest(req.url)
        ) {
          this.snackBar.open('Action completed successfully', 'Close', {
            duration: 3000,
            panelClass: ['success-snackbar'],
          });
        }
        return event;
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 || error.status === 403) {
          this.authService.logout();
          this.router.navigate(['/login']);
          this.snackBar.open('Token is expired', 'Close', {
            duration: 3000,
            panelClass: ['error-snackbar'],
          });
        } else if (this.isWriteRequest(req) && !this.isLoginRequest(req.url)) {
          this.snackBar.open(
            'An error occurred during the operation',
            'Close',
            {
              duration: 3000,
              panelClass: ['error-snackbar'],
            }
          );
        }
        return throwError(() => error);
      })
    );
  }

  private isWriteRequest(req: HttpRequest<any>): boolean {
    return (
      req.method === 'POST' || req.method === 'PUT' || req.method === 'DELETE'
    );
  }

  private isLoginRequest(url: string): boolean {
    return url.includes('/login');
  }
}
