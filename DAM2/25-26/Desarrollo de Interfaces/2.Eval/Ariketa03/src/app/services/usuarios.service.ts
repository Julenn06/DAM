import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Erabiltzailea } from '../interfaces/usuario';

@Injectable({
  providedIn: 'root',
})
export class Erabiltzaileak {
  private urlBase = 'https://bd-prueba-b9487-default-rtdb.firebaseio.com/erabiltzaileak';

  constructor(private http: HttpClient) { }

  lortuErabiltzaileak(): Observable<Erabiltzailea[]> {
    return this.http.get<{ [key: string]: Erabiltzailea }>(this.urlBase + '.json')
      .pipe(
        map(res => {
          if (!res) return [];
          return Object.keys(res).map(key => ({ id: key, ...res[key] }));
        })
      );
  }

  gehituErabiltzailea(erabiltzailea: Erabiltzailea) {
    return this.http.post(this.urlBase + '.json', erabiltzailea);
  }

  eguneratuErabiltzailea(erabiltzailea: Erabiltzailea) {
    if (!erabiltzailea.id) throw new Error('Erabiltzaileak id-a eduki behar du');
    return this.http.put(this.urlBase + '/' + erabiltzailea.id + '.json', {
      izena: erabiltzailea.izena,
      emaila: erabiltzailea.emaila
    });
  }

  ezabatuErabiltzailea(id: string) {
    return this.http.delete(this.urlBase + '/' + id + '.json');
  }
}
