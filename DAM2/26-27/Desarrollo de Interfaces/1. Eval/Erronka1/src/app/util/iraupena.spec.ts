import { describe, expect, it } from 'vitest';
import { Ariketa } from '../modelo/Ariketa';
import { Saioa } from '../modelo/Saioa';
import { ariketaIraupena, saioIraupena } from './iraupena';

function ariketaSortu(atsedena: number, serieenIraupenak: number[]): Ariketa {
  return {
    izena: 'Ariketa',
    deskribapena: '',
    atsedena,
    serieak: serieenIraupenak.map((iraupena) => ({
      izena: '',
      argazkia: '',
      errepikapenak: 1,
      iraupena,
    })),
  };
}

function saioaSortu(ariketak: Ariketa[]): Saioa {
  return {
    izena: 'Saioa',
    deskribapena: '',
    maila: 1,
    bideoa: '',
    ariketak,
  };
}

describe('ariketaIraupena', () => {
  it('serie bakoitzaren iraupena eta atsedena batzen ditu', () => {
    const ariketa = ariketaSortu(10, [30, 30]);
    expect(ariketaIraupena(ariketa)).toBe(80);
  });

  it('0 itzultzen du serieik ez badago', () => {
    const ariketa = ariketaSortu(10, []);
    expect(ariketaIraupena(ariketa)).toBe(0);
  });

  it('serie bakarra duen ariketa kalkulatzen du', () => {
    const ariketa = ariketaSortu(45, [60]);
    expect(ariketaIraupena(ariketa)).toBe(105);
  });
});

describe('saioIraupena', () => {
  it('ariketa guztien iraupenen batura itzultzen du', () => {
    const saioa = saioaSortu([ariketaSortu(10, [30, 30]), ariketaSortu(5, [20])]);
    expect(saioIraupena(saioa)).toBe(80 + 25);
  });

  it('0 itzultzen du ariketik ez badago', () => {
    const saioa = saioaSortu([]);
    expect(saioIraupena(saioa)).toBe(0);
  });
});
