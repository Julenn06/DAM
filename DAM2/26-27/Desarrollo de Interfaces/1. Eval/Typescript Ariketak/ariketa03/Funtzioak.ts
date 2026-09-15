// Ariketa 1
function bataz_bestekoa(a: number, b: number): number {
  return (a + b) / 2;
}

// Ariketa 2
const bataz_bestekoaGezia = (a: number, b: number): number => (a + b) / 2;

// Ariketa 3
function sarrera_prezioa(oinarrizkoa: number, deskontua ? : number, BEZ: number = 1.21): number {
  const azpiprezioa = deskontua ? oinarrizkoa - deskontua : oinarrizkoa;

  return azpiprezioa * BEZ;
}
console.log("1. Oinarrizko prezioa bakarrik (100€):", sarrera_prezioa(100));
console.log("2. Oinarrizkoa eta deskontua (100€ - 20€):", sarrera_prezioa(100, 20));
console.log("3. Hiru parametroak (100€ - 20€, %10eko BEZa):", sarrera_prezioa(100, 20, 1.10));

// Ariketa 4
interface Produktua {
  izena: string;
  stock: number;
  agortuta: () => boolean;
}

const nireProduktua: Produktua = {
  izena: "Ordenagailua",
  stock: 0,
  agortuta: function (): boolean {
    return this.stock === 0;
  }
};

// Ariketa 5
console.log(`Produktua (${nireProduktua.izena}) agortuta dago?`, nireProduktua.agortuta());