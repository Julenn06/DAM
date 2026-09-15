import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Auth {
  // Logeo egoera simulatzeko aldagaia
  private isLoggedIn = false; // Hasieran ez dago logeatuta (false).

  constructor() { }
    
  public isAuthenticated(): boolean {
    return this.isLoggedIn; // Aldagaiaren balioa irakurtzeko
  }
  
  public login() {
    this.isLoggedIn = true;
    console.log('Erabiltzailea logeatuta');
  }

  public logout() {
    this.isLoggedIn = false;
    console.log('Erabiltzailea deslogeatuta');
  }
}
