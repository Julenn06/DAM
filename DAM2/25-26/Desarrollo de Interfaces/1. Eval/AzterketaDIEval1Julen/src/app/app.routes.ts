import { Routes } from '@angular/router';
import { ProductsPage } from './pages/products-page/products-page';
import { ProductPage } from './pages/product-page/product-page';
import { ProductCreatePage } from './pages/product-create-page/product-create-page';

export const routes: Routes = [
    {path: '', component: ProductsPage},
    {path: 'products/:id', component: ProductPage },
    {path: 'create', component: ProductCreatePage}
];
