import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProductService } from '../product-service/product-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-create-page',
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './product-create-page.html',
  styleUrl: './product-create-page.css',
})
export class ProductCreatePage {
name: string = '';
price: number = 0;
stock: number = 0;

constructor (private remote: ProductService, private router: Router) {}

produktuaSortu() {
      if (!this.name.trim()) {
      alert("Gutxienez Izena bete Bete");
      return;
    }

    if (this.price < 0 || this.stock < 0) {
      alert("Balio Positiboak Jarri");
      this.price = 0;
      this.stock = 0;
      return;
    }

    this.remote.createproduct(this.name.trim(), this.price, this.stock).subscribe({
      next: () => {
        alert("Produktua " + this.name + "Ondo Sortuta");
        this.router.navigate(['/']);
      },
      error: (err) => console.error('Errorea Produktua Sortzen', err)
    });
}

datuakKendu(){
this.name = '';
this.price = 0;
this.stock = 0;
}

ezabatu() {
this.router.navigate(['/']);
}
}
