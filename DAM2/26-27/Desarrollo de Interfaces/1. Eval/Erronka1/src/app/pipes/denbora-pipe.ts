import { Pipe, PipeTransform } from '@angular/core';

/** Pipea segunduak MM:SS formatura bihurtzeko. */
@Pipe({
  name: 'denbora',
})
export class DenboraPipe implements PipeTransform {
  /** Segundo kopurua jaso eta MM:SS formatuaz itzultzen du. */
  transform(segunduak: number): string {
    const min = Math.floor(segunduak / 60);
    const seg = Math.floor(segunduak % 60);
    return `${min.toString().padStart(2, '0')}:${seg.toString().padStart(2, '0')}`;
  }
}
