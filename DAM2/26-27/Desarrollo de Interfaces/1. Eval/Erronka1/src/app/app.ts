import { Component, computed, inject, signal } from '@angular/core';
import { SaioBilaketa } from './saioak/saio-bilaketa/saio-bilaketa';
import { SaioZerrenda } from './saioak/saio-zerrenda/saio-zerrenda';
import { SaioXehetasuna } from './saioak/saio-xehetasuna/saio-xehetasuna';
import { Saioa } from './modelo/Saioa';
import { Iragazketa } from './zerbitzuak/iragazketa';

@Component({
  imports: [SaioBilaketa, SaioZerrenda, SaioXehetasuna],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  private iragazketa = inject(Iragazketa);
  protected aukeratua = signal<Saioa | null>(null);

  /** Xehetasun panela kontrolatzen duen tab-aren indizea */
  protected aukeratutakoTabId = computed(() => {
    const saioa = this.aukeratua();
    if (!saioa) return null;
    const indizea = this.iragazketa.iragazitakoak().indexOf(saioa);
    return indizea >= 0 ? `saio-tab-${indizea}` : null;
  });
}
