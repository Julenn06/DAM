// Ariketa 1
export function lehenengoElementua<T>(zerrenda: T[]): T | undefined {
    return zerrenda[0];
}

// Ariketa 2
export interface Eskaria {
    zenbakia: number;
    produktuak: string[];
}

// Ariketa 3
export interface Bezeroa {
    izena: string;
    eskariak?: Eskaria[];
}

// Ariketa 4
export function eskariKopurua(bezeroa: Bezeroa): number {
    return bezeroa.eskariak?.length ?? 0;
}
