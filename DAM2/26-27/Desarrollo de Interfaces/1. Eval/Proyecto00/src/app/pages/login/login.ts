import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  imports: [],
  selector: 'app-login',
  styleUrl: './login.css',
  templateUrl: './login.html',
})
export class Login {

  private router = inject(Router)

  onSubmit(username: string, age: number) {
    if (!username.trim() || age == 0)
      alert("completa los datos")
    else if (age < 18)
      alert("tienes que ser mayor de edad")
    else
      this.router.navigate(['/home'])
  }
}
