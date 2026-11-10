import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-saltos',
  imports: [FormsModule, RouterLink],
  templateUrl: './saltos.html',
  styleUrl: './saltos.css',
})
export class SaltosComponent {
  protected jumps = 20;
  protected results = signal<number[]>([]);
  protected average = signal(0);
  protected highest = signal(0);
  protected run() {
    const values = Array.from(
      { length: Math.min(100, Math.max(1, this.jumps)) },
      () => Math.floor(Math.random() * 100000) + 1,
    );
    this.results.set(values);
    this.average.set(Math.round(values.reduce((a, b) => a + b, 0) / values.length));
    this.highest.set(Math.max(...values));
  }
  protected height(value: number) {
    return this.highest() ? Math.max(3, (value / this.highest()) * 100) : 3;
  }
}
