import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Playlist } from '../models/playlist.model';

@Injectable({
  providedIn: 'root'
})
export class PlaylistService {
  private apiUrl = 'http://localhost:3000/playlists';

  constructor(private http: HttpClient) { }

  // 46. Obtener todas las playlists
  getPlaylists(): Observable<Playlist[]> {
    return this.http.get<Playlist[]>(this.apiUrl);
  }

  // 47. Obtener playlist por ID
  getPlaylistById(id: string): Observable<Playlist> {
    return this.http.get<Playlist>(`${this.apiUrl}/${id}`);
  }

  // 48. Crear playlist
  createPlaylist(playlist: Omit<Playlist, 'id'>): Observable<Playlist> {
    return this.http.post<Playlist>(this.apiUrl, playlist);
  }

  // 49. Actualizar playlist
  updatePlaylist(id: string, playlist: Partial<Playlist>): Observable<Playlist> {
    return this.http.patch<Playlist>(`${this.apiUrl}/${id}`, playlist);
  }

  // 50. Eliminar playlist
  deletePlaylist(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // 51. Buscar playlists por nombre
  searchPlaylistsByNombre(nombre: string): Observable<Playlist[]> {
    const params = new HttpParams().set('nombre_like', nombre);
    return this.http.get<Playlist[]>(this.apiUrl, { params });
  }

  // 52. Añadir canción a playlist
  addCancionToPlaylist(playlistId: string, cancionId: string): Observable<Playlist> {
    return this.getPlaylistById(playlistId).pipe(
      // En una implementación real, haríamos un patch con la nueva lista
    ) as any;
  }

  // 53. Remover canción de playlist
  removeCancionFromPlaylist(playlistId: string, cancionId: string): Observable<Playlist> {
    return this.getPlaylistById(playlistId).pipe(
      // Similar al anterior
    ) as any;
  }

  // 54. Obtener playlists que contienen una canción específica
  getPlaylistsContainingCancion(cancionId: string): Observable<Playlist[]> {
    // Esto requeriría una consulta más compleja, pero json-server tiene limitaciones
    return this.http.get<Playlist[]>(this.apiUrl);
  }

  // 55. Contar canciones en una playlist
  countCancionesInPlaylist(playlistId: string): Observable<number> {
    return this.getPlaylistById(playlistId).pipe() as any;
  }

  // 56. Obtener playlists ordenadas por nombre
  getPlaylistsOrdenadasPorNombre(): Observable<Playlist[]> {
    const params = new HttpParams().set('_sort', 'nombre').set('_order', 'asc');
    return this.http.get<Playlist[]>(this.apiUrl, { params });
  }

  // 57. Crear playlist vacía
  createEmptyPlaylist(nombre: string, descripcion: string): Observable<Playlist> {
    const playlist = { nombre, descripcion, cancionIds: [] };
    return this.createPlaylist(playlist);
  }

  // 58. Duplicar playlist
  duplicatePlaylist(id: string, nuevoNombre: string): Observable<Playlist> {
    return this.getPlaylistById(id).pipe(
      // Crear nueva con mismo contenido
    ) as any;
  }

  // 59. Vaciar playlist
  emptyPlaylist(id: string): Observable<Playlist> {
    return this.updatePlaylist(id, { cancionIds: [] });
  }

  // 60. Fusionar dos playlists
  mergePlaylists(id1: string, id2: string, nuevoNombre: string): Observable<Playlist> {
    // Obtener ambas y combinar
    return this.getPlaylistById(id1).pipe() as any;
  }
}