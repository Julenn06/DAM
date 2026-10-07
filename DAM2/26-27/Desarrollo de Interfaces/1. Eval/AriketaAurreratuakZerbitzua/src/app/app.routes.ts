import { Routes } from '@angular/router';
import { Zerrenda } from './pages/zerrenda/zerrenda';

export const routes: Routes = [
  { path: 'zerrenda', component: Zerrenda },
  { path: '', redirectTo: 'zerrenda', pathMatch: 'full' },
  { path: '**', redirectTo: 'zerrenda' },
];
