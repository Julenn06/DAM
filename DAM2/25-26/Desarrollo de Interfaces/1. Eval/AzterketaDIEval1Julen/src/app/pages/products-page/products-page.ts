import { ChangeDetectorRef, Component } from '@angular/core';
import { Product } from '../product/Product';
import { ProductService } from '../product-service/product-service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-products-page',
  imports: [RouterLink],
  templateUrl: './products-page.html',
  styleUrl: './products-page.css',
})
export class ProductsPage {
[x: string]: any;

  produktuGuztiak: Product[] = []

constructor(private cdr: ChangeDetectorRef, private remote: ProductService, private router: Router) {}

  ngOnInit(): void {
    this.produktuakIkusi()
  }

  produktuakIkusi() {
    this.remote.getAllproducts().subscribe({
      next: (hs) => {
        this.produktuGuztiak = hs;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Error produktuak kargatzean', err);
      }
    });
  }

  deleteProduct(id: string): void {
    this.remote.deleteproduct(id).subscribe(() => {
      this.produktuakIkusi();
    });
  }

  sortuProduktua() {
    this.router.navigate(['/create']);
  }
}