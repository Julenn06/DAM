import { Component } from '@angular/core';

@Component({
  imports: [],
  selector: 'app-dado',
  styleUrl: './dado.css',
  templateUrl: './dado.html',
})
export class Dado {
  numeroDado(dado1: HTMLInputElement, dado2: HTMLInputElement, dado3: HTMLInputElement) {
    dado1.value = this.generarNumero().toString();
    dado2.value = this.generarNumero().toString();
    dado3.value = this.generarNumero().toString();
  }

  generarNumero(): Number {
    return Math.floor(Math.random() * 6) + 1;
  }
}
