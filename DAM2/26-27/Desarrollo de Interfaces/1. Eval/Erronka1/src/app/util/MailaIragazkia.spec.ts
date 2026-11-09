import { describe, expect, it } from 'vitest';
import { MailaIragazkia, mailaIzena } from './MailaIragazkia';

describe('mailaIzena', () => {
  it('1 edo gutxiagokoak Hasiberria da', () => {
    expect(mailaIzena(0)).toBe(MailaIragazkia.Hasiberria);
    expect(mailaIzena(1)).toBe(MailaIragazkia.Hasiberria);
  });

  it('2 eta 3 artekoak Ertaina da', () => {
    expect(mailaIzena(2)).toBe(MailaIragazkia.Ertaina);
    expect(mailaIzena(3)).toBe(MailaIragazkia.Ertaina);
  });

  it('4 edo gehiagokoak Aurreratua da', () => {
    expect(mailaIzena(4)).toBe(MailaIragazkia.Aurreratua);
    expect(mailaIzena(10)).toBe(MailaIragazkia.Aurreratua);
  });
});

describe('MailaIragazkia enum-a', () => {
  it('balioak izen berdinak dira', () => {
    expect(MailaIragazkia.Denak).toBe('Denak');
    expect(MailaIragazkia.Hasiberria).toBe('Hasiberria');
    expect(MailaIragazkia.Ertaina).toBe('Ertaina');
    expect(MailaIragazkia.Aurreratua).toBe('Aurreratua');
  });
});
