import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, map } from 'rxjs';
import { Artista } from '../models/artista.model';
import { Album } from '../models/album.model';
import { Cancion } from '../models/cancion.model';
import { Playlist } from '../models/playlist.model';

@Injectable({
  providedIn: 'root'
})
export class EstadisticasService {
  private artistasUrl = 'http://localhost:3000/artistas';
  private albumesUrl = 'http://localhost:3000/albumes';
  private cancionesUrl = 'http://localhost:3000/canciones';
  private playlistsUrl = 'http://localhost:3000/playlists';

  constructor(private http: HttpClient) { }

  // 61. Obtener estadísticas generales
  getEstadisticasGenerales(): Observable<any> {
    return forkJoin({
      artistas: this.http.get<Artista[]>(this.artistasUrl),
      albumes: this.http.get<Album[]>(this.albumesUrl),
      canciones: this.http.get<Cancion[]>(this.cancionesUrl),
      playlists: this.http.get<Playlist[]>(this.playlistsUrl)
    }).pipe(
      map(data => ({
        totalArtistas: data.artistas.length,
        totalAlbumes: data.albumes.length,
        totalCanciones: data.canciones.length,
        totalPlaylists: data.playlists.length,
        promedioCancionesPorAlbum: data.canciones.length / data.albumes.length,
        promedioCancionesPorPlaylist: data.canciones.reduce((acc, cancion) => {
          const playlistsConCancion = data.playlists.filter(p => p.cancionIds.includes(cancion.id)).length;
          return acc + playlistsConCancion;
        }, 0) / data.canciones.length
      }))
    );
  }

  // 62. Estadísticas por género
  getEstadisticasPorGenero(): Observable<Record<string, number>> {
    return this.http.get<Cancion[]>(this.cancionesUrl).pipe(
      map(canciones => {
        const generos: Record<string, number> = {};
        canciones.forEach(cancion => {
          generos[cancion.genero] = (generos[cancion.genero] || 0) + 1;
        });
        return generos;
      })
    );
  }

  // 63. Duración total de todas las canciones
  getDuracionTotal(): Observable<number> {
    return this.http.get<Cancion[]>(this.cancionesUrl).pipe(
      map(canciones => canciones.reduce((total, cancion) => total + cancion.duracion, 0))
    );
  }

  // 64. Artista con más álbumes
  getArtistaConMasAlbumes(): Observable<any> {
    return forkJoin({
      artistas: this.http.get<Artista[]>(this.artistasUrl),
      albumes: this.http.get<Album[]>(this.albumesUrl)
    }).pipe(
      map(data => {
        const albumesPorArtista: Record<string, number> = {};
        data.albumes.forEach(album => {
          albumesPorArtista[album.artistaId] = (albumesPorArtista[album.artistaId] || 0) + 1;
        });
        const maxAlbumes = Math.max(...Object.values(albumesPorArtista) as number[]);
        const artistaId = Object.keys(albumesPorArtista).find(id => albumesPorArtista[id] === maxAlbumes);
        const artista = data.artistas.find(a => a.id === artistaId);
        return { artista, numeroAlbumes: maxAlbumes };
      })
    );
  }

  // 65. Canción más larga
  getCancionMasLarga(): Observable<Cancion> {
    return this.http.get<Cancion[]>(this.cancionesUrl).pipe(
      map(canciones => canciones.reduce((max, cancion) => cancion.duracion > max.duracion ? cancion : max))
    );
  }

  // 66. Canción más corta
  getCancionMasCorta(): Observable<Cancion> {
    return this.http.get<Cancion[]>(this.cancionesUrl).pipe(
      map(canciones => canciones.reduce((min, cancion) => cancion.duracion < min.duracion ? cancion : min))
    );
  }

  // 67. Promedio de duración de canciones por género
  getPromedioDuracionPorGenero(): Observable<Record<string, number>> {
    return this.http.get<Cancion[]>(this.cancionesUrl).pipe(
      map(canciones => {
        const porGenero: Record<string, { total: number; count: number }> = {};
        canciones.forEach(cancion => {
          if (!porGenero[cancion.genero]) {
            porGenero[cancion.genero] = { total: 0, count: 0 };
          }
          porGenero[cancion.genero].total += cancion.duracion;
          porGenero[cancion.genero].count += 1;
        });
        const promedios: Record<string, number> = {};
        Object.keys(porGenero).forEach(genero => {
          promedios[genero] = porGenero[genero].total / porGenero[genero].count;
        });
        return promedios;
      })
    );
  }

  // 68. Distribución de artistas por país
  getDistribucionArtistasPorPais(): Observable<Record<string, number>> {
    return this.http.get<Artista[]>(this.artistasUrl).pipe(
      map(artistas => {
        const porPais: Record<string, number> = {};
        artistas.forEach(artista => {
          porPais[artista.pais] = (porPais[artista.pais] || 0) + 1;
        });
        return porPais;
      })
    );
  }

  // 69. Álbumes por década
  getAlbumesPorDecada(): Observable<Record<string, number>> {
    return this.http.get<Album[]>(this.albumesUrl).pipe(
      map(albumes => {
        const porDecada: Record<string, number> = {};
        albumes.forEach(album => {
          const decada = Math.floor(album.anio / 10) * 10;
          const key = decada.toString();
          porDecada[key] = (porDecada[key] || 0) + 1;
        });
        return porDecada;
      })
    );
  }

  // 70. Playlist más popular (con más canciones)
  getPlaylistMasPopular(): Observable<Playlist> {
    return this.http.get<Playlist[]>(this.playlistsUrl).pipe(
      map(playlists => playlists.reduce((max, playlist) =>
        playlist.cancionIds.length > max.cancionIds.length ? playlist : max
      ))
    );
  }
}