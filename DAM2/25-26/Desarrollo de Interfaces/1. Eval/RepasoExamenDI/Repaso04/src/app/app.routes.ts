import { Routes } from '@angular/router';
import { PeliculaList } from './pages/pelicula-list/pelicula-list';
import { PeliculaDetail } from './pages/pelicula-detail/pelicula-detail';
import { PeliculaCreate } from './pages/pelicula-create/pelicula-create';
import { Estadisticas } from './pages/estadisticas/estadisticas';

export const routes: Routes = [
  { path: '', redirectTo: '/peliculas', pathMatch: 'full' },
  { path: 'peliculas', component: PeliculaList },
  { path: 'peliculas/create', component: PeliculaCreate },
  { path: 'peliculas/:id', component: PeliculaDetail },
  { path: 'estadisticas', component: Estadisticas }
];
