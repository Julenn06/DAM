import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  standalone: true,
  imports: [] // Completamente vacío para evitar errores estáticos del compilador
})
export class LoginComponent {
  private router = inject(Router);
  error = '';

  // Aquí está la función que acepta los 2 argumentos que le envía el HTML
  onSubmit(usernameVal: string, passwordVal: string) {
    if (!usernameVal || !passwordVal) {
      this.error = "Por favor, rellena todos los campos";
      return;
    }

    if (usernameVal === "julen@gmail.com" && passwordVal === "12345") {
        this.router.navigate(['/person']);
    } else {
        this.error = "Credenciales incorrectas";
    }
  }
}
