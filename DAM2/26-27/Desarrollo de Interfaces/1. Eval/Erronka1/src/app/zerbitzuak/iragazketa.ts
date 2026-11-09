import { computed, inject, Service, signal } from '@angular/core';
import { Datuak } from './datuak';
import { MailaIragazkia, mailaIzena } from '../util/MailaIragazkia';
import { Saioa } from '../modelo/Saioa';

@Service()
export class Iragazketa {
  private datuak = inject(Datuak);

  bilaketa = signal('');
  maila = signal(MailaIragazkia.Denak);

  iragazitakoak = computed<Saioa[]>(() => {
    const workoutak = this.datuak.gimnasioa()?.workoutak ?? [];
    const testua = this.bilaketa().trim().toLowerCase();
    const maila = this.maila();

    return workoutak.filter((saioa) => {
      const testuaOndo =
        !testua ||
        saioa.izena.toLowerCase().includes(testua) ||
        saioa.deskribapena.toLowerCase().includes(testua);

      const mailaOndo = maila === MailaIragazkia.Denak || mailaIzena(saioa.maila) === maila;

      return testuaOndo && mailaOndo;
    });
  });
}
