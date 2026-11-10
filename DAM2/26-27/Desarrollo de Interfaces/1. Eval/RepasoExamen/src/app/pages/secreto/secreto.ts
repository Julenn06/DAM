import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-secreto',
  imports: [FormsModule, RouterLink],
  templateUrl: './secreto.html',
  styleUrl: './secreto.css',
})
export class SecretoComponent {
  protected guess: number | null = null;
  protected secret = this.random();
  protected readonly attempts = signal(0);
  protected readonly message = signal('');
  protected readonly won = signal(false);
  protected readonly finished = signal(false);
  protected readonly best = signal(0);
  private random() {
    return Math.floor(Math.random() * 100) + 1;
  }
  protected tryGuess() {
    if (this.guess === null || this.guess < 1 || this.guess > 100) {
      this.message.set('Escribe un número entre 1 y 100.');
      return;
    }
    const count = this.attempts() + 1;
    this.attempts.set(count);
    if (this.guess === this.secret) {
      this.won.set(true);
      this.finished.set(true);
      this.message.set(`¡Correcto! Era el ${this.secret}.`);
      if (!this.best() || count < this.best()) this.best.set(count);
    } else if (count >= 7) {
      this.finished.set(true);
      this.message.set(`Se acabaron los intentos. Era el ${this.secret}.`);
    } else
      this.message.set(this.guess < this.secret ? 'El secreto es mayor.' : 'El secreto es menor.');
  }
  protected newGame() {
    this.secret = this.random();
    this.guess = null;
    this.attempts.set(0);
    this.message.set('Nueva partida: ¿cuál será el secreto?');
    this.won.set(false);
    this.finished.set(false);
  }
}
