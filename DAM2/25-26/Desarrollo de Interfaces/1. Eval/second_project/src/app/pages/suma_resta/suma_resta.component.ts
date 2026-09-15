import { Component } from '@angular/core';

@Component({
  selector: 'app-suma-resta',
  imports: [],
  templateUrl: './suma_resta.component.html',
  styleUrl: './suma_resta.component.css'
})
export class SumaRestaComponent {

  ngOnInit() {
      if (!localStorage.getItem('loggedInUser')) {
    window.location.href = '/login';
  }
}

  contador: number = 0;

  sumar() {
    this.contador++;
  }

  restar() {
    this.contador--;
  }
}