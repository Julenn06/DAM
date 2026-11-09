import { describe, expect, it } from 'vitest';
import { MailaIragazkia } from '../util/MailaIragazkia';
import { MailaPipe } from './maila-pipe';

describe('MailaPipe', () => {
  const pipe = new MailaPipe();

  it('maila baxuak Hasiberria itzultzen du', () => {
    expect(pipe.transform(1)).toBe(MailaIragazkia.Hasiberria);
  });

  it('maila ertainak Ertaina itzultzen du', () => {
    expect(pipe.transform(2)).toBe(MailaIragazkia.Ertaina);
    expect(pipe.transform(3)).toBe(MailaIragazkia.Ertaina);
  });

  it('maila altuak Aurreratua itzultzen du', () => {
    expect(pipe.transform(4)).toBe(MailaIragazkia.Aurreratua);
    expect(pipe.transform(5)).toBe(MailaIragazkia.Aurreratua);
  });
});
