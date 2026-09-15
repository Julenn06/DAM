import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('first-project');

  ngOnInit() {
    if (window.location.href.includes('login')) {
      const button = document.getElementById("buttonlogout");
      if (button) {
        button.style.display = "none";
      }
    }
  }

  logout() {
    localStorage.removeItem('loggedInUser');
    window.location.href = 'login';
  }
}

