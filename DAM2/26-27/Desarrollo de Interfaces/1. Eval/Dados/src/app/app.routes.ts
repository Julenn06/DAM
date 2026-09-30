import { Routes } from '@angular/router';
import { Dado } from './pages/dado/dado';

export const routes: Routes = [
  { path: 'dado', component: Dado },
  { path: '', redirectTo: 'dado', pathMatch: 'full' },
  { path: '**', redirectTo: 'dado' },
];
