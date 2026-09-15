import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { PeliculasService } from '../../services/peliculas.service';
import { Pelicula } from '../../models/pelicula.model';

@Component({
  selector: 'app-estadisticas',
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule
  ],
  templateUrl: './estadisticas.html',
  styleUrl: './estadisticas.css',
})
export class Estadisticas implements OnInit {
  estadisticas: any = {};
  topPeliculas: any[] = [];

  constructor(private peliculasService: PeliculasService) {}

  ngOnInit() {
    this.loadEstadisticas();
    this.loadTopPeliculas();
  }

  loadEstadisticas() {
    this.peliculasService.getEstadisticas().subscribe((stats: any) => {
      this.estadisticas = stats;
    });
  }

  loadTopPeliculas() {
    this.peliculasService.getPeliculasMasRecaudadoras().subscribe((peliculas: Pelicula[]) => {
      this.topPeliculas = peliculas;
    });
  }
}
