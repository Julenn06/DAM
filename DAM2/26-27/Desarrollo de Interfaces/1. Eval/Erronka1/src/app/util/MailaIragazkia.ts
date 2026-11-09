export enum MailaIragazkia {
  Denak = 'Denak',
  Hasiberria = 'Hasiberria',
  Ertaina = 'Ertaina',
  Aurreratua = 'Aurreratua',
}

export function mailaIzena(maila: number): MailaIragazkia {
  if (maila <= 1) return MailaIragazkia.Hasiberria;
  if (maila <= 3) return MailaIragazkia.Ertaina;
  return MailaIragazkia.Aurreratua;
}
