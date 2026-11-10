import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home';
import { SecretoComponent } from './pages/secreto/secreto';
import { SaltosComponent } from './pages/saltos/saltos';
import { TragaperrasComponent } from './pages/tragaperras/tragaperras';
import { JabalinaComponent } from './pages/jabalina/jabalina';
import { PrimitivaComponent } from './pages/primitiva/primitiva';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'secreto', component: SecretoComponent },
  { path: 'saltos', component: SaltosComponent },
  { path: 'tragaperras', component: TragaperrasComponent },
  { path: 'jabalina', component: JabalinaComponent },
  { path: 'primitiva', component: PrimitivaComponent },
  { path: '**', redirectTo: '' },
];
