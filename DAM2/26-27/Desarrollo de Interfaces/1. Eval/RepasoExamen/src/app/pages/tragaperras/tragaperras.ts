import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-tragaperras',
  imports: [RouterLink],
  templateUrl: './tragaperras.html',
  styleUrl: './tragaperras.css',
})
export class TragaperrasComponent {
  protected symbols = signal(['?', '?', '?']);
  protected credits = signal(100);
  protected result = signal('Pulsa jugar para probar suerte.');
  protected won = signal(false);
  private icons = ['♣', '♦', '♥', '♠', '7'];
  protected spin() {
    if (this.credits() < 10) return;
    this.credits.update((value) => value - 10);
    const symbols = Array.from(
      { length: 3 },
      () => this.icons[Math.floor(Math.random() * this.icons.length)],
    );
    this.symbols.set(symbols);
    const max = Math.max(
      ...this.icons.map((icon) => symbols.filter((symbol) => symbol === icon).length),
    );
    const prize = max === 3 ? (symbols[0] === '7' ? 200 : 50) : max === 2 ? 15 : 0;
    this.credits.update((value) => value + prize);
    this.won.set(prize > 0);
    this.result.set(prize ? `¡Premio de ${prize} créditos!` : 'Sin premio esta vez.');
  }
  protected reset() {
    this.credits.set(100);
    this.result.set('Créditos recargados.');
    this.won.set(false);
  }
}
