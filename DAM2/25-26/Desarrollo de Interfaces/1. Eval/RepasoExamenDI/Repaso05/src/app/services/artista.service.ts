import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Artista } from '../models/artista.model';

@Injectable({
  providedIn: 'root'
})
export class ArtistaService {
  private apiUrl = 'http://localhost:3000/artistas';

  constructor(private http: HttpClient) { }

  // 1. Obtener todos los artistas
  getArtistas(): Observable<Artista[]> {
    return this.http.get<Artista[]>(this.apiUrl);
  }

  // 2. Obtener artista por ID
  getArtistaById(id: string): Observable<Artista> {
    return this.http.get<Artista>(`${this.apiUrl}/${id}`);
  }

  // 3. Crear artista
  createArtista(artista: Omit<Artista, 'id'>): Observable<Artista> {
    return this.http.post<Artista>(this.apiUrl, artista);
  }

  // 4. Actualizar artista
  updateArtista(id: string, artista: Partial<Artista>): Observable<Artista> {
    return this.http.patch<Artista>(`${this.apiUrl}/${id}`, artista);
  }

  // 5. Eliminar artista
  deleteArtista(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // 6. Filtrar artistas por género
  getArtistasByGenero(genero: string): Observable<Artista[]> {
    const params = new HttpParams().set('genero', genero);
    return this.http.get<Artista[]>(this.apiUrl, { params });
  }

  // 7. Filtrar artistas por país
  getArtistasByPais(pais: string): Observable<Artista[]> {
    const params = new HttpParams().set('pais', pais);
    return this.http.get<Artista[]>(this.apiUrl, { params });
  }

  // 8. Buscar artistas por nombre (contiene)
  searchArtistasByNombre(nombre: string): Observable<Artista[]> {
    const params = new HttpParams().set('nombre_like', nombre);
    return this.http.get<Artista[]>(this.apiUrl, { params });
  }

  // 9. Obtener artistas activos
  getArtistasActivos(): Observable<Artista[]> {
    const params = new HttpParams().set('activo', 'true');
    return this.http.get<Artista[]>(this.apiUrl, { params });
  }

  // 10. Obtener artistas inactivos
  getArtistasInactivos(): Observable<Artista[]> {
    const params = new HttpParams().set('activo', 'false');
    return this.http.get<Artista[]>(this.apiUrl, { params });
  }

  // 11. Ordenar artistas por nombre ascendente
  getArtistasOrdenadosPorNombreAsc(): Observable<Artista[]> {
    const params = new HttpParams().set('_sort', 'nombre').set('_order', 'asc');
    return this.http.get<Artista[]>(this.apiUrl, { params });
  }

  // 12. Ordenar artistas por nombre descendente
  getArtistasOrdenadosPorNombreDesc(): Observable<Artista[]> {
    const params = new HttpParams().set('_sort', 'nombre').set('_order', 'desc');
    return this.http.get<Artista[]>(this.apiUrl, { params });
  }

  // 13. Ordenar artistas por año de formación
  getArtistasOrdenadosPorAnio(): Observable<Artista[]> {
    const params = new HttpParams().set('_sort', 'anioFormacion').set('_order', 'asc');
    return this.http.get<Artista[]>(this.apiUrl, { params });
  }

  // 14. Contar artistas
  countArtistas(): Observable<number> {
    return this.http.get<Artista[]>(this.apiUrl).pipe(
      // Nota: json-server no soporta count directamente, pero podemos usar length
    ) as any;
  }

  // 15. Obtener artistas con paginación
  getArtistasPaginados(page: number, limit: number): Observable<Artista[]> {
    const params = new HttpParams()
      .set('_page', page.toString())
      .set('_limit', limit.toString());
    return this.http.get<Artista[]>(this.apiUrl, { params });
  }
}