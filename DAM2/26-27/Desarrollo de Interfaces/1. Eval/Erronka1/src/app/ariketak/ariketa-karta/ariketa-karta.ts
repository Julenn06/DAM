import { Component, computed, input } from '@angular/core';
import { Ariketa } from '../../modelo/Ariketa';
import { ariketaIraupena } from '../../util/iraupena';
import { DenboraPipe } from '../../pipes/denbora-pipe';

@Component({
  imports: [DenboraPipe],
  selector: 'app-ariketa-karta',
  styleUrl: './ariketa-karta.css',
  templateUrl: './ariketa-karta.html',
})
export class AriketaKarta {
  ariketa = input.required<Ariketa>();

  iraupena = computed(() => ariketaIraupena(this.ariketa()));
  argazkia = computed(() => {
    let serieak = this.ariketa().serieak;
    return serieak.at(0)?.argazkia || 'irudirik-ez.svg';
  });
}
