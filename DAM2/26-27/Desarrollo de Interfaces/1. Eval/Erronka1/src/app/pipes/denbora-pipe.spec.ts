import { describe, expect, it } from 'vitest';
import { DenboraPipe } from './denbora-pipe';

describe('DenboraPipe', () => {
  const pipe = new DenboraPipe();

  it('0 segundu "00:00" bihurtzen ditu', () => {
    expect(pipe.transform(0)).toBe('00:00');
  });

  it('segundu bakarrak zeroz betetzen ditu', () => {
    expect(pipe.transform(9)).toBe('00:09');
  });

  it('minutu bakarra itzultzen du', () => {
    expect(pipe.transform(60)).toBe('01:00');
    expect(pipe.transform(65)).toBe('01:05');
  });

  it('ordu bakoitzeko 60 minutu ditu', () => {
    expect(pipe.transform(600)).toBe('10:00');
    expect(pipe.transform(3599)).toBe('59:59');
  });

  it('minutu bakoitzeko 60 segundo ditu', () => {
    expect(pipe.transform(119)).toBe('01:59');
    expect(pipe.transform(120)).toBe('02:00');
  });
});
