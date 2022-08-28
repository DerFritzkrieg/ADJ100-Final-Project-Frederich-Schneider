import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { RegisterPayload } from './register-payload';
import { LoginPayload } from './login-payload';
import { JwtAutResponse } from './jwt-aut-response';
import { LocalStorageService } from 'ngx-webstorage';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private url = 'http://localhost:8080/api/auth/';


  constructor(private httpCLient: HttpClient, private localStorageService: LocalStorageService) { }

  register(registerPayload:RegisterPayload): Observable<any>{
    return this.httpCLient.post(this.url + 'signup', registerPayload);
  }

  login(loginPayload: LoginPayload): Observable<boolean> {
    return this.httpCLient.post<JwtAutResponse>(this.url + 'login', loginPayload).pipe(map(data=>{
      this.localStorageService.store('authenticationToken', data.authenticationToken);
      this.localStorageService.store('username', data.username);
      return true;
    }));
  }

  isAuthenticated(): Boolean{
    return this.localStorageService.retrieve('username') != null;
  }
}
