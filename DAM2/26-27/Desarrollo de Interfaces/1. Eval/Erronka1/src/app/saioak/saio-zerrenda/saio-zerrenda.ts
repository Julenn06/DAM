import { Component, ElementRef, HostListener, inject, input, output } from '@angular/core';
import { Saioa } from '../../modelo/Saioa';
import { SaioKarta } from '../saio-karta/saio-karta';
import { Datuak } from '../../zerbitzuak/datuak';
import { Iragazketa } from '../../zerbitzuak/iragazketa';

@Component({
  imports: [SaioKarta],
  selector: 'app-saio-zerrenda',
  styleUrl: './saio-zerrenda.css',
  templateUrl: './saio-zerrenda.html',
})
export class SaioZerrenda {
  private iragazketa = inject(Iragazketa);
  private datuak = inject(Datuak);
  private elementua = inject<ElementRef<HTMLElement>>(ElementRef);

  iragazitakoak = this.iragazketa.iragazitakoak;
  kargatzen = this.datuak.kargatzen;
  errorea = this.datuak.errorea;

  aktiboa = input<Saioa | null>(null);
  saioAukeratua = output<Saioa>();

  aukeratu(saioa: Saioa) {
    this.saioAukeratua.emit(saioa);
  }

  /**
   * Saioaren indize honek izan behar duen "tab-index" balioa.
   *
   * "Roving tabindex": Aukeratuta dagoen karta (edo lehenegokoa,
   * ezer ez dagoenean aukeratuta) Tab teklarekin litzateke eskuragarri. Beste guztiak ez.
   *
   * Demagun 50 saio daudela, eta lehenengokoa aukeratuta dugula. Saioaren
   * xehetasunen barruan dagoen botoi edo bestelako elementu bat
   * sakatu nahi bada TAB botoiarekin, ez dira beste 49 saioetatik pasatu behar da.
   *
   * Gezi teklekein aukeratu daitezke besteak.
   */
  tabIndexIndizetik(indizea: number): number {
    const saioak = this.iragazitakoak();
    const aktiboa = this.aktiboa();
    if (saioak.length === 0) return -1;
    if (aktiboa && saioak.includes(aktiboa)) {
      return saioak[indizea] === aktiboa ? 0 : -1;
    }
    return indizea === 0 ? 0 : -1;
  }

  /** Teklatu bidezko nabigazioa */
  @HostListener('keydown', ['$event'])
  teklatua(event: KeyboardEvent): void {
    const saioak = this.iragazitakoak();
    if (saioak.length === 0) return;

    // "tab" rola duten elementuen zerrenda
    const tabZerrenda = Array.from(
      this.elementua.nativeElement.querySelectorAll<HTMLElement>('[role="tab"]'),
    );
    const unekoa = tabZerrenda.indexOf(document.activeElement as HTMLElement);
    if (unekoa === -1) return;

    let hurrengoa: number;
    switch (event.key) {
      case 'ArrowRight':
      case 'ArrowDown':
        hurrengoa = (unekoa + 1) % tabZerrenda.length;
        break;
      case 'ArrowLeft':
      case 'ArrowUp':
        hurrengoa = (unekoa - 1 + tabZerrenda.length) % tabZerrenda.length;
        break;
      case 'Home':
        hurrengoa = 0;
        break;
      case 'End':
        hurrengoa = tabZerrenda.length - 1;
        break;
      default:
        return;
    }

    event.preventDefault();
    const hurrengoAukera = saioak[hurrengoa];
    if (!hurrengoAukera) return;
    this.aukeratu(hurrengoAukera);
    tabZerrenda[hurrengoa]?.focus();
  }
}
