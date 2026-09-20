import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:3000/users';
  private loggedInUserKey = 'loggedInUser';

  constructor(private http: HttpClient) { }

  login(user: User): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}?usuario=${user.username}&contra=${user.password}`).pipe(
      tap(users => {
        if (users.length > 0) {
          sessionStorage.setItem(this.loggedInUserKey, JSON.stringify(users[0]));
        }
      })
    );
  }

  logout(): void {
    sessionStorage.removeItem(this.loggedInUserKey);
  }

  isLoggedIn(): boolean {
    return !!sessionStorage.getItem(this.loggedInUserKey);
  }
}
