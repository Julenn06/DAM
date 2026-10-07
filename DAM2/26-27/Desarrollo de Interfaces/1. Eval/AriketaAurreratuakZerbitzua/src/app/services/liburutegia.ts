import { Injectable, signal } from '@angular/core';
import { Liburua } from '../interface/liburua';

@Injectable({
  providedIn: 'root',
})
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

  liburuak = this._liburuak.asReadonly();

  gehitu(izenburua: string, egilea: string, urtea: number) {
    const berria: Liburua = {
      id: Date.now(),
      izenburua: String(izenburua),
      egilea: String(egilea),
      urtea: Number(urtea),
      mailegatuta: false,
    };
    this._liburuak.update((lista) => [...lista, berria]);
    console.log('Array actualizado:', this._liburuak());
  }

  mailegatuAldatu(id: number) {
    this._liburuak.update((lista) =>
      lista.map((libro) =>
        libro.id === id ? { ...libro, mailegatuta: !libro.mailegatuta } : libro,
      ),
    );
  }

  ezabatu(id: number) {
    this._liburuak.update((lista) => lista.filter((libro) => libro.id !== id));
  }

  zenbatLiburu() {
    return this._liburuak().length;
  }
}
