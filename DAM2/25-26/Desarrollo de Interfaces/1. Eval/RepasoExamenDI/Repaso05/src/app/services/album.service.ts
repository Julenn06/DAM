import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Album } from '../models/album.model';

@Injectable({
  providedIn: 'root'
})
export class AlbumService {
  private apiUrl = 'http://localhost:3000/albumes';

  constructor(private http: HttpClient) { }

  // 16. Obtener todos los álbumes
  getAlbumes(): Observable<Album[]> {
    return this.http.get<Album[]>(this.apiUrl);
  }

  // 17. Obtener álbum por ID
  getAlbumById(id: string): Observable<Album> {
    return this.http.get<Album>(`${this.apiUrl}/${id}`);
  }

  // 18. Crear álbum
  createAlbum(album: Omit<Album, 'id'>): Observable<Album> {
    return this.http.post<Album>(this.apiUrl, album);
  }

  // 19. Actualizar álbum
  updateAlbum(id: string, album: Partial<Album>): Observable<Album> {
    return this.http.patch<Album>(`${this.apiUrl}/${id}`, album);
  }

  // 20. Eliminar álbum
  deleteAlbum(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // 21. Filtrar álbumes por artista
  getAlbumesByArtista(artistaId: string): Observable<Album[]> {
    const params = new HttpParams().set('artistaId', artistaId);
    return this.http.get<Album[]>(this.apiUrl, { params });
  }

  // 22. Filtrar álbumes por género
  getAlbumesByGenero(genero: string): Observable<Album[]> {
    const params = new HttpParams().set('genero', genero);
    return this.http.get<Album[]>(this.apiUrl, { params });
  }

  // 23. Filtrar álbumes por año
  getAlbumesByAnio(anio: number): Observable<Album[]> {
    const params = new HttpParams().set('anio', anio.toString());
    return this.http.get<Album[]>(this.apiUrl, { params });
  }

  // 24. Buscar álbumes por título
  searchAlbumesByTitulo(titulo: string): Observable<Album[]> {
    const params = new HttpParams().set('titulo_like', titulo);
    return this.http.get<Album[]>(this.apiUrl, { params });
  }

  // 25. Ordenar álbumes por título
  getAlbumesOrdenadosPorTitulo(): Observable<Album[]> {
    const params = new HttpParams().set('_sort', 'titulo').set('_order', 'asc');
    return this.http.get<Album[]>(this.apiUrl, { params });
  }

  // 26. Ordenar álbumes por año
  getAlbumesOrdenadosPorAnio(): Observable<Album[]> {
    const params = new HttpParams().set('_sort', 'anio').set('_order', 'desc');
    return this.http.get<Album[]>(this.apiUrl, { params });
  }

  // 27. Obtener álbumes con duración mayor a X minutos
  getAlbumesConDuracionMayor(duracion: number): Observable<Album[]> {
    const params = new HttpParams().set('duracionTotal_gte', duracion.toString());
    return this.http.get<Album[]>(this.apiUrl, { params });
  }

  // 28. Contar álbumes por artista
  countAlbumesByArtista(artistaId: string): Observable<number> {
    const params = new HttpParams().set('artistaId', artistaId);
    return this.http.get<Album[]>(this.apiUrl, { params }).pipe() as any;
  }

  // 29. Obtener álbumes recientes (últimos 10 años)
  getAlbumesRecientes(): Observable<Album[]> {
    const anioActual = new Date().getFullYear();
    const params = new HttpParams().set('anio_gte', (anioActual - 10).toString());
    return this.http.get<Album[]>(this.apiUrl, { params });
  }

  // 30. Obtener álbumes antiguos (antes de 1980)
  getAlbumesAntiguos(): Observable<Album[]> {
    const params = new HttpParams().set('anio_lte', '1980');
    return this.http.get<Album[]>(this.apiUrl, { params });
  }
}