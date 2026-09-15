import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cancion } from '../models/cancion.model';

@Injectable({
  providedIn: 'root'
})
export class CancionService {
  private apiUrl = 'http://localhost:3000/canciones';

  constructor(private http: HttpClient) { }

  // 31. Obtener todas las canciones
  getCanciones(): Observable<Cancion[]> {
    return this.http.get<Cancion[]>(this.apiUrl);
  }

  // 32. Obtener canción por ID
  getCancionById(id: string): Observable<Cancion> {
    return this.http.get<Cancion>(`${this.apiUrl}/${id}`);
  }

  // 33. Crear canción
  createCancion(cancion: Omit<Cancion, 'id'>): Observable<Cancion> {
    return this.http.post<Cancion>(this.apiUrl, cancion);
  }

  // 34. Actualizar canción
  updateCancion(id: string, cancion: Partial<Cancion>): Observable<Cancion> {
    return this.http.patch<Cancion>(`${this.apiUrl}/${id}`, cancion);
  }

  // 35. Eliminar canción
  deleteCancion(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // 36. Filtrar canciones por álbum
  getCancionesByAlbum(albumId: string): Observable<Cancion[]> {
    const params = new HttpParams().set('albumId', albumId);
    return this.http.get<Cancion[]>(this.apiUrl, { params });
  }

  // 37. Filtrar canciones por artista
  getCancionesByArtista(artistaId: string): Observable<Cancion[]> {
    const params = new HttpParams().set('artistaId', artistaId);
    return this.http.get<Cancion[]>(this.apiUrl, { params });
  }

  // 38. Filtrar canciones por género
  getCancionesByGenero(genero: string): Observable<Cancion[]> {
    const params = new HttpParams().set('genero', genero);
    return this.http.get<Cancion[]>(this.apiUrl, { params });
  }

  // 39. Buscar canciones por título
  searchCancionesByTitulo(titulo: string): Observable<Cancion[]> {
    const params = new HttpParams().set('titulo_like', titulo);
    return this.http.get<Cancion[]>(this.apiUrl, { params });
  }

  // 40. Ordenar canciones por duración
  getCancionesOrdenadasPorDuracion(): Observable<Cancion[]> {
    const params = new HttpParams().set('_sort', 'duracion').set('_order', 'asc');
    return this.http.get<Cancion[]>(this.apiUrl, { params });
  }

  // 41. Obtener canciones con duración mayor a X minutos
  getCancionesConDuracionMayor(duracion: number): Observable<Cancion[]> {
    const params = new HttpParams().set('duracion_gte', duracion.toString());
    return this.http.get<Cancion[]>(this.apiUrl, { params });
  }

  // 42. Obtener canciones cortas (menos de 3 minutos)
  getCancionesCortas(): Observable<Cancion[]> {
    const params = new HttpParams().set('duracion_lte', '3');
    return this.http.get<Cancion[]>(this.apiUrl, { params });
  }

  // 43. Contar canciones por álbum
  countCancionesByAlbum(albumId: string): Observable<number> {
    const params = new HttpParams().set('albumId', albumId);
    return this.http.get<Cancion[]>(this.apiUrl, { params }).pipe() as any;
  }

  // 44. Obtener canciones aleatorias (simulado con límite)
  getCancionesAleatorias(limit: number = 5): Observable<Cancion[]> {
    const params = new HttpParams().set('_limit', limit.toString());
    return this.http.get<Cancion[]>(this.apiUrl, { params });
  }

  // 45. Obtener canciones con paginación
  getCancionesPaginadas(page: number, limit: number): Observable<Cancion[]> {
    const params = new HttpParams()
      .set('_page', page.toString())
      .set('_limit', limit.toString());
    return this.http.get<Cancion[]>(this.apiUrl, { params });
  }
}