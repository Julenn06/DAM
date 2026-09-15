import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { FormsModule } from '@angular/forms';
import { PeliculasService } from '../../services/peliculas.service';
import { Pelicula } from '../../models/pelicula.model';

@Component({
  selector: 'app-pelicula-detail',
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    MatIconModule,
    MatChipsModule,
    FormsModule
  ],
  templateUrl: './pelicula-detail.html',
  styleUrl: './pelicula-detail.css',
})
export class PeliculaDetail implements OnInit {
  pelicula: Pelicula | null = null;
  isEditing = false;
  generos = ['Acción', 'Comedia', 'Drama', 'Terror', 'Ciencia Ficción', 'Fantasía', 'Romance', 'Documental'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private peliculasService: PeliculasService
  ) {}

  ngOnInit() {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.loadPelicula(id);
  }

  loadPelicula(id: number | string) {
    this.peliculasService.getPeliculaById(id).subscribe((pelicula: Pelicula) => {
      this.pelicula = { ...pelicula };
    });
  }

  toggleEdit() {
    this.isEditing = !this.isEditing;
  }

  saveChanges() {
    if (this.pelicula) {
      this.peliculasService.updatePelicula(this.pelicula.id, this.pelicula).subscribe(() => {
        this.isEditing = false;
        alert('Película actualizada correctamente');
      });
    }
  }

  cancelEdit() {
    const id = this.pelicula!.id;
    this.loadPelicula(id);
    this.isEditing = false;
  }

  deletePelicula() {
    if (confirm('¿Estás seguro de que quieres eliminar esta película?')) {
      this.peliculasService.deletePelicula(this.pelicula!.id).subscribe(() => {
        this.router.navigate(['/peliculas']);
      });
    }
  }

  goBack() {
    this.router.navigate(['/peliculas']);
  }
}
