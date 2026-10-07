import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Liburutegia } from '../../services/liburutegia';

@Component({
  imports: [FormsModule],
  selector: 'app-laburpena',
  styleUrl: './laburpena.css',
  templateUrl: './laburpena.html',
})
export class Laburpena {
  private liburutegia = inject(Liburutegia);

  // Al usar 'get', Angular lee el valor del servicio automáticamente en cada cambio
  get guztira() {
    return this.liburutegia.zenbatLiburu();
  }

  get mailegatuta() {
    // Nota: He dejado el método que tengas en tu servicio para los mailegatuta
    return this.liburutegia.zenbatLiburu();
  }

  get eskuragarri() {
    // Puedes calcularlo restando directamente los dos anteriores
    return this.guztira - this.mailegatuta;
  }
}
