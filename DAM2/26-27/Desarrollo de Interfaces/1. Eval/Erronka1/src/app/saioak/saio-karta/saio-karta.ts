import { Component, computed, input, output } from '@angular/core';
import { LowerCasePipe, UpperCasePipe } from '@angular/common';
import { Saioa } from '../../modelo/Saioa';
import { saioIraupena } from '../../util/iraupena';
import { MailaPipe } from '../../pipes/maila-pipe';
import { DenboraPipe } from '../../pipes/denbora-pipe';

@Component({
  imports: [MailaPipe, LowerCasePipe, UpperCasePipe, DenboraPipe],
  selector: 'app-saio-karta',
  styleUrl: './saio-karta.css',
  templateUrl: './saio-karta.html',
  host: {
    role: 'presentation',
  },
})
export class SaioKarta {
  saioa = input.required<Saioa>();
  aukeratuta = input<boolean>(false);
  tabId = input.required<string>();
  tabIndex = input<number>(-1);
  saioaAukeratu = output<void>();
  iraupena = computed(() => saioIraupena(this.saioa()));
}
