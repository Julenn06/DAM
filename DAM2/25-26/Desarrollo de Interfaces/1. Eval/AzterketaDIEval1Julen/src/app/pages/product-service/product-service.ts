import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Product } from '../product/Product';
import { map, Observable, switchMap, take } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProductService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:3000/products';
  snapshot: any;
//1
  getAllproducts(): Observable < Product[] > {
    return this.http.get < Product[] > (this.apiUrl);
  }

//2
  getproductById(id: string): Observable < Product > {
    return this.http.get < Product > (`${this.apiUrl}/${(id)}`);
  }

//3
  createproduct(name: string, price ?: number, stock ?: number): Observable < Product > {
    return this.getAllproducts().pipe(
      take(1),
      switchMap((productes) => {
        const maxId = productes.reduce((m, h) => {
          const idNum = typeof h.id === 'string' ? parseInt(h.id, 10) : h.id;
          return Number.isFinite(idNum) ? Math.max(m, idNum) : m;
        }, 0);
        const newId = maxId + 1;

        const body = {
          id: String(newId),
          name: name,
          price: price || 0,
          stock: stock || 0
        };
        return this.http.post < Product > (this.apiUrl, body);
      })
    );
  }
//4
  updateproduct(id: number, changes: Partial < Product > ): Observable < Product > {
    return this.http.patch < Product > (`${this.apiUrl}/${String(id)}`, changes);
  }
//5
  deleteproduct(id: string): Observable < void > {
    return this.http.delete < void > (`${this.apiUrl}/${(id)}`);
  }
}
