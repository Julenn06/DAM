import { Component, inject, signal } from '@angular/core';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import { Auth } from './services/auth';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('protegerrutas');
   
  authService: Auth = inject(Auth);
  router: Router = inject(Router);
   
  login() {
    this.authService.login();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/home']);
  }
}
