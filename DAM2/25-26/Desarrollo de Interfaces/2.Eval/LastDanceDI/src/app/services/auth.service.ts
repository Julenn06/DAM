import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:3000/usuarios';
  private loggedInUserKey = 'loggedInUser';

  constructor(private http: HttpClient) { }

  login(credentials: Pick<User, 'usuario' | 'contra'>): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}?usuario=${credentials.usuario}`).pipe(
      map(users => users.filter(user => String(user.contra) === String(credentials.contra))),
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
