import {
  Routes
} from '@angular/router';
import {LoginComponent} from './pages/login/login';
import {Person} from './pages/person/person';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'person', component: Person },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
