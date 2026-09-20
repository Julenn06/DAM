import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Usuario } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:3000/usuarios';
  private loggedInUserKey = 'loggedInUser';

  constructor(private http: HttpClient) { }

  login(credentials: Pick<Usuario, 'nombre_usuario' | 'clave'>): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUrl}?nombre_usuario=${credentials.nombre_usuario}`).pipe(
      map(users => users.filter(user => String(user.clave) === String(credentials.clave))),
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