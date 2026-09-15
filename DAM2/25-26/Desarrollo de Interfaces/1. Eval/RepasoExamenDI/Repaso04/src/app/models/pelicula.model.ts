export interface Pelicula {
  id: number | string;
  titulo: string;
  director: string;
  genero: string;
  ano: number;
  duracion: number;
  rating: number;
  sinopsis: string;
  actores: string[];
  poster: string;
  presupuesto: number;
  recaudacion: number;
}