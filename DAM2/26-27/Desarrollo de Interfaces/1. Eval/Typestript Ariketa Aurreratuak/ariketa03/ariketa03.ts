// Ariketa 1
enum Egoera {
    Aktibo,
    EzAktibo,
    Ezabatuta
}

// Ariketa 2
interface Erabiltzailea {
    izena: string;
    egoera: Egoera;
}

// Ariketa 3
const erabiltzaileak: Erabiltzailea[] = [{
        izena: "Jon",
        egoera: Egoera.Aktibo
    },
    {
        izena: "Miren",
        egoera: Egoera.EzAktibo
    },
    {
        izena: "Ane",
        egoera: Egoera.Aktibo
    },
    {
        izena: "Mikel",
        egoera: Egoera.Ezabatuta
    }
];

// Ariketa 4
function aktiboakDira(erabiltzaileak: Erabiltzailea[]): Erabiltzailea[] {
    return erabiltzaileak.filter(erabiltzaile => erabiltzaile.egoera === Egoera.Aktibo);
}

// Ariketa 5
type Tamaina = 'S' | 'M' | 'L' | 'XL';

function stockKontrola(tamaina: Tamaina, kopurua: number): string {
    return `Biltegian '${tamaina}' tamainako ${kopurua} pieza geratzen dira.`;
}

// Ariketa 6
console.log("--- Erabiltzaile Aktiboak ---");
const aktiboak = aktiboakDira(erabiltzaileak);
console.log(aktiboak);

console.log("\n--- Stock Kontrola ---");
const stockMezua1 = stockKontrola('M', 15);
const stockMezua2 = stockKontrola('XL', 3);
console.log(stockMezua1);
console.log(stockMezua2);