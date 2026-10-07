import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Liburutegia } from '../../services/liburutegia';

@Component({
  selector: 'app-gehitu',
  imports: [FormsModule],
  templateUrl: './gehitu.html',
})
export class Gehitu {
  private liburutegia = [];
  izenburua = '';
  egilea = '';
  urtea = new Date().getFullYear();

  gorde() {
    // 1. Izenburua edo egilea hutsik badaude, ez egin ezer
    // 2. Zerbitzuaren gehitu() metodoari deitu
    // 3. Formularioa garbitu

    if (
      this.izenburua == null ||
      this.izenburua == '' ||
      this.egilea == null ||
      this.egilea == ''
    ) {
      alert('gehitu izenburua edo egilea');
      return;
    } else {
      //gorde
      this.izenburua = '';
      this.egilea = '';
      this.urtea = new Date().getFullYear();
    }
  }
}
