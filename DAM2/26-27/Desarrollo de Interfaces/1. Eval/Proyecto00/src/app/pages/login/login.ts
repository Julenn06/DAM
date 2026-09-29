import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { PersonService } from '../../services/users.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  private router = inject(Router);
  private personService = inject(PersonService);

  errorMessage: string = '';

  onSubmit(username: string, password: string | number) {
    this.errorMessage = '';

    this.personService.getUsers().subscribe({
      next: (usuarios) => {
        const usuarioValido = usuarios.find(u =>
          String(u.usuario) === String(username) && String(u.contra) === String(password)
        );

        if (usuarioValido) {
          localStorage.setItem('currentUser', JSON.stringify(usuarioValido));
          this.router.navigate(['/home']);
        } else {
          this.errorMessage = 'Usuario o contraseña incorrectos';
        }
      },
      error: () => {
        this.errorMessage = 'Error de conexión con el servidor.';
      }
    });
  }
}
