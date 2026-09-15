import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Erabiltzailea } from './interfaces/usuario';
import { Erabiltzaileak } from './services/usuarios.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class AppComponent implements OnInit {
  erabiltzaileak: Erabiltzailea[] = [];
  erabiltzaileaBerria: Erabiltzailea = { izena: '', emaila: '' };

  constructor(private zerbitzua: Erabiltzaileak) {}

  ngOnInit() {
    this.kargatuErabiltzaileak();
  }

  kargatuErabiltzaileak() {
    this.zerbitzua.lortuErabiltzaileak().subscribe(datuak => {
      this.erabiltzaileak = datuak;
    });
  }

  gehitu() {
    this.zerbitzua.gehituErabiltzailea(this.erabiltzaileaBerria).subscribe(() => {
      this.kargatuErabiltzaileak();
      this.erabiltzaileaBerria = { izena: '', emaila: '' };
    });
  }

  eguneratu(e: Erabiltzailea) {
    this.zerbitzua.eguneratuErabiltzailea(e).subscribe(() => this.kargatuErabiltzaileak());
  }

  ezabatu(e: Erabiltzailea) {
    if (e.id) {
      this.zerbitzua.ezabatuErabiltzailea(e.id).subscribe(() => this.kargatuErabiltzaileak());
    }
  }
}
