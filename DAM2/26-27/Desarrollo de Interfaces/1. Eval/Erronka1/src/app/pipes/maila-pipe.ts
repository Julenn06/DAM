import { Pipe, PipeTransform } from '@angular/core';
import { mailaIzena } from '../util/MailaIragazkia';

/** Pipea maila zenbaki bat izen deskribatzaile bihurtzeko. */
@Pipe({
  name: 'maila',
})
export class MailaPipe implements PipeTransform {
  /** Mailaren zenbakia jaso eta dagokion izena itzultzen du. */
  transform(maila: number): string {
    return mailaIzena(maila);
  }
}
