import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { Product } from '../product/Product';
import { ProductService } from '../product-service/product-service';

@Component({
  selector: 'app-product-page',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './product-page.html',
  styleUrls: ['./product-page.css']
})
export class ProductPage implements OnInit {
  produktua: Product[] = []

  id: string = '';

  constructor (private remote: ProductService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.id = String(this.route.snapshot.paramMap.get('id'));
    this.remote.getproductById(this.id).subscribe({
      next: (produktua) => this.produktua = [produktua]
    });
  }
}