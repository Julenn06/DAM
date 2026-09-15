import { Component } from '@angular/core';

@Component({
  selector: 'app-welcome',
  standalone: true,
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.css'
})
export class WelcomeComponent {

    ngOnInit(): void {
      if (!localStorage.getItem('loggedInUser')) {
        window.location.href = '/login';
      }
    }

  suma_resta() {
      window.location.href = "/suma_resta";
  }

  navigateToArrays() {
    window.location.href = "/arrays";
  }
}