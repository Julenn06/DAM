import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { PersonService } from '../../services/users.service';
import { Person } from '../../models/person.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private router = inject(Router);
  private personService = inject(PersonService);

  errorMessage: string = '';

  onSubmit(username: string, password: string | number) {
    this.errorMessage = '';

    const credentials: Person = { name: username, password: password };

    this.personService.login(credentials).subscribe({
      next: (usuariosEncontrados) => {
        if (usuariosEncontrados.length > 0) {
          this.router.navigate(['/home']);
        } else {
          this.errorMessage = 'Usuario o contraseña incorrectos';
        }
      },
      error: () => {
        this.errorMessage = 'Error de conexión con el servidor.';
      },
    });
  }
}
