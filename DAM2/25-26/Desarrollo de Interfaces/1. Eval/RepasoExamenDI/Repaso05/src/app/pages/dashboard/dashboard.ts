import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ArtistaService } from '../../services/artista.service';
import { AlbumService } from '../../services/album.service';
import { CancionService } from '../../services/cancion.service';
import { PlaylistService } from '../../services/playlist.service';
import { EstadisticasService } from '../../services/estadisticas.service';
import { Artista } from '../../models/artista.model';
import { Album } from '../../models/album.model';
import { Cancion } from '../../models/cancion.model';
import { Playlist } from '../../models/playlist.model';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  // Datos
  artistas: Artista[] = [];
  albumes: Album[] = [];
  canciones: Cancion[] = [];
  playlists: Playlist[] = [];

  // Formularios
  nuevoArtista: Omit<Artista, 'id'> = { nombre: '', genero: '', pais: '', anioFormacion: 0, activo: true };
  nuevoAlbum: Omit<Album, 'id'> = { titulo: '', artistaId: '', anio: 0, genero: '', duracionTotal: 0 };
  nuevaCancion: Omit<Cancion, 'id'> = { titulo: '', albumId: '', artistaId: '', duracion: 0, genero: '' };
  nuevaPlaylist: Omit<Playlist, 'id'> = { nombre: '', descripcion: '', cancionIds: [] };

  // Filtros y búsquedas
  filtroGenero = '';
  filtroPais = '';
  busquedaArtista = '';
  busquedaAlbum = '';
  busquedaCancion = '';

  // Estadísticas
  estadisticasGenerales: any = {};
  estadisticasPorGenero: Record<string, number> = {};
  duracionTotal = 0;
  artistaMasAlbumes: any = {};
  cancionMasLarga: Cancion | null = null;
  cancionMasCorta: Cancion | null = null;
  promedioDuracionPorGenero: Record<string, number> = {};
  distribucionPorPais: Record<string, number> = {};
  albumesPorDecada: Record<string, number> = {};
  playlistMasPopular: Playlist | null = null;

  constructor(
    private artistaService: ArtistaService,
    private albumService: AlbumService,
    private cancionService: CancionService,
    private playlistService: PlaylistService,
    private estadisticasService: EstadisticasService
  ) {}

  ngOnInit() {
    this.cargarDatos();
    this.cargarEstadisticas();
  }

  cargarDatos() {
    // 1-4. CRUD Artistas
    this.artistaService.getArtistas().subscribe(data => this.artistas = data);
    this.albumService.getAlbumes().subscribe(data => this.albumes = data);
    this.cancionService.getCanciones().subscribe(data => this.canciones = data);
    this.playlistService.getPlaylists().subscribe(data => this.playlists = data);
  }

  cargarEstadisticas() {
    // 61-70. Estadísticas
    this.estadisticasService.getEstadisticasGenerales().subscribe(data => this.estadisticasGenerales = data);
    this.estadisticasService.getEstadisticasPorGenero().subscribe(data => this.estadisticasPorGenero = data);
    this.estadisticasService.getDuracionTotal().subscribe(data => this.duracionTotal = data);
    this.estadisticasService.getArtistaConMasAlbumes().subscribe(data => this.artistaMasAlbumes = data);
    this.estadisticasService.getCancionMasLarga().subscribe(data => this.cancionMasLarga = data);
    this.estadisticasService.getCancionMasCorta().subscribe(data => this.cancionMasCorta = data);
    this.estadisticasService.getPromedioDuracionPorGenero().subscribe(data => this.promedioDuracionPorGenero = data);
    this.estadisticasService.getDistribucionArtistasPorPais().subscribe(data => this.distribucionPorPais = data);
    this.estadisticasService.getAlbumesPorDecada().subscribe(data => this.albumesPorDecada = data);
    this.estadisticasService.getPlaylistMasPopular().subscribe(data => this.playlistMasPopular = data);
  }

  // Métodos CRUD
  crearArtista() {
    this.artistaService.createArtista(this.nuevoArtista).subscribe(() => {
      this.cargarDatos();
      this.nuevoArtista = { nombre: '', genero: '', pais: '', anioFormacion: 0, activo: true };
    });
  }

  eliminarArtista(id: string) {
    this.artistaService.deleteArtista(id).subscribe(() => this.cargarDatos());
  }

  crearAlbum() {
    this.albumService.createAlbum(this.nuevoAlbum).subscribe(() => {
      this.cargarDatos();
      this.nuevoAlbum = { titulo: '', artistaId: '', anio: 0, genero: '', duracionTotal: 0 };
    });
  }

  eliminarAlbum(id: string) {
    this.albumService.deleteAlbum(id).subscribe(() => this.cargarDatos());
  }

  crearCancion() {
    this.cancionService.createCancion(this.nuevaCancion).subscribe(() => {
      this.cargarDatos();
      this.nuevaCancion = { titulo: '', albumId: '', artistaId: '', duracion: 0, genero: '' };
    });
  }

  eliminarCancion(id: string) {
    this.cancionService.deleteCancion(id).subscribe(() => this.cargarDatos());
  }

  crearPlaylist() {
    this.playlistService.createPlaylist(this.nuevaPlaylist).subscribe(() => {
      this.cargarDatos();
      this.nuevaPlaylist = { nombre: '', descripcion: '', cancionIds: [] };
    });
  }

  eliminarPlaylist(id: string) {
    this.playlistService.deletePlaylist(id).subscribe(() => this.cargarDatos());
  }

  // Métodos de filtrado y búsqueda
  getArtistasFiltrados(): Artista[] {
    let filtered = this.artistas;
    if (this.filtroGenero) {
      filtered = filtered.filter(a => a.genero === this.filtroGenero);
    }
    if (this.filtroPais) {
      filtered = filtered.filter(a => a.pais === this.filtroPais);
    }
    if (this.busquedaArtista) {
      filtered = filtered.filter(a => a.nombre.toLowerCase().includes(this.busquedaArtista.toLowerCase()));
    }
    return filtered;
  }

  getAlbumesFiltrados(): Album[] {
    let filtered = this.albumes;
    if (this.busquedaAlbum) {
      filtered = filtered.filter(a => a.titulo.toLowerCase().includes(this.busquedaAlbum.toLowerCase()));
    }
    return filtered;
  }

  getCancionesFiltradas(): Cancion[] {
    let filtered = this.canciones;
    if (this.busquedaCancion) {
      filtered = filtered.filter(c => c.titulo.toLowerCase().includes(this.busquedaCancion.toLowerCase()));
    }
    return filtered;
  }

  // Utilidades
  getArtistaNombre(id: string): string {
    const artista = this.artistas.find(a => a.id === id);
    return artista ? artista.nombre : 'Desconocido';
  }

  getAlbumNombre(id: string): string {
    const album = this.albumes.find(a => a.id === id);
    return album ? album.titulo : 'Desconocido';
  }

  getCancionesDePlaylist(playlist: Playlist): Cancion[] {
    return this.canciones.filter(c => playlist.cancionIds.includes(c.id));
  }
}
