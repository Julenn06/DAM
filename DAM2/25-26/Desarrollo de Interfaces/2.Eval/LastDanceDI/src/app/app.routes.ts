import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { authGuard } from './guards/auth.guard';
import { PersonComponent } from './pages/person/person';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'person', component: PersonComponent, canActivate: [authGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
