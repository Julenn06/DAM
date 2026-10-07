import { Service, signal } from '@angular/core';
import { Liburua } from '../interface/liburua';

@Service()
export class Liburutegia {
  private _liburuak = signal<Liburua[]>([
    { id: 1, izenburua: 'Obabakoak', egilea: 'Bernardo Atxaga', urtea: 1988, mailegatuta: false },
    {
      id: 2,
      izenburua: 'Bilbao-New York-Bilbao',
      egilea: 'Kirmen Uribe',
      urtea: 2008,
      mailegatuta: true,
    },
    { id: 3, izenburua: 'Twist', egilea: 'Harkaitz Cano', urtea: 2011, mailegatuta: false },
  ]);

  liburuak = this._liburuak.asReadonly(); // osagaiek irakurri bai, aldatu ez

  gehitu(izenburua: string, egilea: string, urtea: number) {
    const berria: Liburua = {
      id: Date.now(),
      izenburua,
      egilea,
      urtea,
      mailegatuta: false,
    };
    this._liburuak.update((lista) => [...lista, berria]); // array BERRIA
  }
}
