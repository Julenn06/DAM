// Ariketa 1
export interface Produktua {
    izena: string;
    prezioa: number;
    kategoria: string;
}

export function kalkulatuLaburpena(produktuak: Produktua[]): [number, number] {
    let batura = 0;
    const kopurua = produktuak.length;

    produktuak.forEach(({ prezioa }) => {
        batura += prezioa;
    });

    return [kopurua, batura];
}
