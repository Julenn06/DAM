import { Component, inject } from '@angular/core';
import { PersonService } from '../../services/users.service';

@Component({
  imports: [],
  selector: 'app-home',
  styleUrl: './home.css',
  templateUrl: './home.html',
})
export class Home {
  private personService = inject(PersonService);

  onSubmit() {
    this.personService.logout();
  }

  sumarNumeros(n1: number, n2: number, r1: HTMLInputElement) {
    const resultado = n1 + n2;
    r1.value = resultado.toString();
  }
}
