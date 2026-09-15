import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  loginError = '';
  loginSuccess = '';

  constructor(private http: HttpClient, private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

    onSubmit(): void {
      const { username, password } = this.loginForm.value;
      // Login local: solo permite admin/admin
      console.log('[DEBUG] Intentando login con:', username, password);
      if (username === 'admin' && password === 'admin') {
        this.loginError = '';
        this.loginSuccess = 'Login correcto';
        localStorage.setItem('loggedInUser', JSON.stringify({ username: 'admin' }));
        console.log('[DEBUG] Login correcto, redirigiendo...');
        window.location.href = '/welcome';
      } else {
        this.loginError = 'Usuario o contraseña incorrectos';
        this.loginSuccess = '';
        console.log('[DEBUG] Usuario o contraseña incorrectos');
      }
    }
}
