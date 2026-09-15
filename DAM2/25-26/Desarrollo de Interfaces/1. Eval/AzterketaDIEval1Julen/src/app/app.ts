import { Component, signal } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('AzterketaDIEval1Julen');

  constructor(private router: Router) {}

  produktuLista() {
    this.router.navigate(['/']);
  }

  sortuProduktua() {
    this.router.navigate(['/create']);
  }
}
