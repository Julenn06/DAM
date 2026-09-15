import { Routes } from '@angular/router';
import { HomePage } from '../app/pages/home-page/home-page';
import { CreationPage } from '../app/pages/creation-page/creation-page';
import { HouseDetailComponent } from '../app/pages/house-page/house-page';

export const routes: Routes = [
    {path: '', component: HomePage},
    {path: 'home', component: HomePage},
    {path: 'creation', component: CreationPage},
    {path: 'house/:id', component: HouseDetailComponent }
];
