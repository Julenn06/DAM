import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { FormsModule } from '@angular/forms';
import { PeliculasService } from '../../services/peliculas.service';
import { Pelicula } from '../../models/pelicula.model';

@Component({
  selector: 'app-pelicula-list',
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    MatIconModule,
    MatPaginatorModule,
    FormsModule
  ],
  templateUrl: './pelicula-list.html',
  styleUrl: './pelicula-list.css',
})
export class PeliculaList implements OnInit {
  peliculas: Pelicula[] = [];
  filteredPeliculas: Pelicula[] = [];
  searchQuery: string = '';
  selectedGenero: string = '';
  selectedAno: number | null = null;
  generos: string[] = [];
  anos: number[] = [];
  pageSize = 6;
  pageIndex = 0;
  totalPeliculas = 0;

  constructor(private peliculasService: PeliculasService, private router: Router) {}

  ngOnInit() {
    this.loadPeliculas();
  }

  loadPeliculas() {
    this.peliculasService.getAllPeliculas().subscribe((peliculas: Pelicula[]) => {
      this.peliculas = peliculas;
      this.filteredPeliculas = peliculas;
      this.totalPeliculas = peliculas.length;
      this.generos = [...new Set(peliculas.map((p: Pelicula) => p.genero))];
      this.anos = [...new Set(peliculas.map((p: Pelicula) => p.ano))].sort((a: number, b: number) => b - a);
      this.applyFilters();
    });
  }

  applyFilters() {
    let filtered = this.peliculas;

    if (this.searchQuery) {
      filtered = filtered.filter(p =>
        p.titulo.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        p.director.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    }

    if (this.selectedGenero) {
      filtered = filtered.filter(p => p.genero === this.selectedGenero);
    }

    if (this.selectedAno) {
      filtered = filtered.filter(p => p.ano === this.selectedAno);
    }

    this.filteredPeliculas = filtered;
    this.totalPeliculas = filtered.length;
    this.pageIndex = 0; // Reset to first page
  }

  onSearchChange() {
    this.applyFilters();
  }

  onGeneroChange() {
    this.applyFilters();
  }

  onAnoChange() {
    this.applyFilters();
  }

  getPaginatedPeliculas() {
    const startIndex = this.pageIndex * this.pageSize;
    return this.filteredPeliculas.slice(startIndex, startIndex + this.pageSize);
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  viewPelicula(id: number | string) {
    this.router.navigate(['/peliculas', id]);
  }

  deletePelicula(id: number | string) {
    if (confirm('¿Estás seguro de que quieres eliminar esta película?')) {
      this.peliculasService.deletePelicula(id).subscribe(() => {
        this.loadPeliculas();
      });
    }
  }
}
