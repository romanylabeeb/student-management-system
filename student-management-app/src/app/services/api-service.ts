import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  async get<T>(endpoint: string, params?: HttpParams): Promise<T> {
    return firstValueFrom(
      this.http.get<T>(`${this.baseUrl}/${endpoint}`, { params })
    );
  }

  async post<T>(
    endpoint: string,
    body: any,
    options?: { headers?: HttpHeaders }
  ): Promise<T> {
    return firstValueFrom(
      this.http.post<T>(`${this.baseUrl}/${endpoint}`, body, options)
    );
  }

  async put<T>(endpoint: string, body: any): Promise<T> {
    return firstValueFrom(
      this.http.put<T>(`${this.baseUrl}/${endpoint}`, body)
    );
  }

  async delete<T>(endpoint: string): Promise<T> {
    return firstValueFrom(this.http.delete<T>(`${this.baseUrl}/${endpoint}`));
  }

  downloadPdf(endpoint: string): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${endpoint}`, {
      responseType: 'blob',
    });
  }
}
