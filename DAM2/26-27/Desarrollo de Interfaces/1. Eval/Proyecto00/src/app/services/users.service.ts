import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Person } from '../models/person.model';

@Injectable({
  providedIn: 'root'
})
export class PersonService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:3000/users';
  private sessionKey = 'currentUser';

  login(credentials: Person): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl).pipe(
      map(usuarios => usuarios.filter(u =>
        String(u.usuario) === String(credentials.name) &&
        String(u.contra) === String(credentials.password)
      )),
      tap(usuariosEncontrados => {
        if (usuariosEncontrados.length > 0) {
          localStorage.setItem(this.sessionKey, JSON.stringify(usuariosEncontrados[0]));
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.sessionKey);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem(this.sessionKey);
  }
}
