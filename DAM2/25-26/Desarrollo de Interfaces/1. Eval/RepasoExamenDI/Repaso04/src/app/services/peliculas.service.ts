import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Pelicula } from '../models/pelicula.model';
import { map, Observable, switchMap, take } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PeliculasService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:3000/Peliculas';

  getAllPeliculas(): Observable<Pelicula[]> {
    return this.http.get<Pelicula[]>(this.apiUrl);
  }

  getPeliculaById(id: number | string): Observable<Pelicula> {
    return this.http.get<Pelicula>(`${this.apiUrl}/${String(id)}`);
  }

  createPelicula(pelicula: Omit<Pelicula, 'id'>): Observable<Pelicula> {
    return this.getAllPeliculas().pipe(
      take(1),
      switchMap((peliculas) => {
        const maxId = peliculas.reduce((m, p) => {
          const idNum = typeof p.id === 'string' ? parseInt(p.id, 10) : p.id;
          return Number.isFinite(idNum) ? Math.max(m, idNum) : m;
        }, 0);
        const newId = maxId + 1;

        const body = {
          id: String(newId),
          ...pelicula
        };
        return this.http.post<Pelicula>(this.apiUrl, body);
      })
    );
  }

  updatePelicula(id: number | string, changes: Partial<Pelicula>): Observable<Pelicula> {
    return this.http.patch<Pelicula>(`${this.apiUrl}/${String(id)}`, changes);
  }

  deletePelicula(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${String(id)}`);
  }

  getEstadisticas(): Observable<any> {
    return this.getAllPeliculas().pipe(
      map(peliculas => {
        const totalPeliculas = peliculas.length;
        const promedioRating = peliculas.reduce((sum, p) => sum + p.rating, 0) / totalPeliculas;
        const generos = [...new Set(peliculas.map(p => p.genero))];
        return {
          totalPeliculas,
          promedioRating: promedioRating.toFixed(2),
          totalGeneros: generos.length
        };
      })
    );
  }

  getPeliculasMasRecaudadoras(): Observable<Pelicula[]> {
    // Asumiendo que no hay campo de recaudación, ordenar por rating
    return this.getAllPeliculas().pipe(
      map(peliculas => peliculas.sort((a, b) => b.rating - a.rating).slice(0, 5))
    );
  }
}