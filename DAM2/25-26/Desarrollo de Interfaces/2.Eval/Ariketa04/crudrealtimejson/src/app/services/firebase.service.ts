import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Usuario } from '../models/usuario';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FirebaseService {
  private http = inject(HttpClient);
  // Cambia esta URL por la de tu proyecto Firebase
  private baseUrl = 'https://bd-prueba-b9487-default-rtdb.firebaseio.com/erabiltzaileak.json';

  crearUsuario(user: Usuario): Observable<any> {
    return this.http.post(`${this.baseUrl}usuarios.json`, {
      nombre: user.nombre,
      email: user.email
    });
  }

  obtenerUsuarios(): Observable<Usuario[]> {
    return this.http.get<{[key: string]: {nombre: string, email: string}}>(`${this.baseUrl}usuarios.json`)
      .pipe(
        map(data => {
          if (!data) return [];
          return Object.keys(data).map(key => ({
            id: key,
            nombre: data[key].nombre,
            email: data[key].email
          }));
        })
      );
  }

  actualizarUsuario(user: Usuario): Observable<any> {
    return this.http.put(`${this.baseUrl}usuarios/${user.id}.json`, {
      nombre: user.nombre,
      email: user.email
    });
  }

  eliminarUsuario(id: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}usuarios/${id}.json`);
  }
}
