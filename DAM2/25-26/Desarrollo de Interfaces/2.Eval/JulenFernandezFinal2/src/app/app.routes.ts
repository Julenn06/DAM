import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { Libros } from './pages/libros/libros';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'list', component: Libros, canActivate: [authGuard]},
  { path: '**', redirectTo: '/login' }
];
