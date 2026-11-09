import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MailaIragazkia } from '../../util/MailaIragazkia';
import { Iragazketa } from '../../zerbitzuak/iragazketa';

@Component({
  imports: [FormsModule],
  selector: 'app-saio-bilaketa',
  styleUrl: './saio-bilaketa.css',
  templateUrl: './saio-bilaketa.html',
})
export class SaioBilaketa {
  private iragazketa = inject(Iragazketa);
  protected mailak = [
    MailaIragazkia.Denak,
    MailaIragazkia.Hasiberria,
    MailaIragazkia.Ertaina,
    MailaIragazkia.Aurreratua,
  ];
  private maila = this.iragazketa.maila;
  private bilaketa = this.iragazketa.bilaketa;
  private iragazitakoak = this.iragazketa.iragazitakoak;
}
