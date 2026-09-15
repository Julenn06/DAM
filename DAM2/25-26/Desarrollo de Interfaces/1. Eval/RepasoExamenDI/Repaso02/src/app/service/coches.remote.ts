import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Coche } from '../coche/Coche';
import { map, Observable, switchMap, take } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CochesRemoteService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:3000/Coches';

  getAllCoches(): Observable<Coche[]> {
    return this.http.get<Coche[]>(this.apiUrl);
  }

  getFeaturedCoches(limit = 4): Observable<Coche[]> {
    return this.http.get<Coche[]>(`${this.apiUrl}?_limit=${limit}`);
  }

  getCocheById(id: number): Observable<Coche> {
    return this.http.get<Coche>(`${this.apiUrl}/${String(id)}`);
  }

  getCocheByName(name: string): Observable<Coche> {
    return this.http.get<Coche[]>(`${this.apiUrl}?name=${encodeURIComponent(name)}`).pipe(
      map((Coches) => {
        return Coches[0];
      }),
    );
  }

  getCochesByYearGreaterThan(ano: number): Observable<Coche[]> {
    return this.http
      .get<Coche[]>(this.apiUrl)
      .pipe(map((coches) => coches.filter((c) => Number(c.ano) > ano)));
  }

  createCoche(name: string, tipo?: string, ano?: number): Observable<Coche> {
    return this.getAllCoches().pipe(
      take(1),
      switchMap((Cochees) => {
        const maxId = Cochees.reduce((m, h) => {
          const idNum = typeof h.id === 'string' ? parseInt(h.id, 10) : h.id;
          return Number.isFinite(idNum) ? Math.max(m, idNum) : m;
        }, 0);
        const newId = maxId + 1;

        const body = {
          id: String(newId),
          name: name,
          tipo: tipo || '',
          ano: ano || 0,
        };
        return this.http.post<Coche>(this.apiUrl, body);
      }),
    );
  }

  updateCoche(id: number, changes: Partial<Coche>): Observable<Coche> {
    // PATCH solo envía los cambios, no el ID completo
    return this.http.patch<Coche>(`${this.apiUrl}/${String(id)}`, changes);
  }

  deleteCoche(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${String(id)}`);
  }

  deleteCocheByName(name: string): Observable<void> {
    return this.getCocheByName(name).pipe(
      switchMap((Coche) => {
        return this.deleteCoche(Number(Coche.id));
      }),
    );
  }

  getCochesWithEvenYear(): Observable<Coche[]> {
    return this.http
      .get<Coche[]>(this.apiUrl)
      .pipe(map((coches) => coches.filter((c) => Number(c.ano) % 2 === 0)));
  }

  getCochesByTipo(tipo: string): Observable<Coche[]> {
    return this.http
      .get<Coche[]>(this.apiUrl)
      .pipe(map((coches) => coches.filter((c) => c.tipo.toLowerCase() === tipo.toLowerCase())));
  }
}
