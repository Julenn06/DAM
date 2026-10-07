import { Liburua } from './../../interface/liburua';
import { Component, inject } from '@angular/core';
import { Liburutegia } from '../../services/liburutegia';

@Component({
  selector: 'app-zerrenda',
  templateUrl: './zerrenda.html',
})
export class Zerrenda {
  private liburutegia = inject(Liburutegia);
  liburuak = this.liburutegia.liburuak;
}
