import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FirebaseService } from '../../services/firebase.service';
import { Usuario } from '../../models/usuario';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  private firebaseService = inject(FirebaseService);
  
  usuarios: Usuario[] = [];
  nuevoUsuario: Usuario = { id: '', nombre: '', email: '' };

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.firebaseService.obtenerUsuarios().subscribe({
      next: (data) => {
        this.usuarios = data;
      },
      error: (error) => {
        console.error('Errorea erabiltzaileak kargatzean:', error);
      }
    });
  }

  crearUsuario(): void {
    if (this.nuevoUsuario.nombre && this.nuevoUsuario.email) {
      this.firebaseService.crearUsuario(this.nuevoUsuario).subscribe({
        next: () => {
          this.nuevoUsuario = { id: '', nombre: '', email: '' };
          this.cargarUsuarios();
        },
        error: (error) => {
          console.error('Errorea erabiltzailea sortzean:', error);
        }
      });
    }
  }

  actualizarUsuario(usuario: Usuario): void {
    this.firebaseService.actualizarUsuario(usuario).subscribe({
      next: () => {
        console.log('Erabiltzailea eguneratuta');
      },
      error: (error) => {
        console.error('Errorea erabiltzailea eguneratzean:', error);
      }
    });
  }

  eliminarUsuario(id: string): void {
    this.firebaseService.eliminarUsuario(id).subscribe({
      next: () => {
        this.cargarUsuarios();
      },
      error: (error) => {
        console.error('Errorea erabiltzailea ezabatzean:', error);
      }
    });
  }
}
