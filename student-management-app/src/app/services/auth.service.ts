import { Injectable } from '@angular/core';
import { ApiService } from './api-service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private authEndpoint = 'api/auth/login';
  private tokenKey = 'authToken';

  constructor(private apiService: ApiService) {}

  async login(username: string, password: string): Promise<any> {
    localStorage.clear();
    const loginResponse: any = await this.apiService.post(this.authEndpoint, {
      username,
      password,
    });

    localStorage.setItem(this.tokenKey, loginResponse.token);
  }

  public logout() {
    localStorage.removeItem(this.tokenKey);
  }
}
