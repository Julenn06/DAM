import { Component, inject } from '@angular/core'; // 👈 Asegúrate de importar 'inject'
import { FormsModule } from '@angular/forms';
import { Liburutegia } from '../../services/liburutegia';

@Component({
  selector: 'app-gehitu',
  imports: [FormsModule],
  templateUrl: './gehitu.html',
})
export class Gehitu {
  private liburutegia = inject(Liburutegia);

  izenburua = '';
  egilea = '';
  urtea = new Date().getFullYear();

  gorde() {
    if (!this.izenburua?.trim() || !this.egilea?.trim()) {
      alert('gehitu izenburua edo egilea');
      return;
    }

    this.liburutegia.gehitu(this.izenburua, this.egilea, this.urtea);

    this.izenburua = '';
    this.egilea = '';
    this.urtea = new Date().getFullYear();
  }
}
