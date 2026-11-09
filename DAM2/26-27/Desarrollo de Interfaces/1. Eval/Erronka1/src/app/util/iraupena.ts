import { Ariketa } from '../modelo/Ariketa';
import { Saioa } from '../modelo/Saioa';

/**
 * Ariketaren iraupen osoa kalkulatu.
 *
 * Ariketaren saio bakoitzeko, saioaren iraupena eta ariketaren atsedena gehitu.
 *
 * Ariketaren atsedena, serie bakoitzaren ondoreko atsedenari deritzo.
 */
export function ariketaIraupena(ariketa: Ariketa): number {
  return ariketa.serieak.reduce((total, seriea) => total + seriea.iraupena + ariketa.atsedena, 0);
}

/**
 * Saioaren iraupen osoa kalkulatu.
 *
 * Saioaren ariketa guztien iraupenen batura. `ariketaIraupena` erabiltzen du.
 */
export function saioIraupena(saioa: Saioa): number {
  return saioa.ariketak.reduce((total, ariketa) => total + ariketaIraupena(ariketa), 0);
}
