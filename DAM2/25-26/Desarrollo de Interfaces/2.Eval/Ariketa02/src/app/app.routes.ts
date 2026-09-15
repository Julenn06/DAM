import { Routes } from '@angular/router';
import { Productolista } from './productolista/productolista';

export const routes: Routes = [
  { path: 'home', component: Productolista },
  { path: '', redirectTo: '/home', pathMatch: 'full' }
];
