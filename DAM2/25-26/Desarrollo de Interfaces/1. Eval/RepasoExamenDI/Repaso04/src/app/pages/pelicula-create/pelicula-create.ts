import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { PeliculasService } from '../../services/peliculas.service';
import { Pelicula } from '../../models/pelicula.model';

@Component({
  selector: 'app-pelicula-create',
  imports: [
    MatCardModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    MatIconModule,
    FormsModule
  ],
  templateUrl: './pelicula-create.html',
  styleUrl: './pelicula-create.css',
})
export class PeliculaCreate {
  pelicula: Omit<Pelicula, 'id'> = {
    titulo: '',
    director: '',
    actores: [],
    genero: '',
    ano: new Date().getFullYear(),
    duracion: 0,
    rating: 0,
    sinopsis: '',
    poster: '',
    presupuesto: 0,
    recaudacion: 0
  };

  generos = ['Acción', 'Comedia', 'Drama', 'Terror', 'Ciencia Ficción', 'Fantasía', 'Romance', 'Documental'];

  constructor(private peliculasService: PeliculasService, private router: Router) {}

  onSubmit() {
    if (this.isValid()) {
      this.peliculasService.createPelicula(this.pelicula).subscribe((createdPelicula: Pelicula) => {
        this.router.navigate(['/peliculas', createdPelicula.id]);
      });
    } else {
      alert('Por favor, completa todos los campos obligatorios.');
    }
  }

  isValid(): boolean {
    return !!(
      this.pelicula.titulo &&
      this.pelicula.director &&
      this.pelicula.genero &&
      this.pelicula.ano &&
      this.pelicula.duracion > 0 &&
      this.pelicula.rating > 0 &&
      this.pelicula.sinopsis
    );
  }

  onActoresChange(value: string) {
    this.pelicula.actores = value.split(',').map(actor => actor.trim()).filter(actor => actor);
  }

  cancel() {
    this.router.navigate(['/peliculas']);
  }
}


