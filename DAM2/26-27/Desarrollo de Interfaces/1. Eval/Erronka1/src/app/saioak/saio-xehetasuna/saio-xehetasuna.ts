import { Component, computed, input } from '@angular/core';
import { Saioa } from '../../modelo/Saioa';
import { saioIraupena } from '../../util/iraupena';
import { AriketaZerrenda } from '../../ariketak/ariketa-zerrenda/ariketa-zerrenda';
import { DenboraPipe } from '../../pipes/denbora-pipe';

@Component({
  imports: [AriketaZerrenda, DenboraPipe],
  selector: 'app-saio-xehetasuna',
  styleUrl: './saio-xehetasuna.css',
  templateUrl: './saio-xehetasuna.html',
})
export class SaioXehetasuna {
  saioa = input<Saioa | null>(null);
  tabId = input<string | null>(null);
  iraupena = computed(() => {
    const saioa = this.saioa();
    return saioa ? saioIraupena(saioa) : 0;
  });
}
