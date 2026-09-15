import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Libro } from '../libro/Libro';
import { map, Observable, switchMap, take } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LibrosRemoteService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:3000/Libros';

  getAllLibros(): Observable<Libro[]> {
    return this.http.get<Libro[]>(this.apiUrl);
  }

  getLibroById(id: number): Observable<Libro> {
    return this.http.get<Libro>(`${this.apiUrl}/${String(id)}`);
  }

  getLibroByTitulo(titulo: string): Observable<Libro> {
    return this.http
      .get<Libro[]>(`${this.apiUrl}?titulo=${encodeURIComponent(titulo)}`)
      .pipe(map((libros) => libros[0]));
  }

  getLibrosByAutor(autor: string): Observable<Libro[]> {
    return this.http
      .get<Libro[]>(this.apiUrl)
      .pipe(
        map((libros) => libros.filter((l) => l.autor.toLowerCase().includes(autor.toLowerCase()))),
      );
  }

  getLibrosByGenero(genero: string): Observable<Libro[]> {
    return this.http
      .get<Libro[]>(this.apiUrl)
      .pipe(map((libros) => libros.filter((l) => l.genero.toLowerCase() === genero.toLowerCase())));
  }

  getLibrosByAnoGreaterThan(ano: number): Observable<Libro[]> {
    return this.http
      .get<Libro[]>(this.apiUrl)
      .pipe(map((libros) => libros.filter((l) => Number(l.ano) > ano)));
  }

  createLibro(titulo: string, autor: string, genero: string, ano: number): Observable<Libro> {
    return this.getAllLibros().pipe(
      take(1),
      switchMap((libros) => {
        const maxId = libros.reduce((m, l) => {
          const idNum = typeof l.id === 'string' ? parseInt(l.id, 10) : l.id;
          return Number.isFinite(idNum) ? Math.max(m, idNum) : m;
        }, 0);
        const newId = maxId + 1;

        const body = {
          id: String(newId),
          titulo: titulo,
          autor: autor,
          genero: genero,
          ano: ano,
        };
        return this.http.post<Libro>(this.apiUrl, body);
      }),
    );
  }

  updateLibro(id: number, changes: Partial<Libro>): Observable<Libro> {
    return this.http.patch<Libro>(`${this.apiUrl}/${String(id)}`, changes);
  }

  deleteLibro(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${String(id)}`);
  }

  deleteLibroByTitulo(titulo: string): Observable<void> {
    return this.getLibroByTitulo(titulo).pipe(
      switchMap((libro) => {
        return this.deleteLibro(Number(libro.id));
      }),
    );
  }

  getLibrosByAnoPar(): Observable<Libro[]> {
    return this.http
      .get<Libro[]>(this.apiUrl)
      .pipe(map((libros) => libros.filter((l) => Number(l.ano) % 2 === 0)));
  }
}
