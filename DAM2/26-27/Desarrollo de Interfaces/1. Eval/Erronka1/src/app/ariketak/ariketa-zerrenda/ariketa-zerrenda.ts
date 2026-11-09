import { Component, inject, input } from '@angular/core';
import { Ariketa } from '../../modelo/Ariketa';
import { AriketaKarta } from '../ariketa-karta/ariketa-karta';
import { Datuak } from '../../zerbitzuak/datuak';

@Component({
  imports: [AriketaKarta],
  selector: 'app-ariketa-zerrenda',
  styleUrl: './ariketa-zerrenda.css',
  templateUrl: './ariketa-zerrenda.html',
})
export class AriketaZerrenda {
  ariketak = input.required<Ariketa[]>();
  kargatzen = inject(Datuak).kargatzen;
}
