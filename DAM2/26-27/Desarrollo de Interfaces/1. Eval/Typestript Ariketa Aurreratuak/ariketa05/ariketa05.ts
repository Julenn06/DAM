class Biltegia < T > {
    private elementuak: T[] = [];

    gehitu(elementua: T): void {
        this.elementuak.push(elementua);
    }

    guztiakLortu(): T[] {
        return this.elementuak;
    }
}

// Ariketa 1
interface Produktua {
    id: number;
    izena: string;
    prezioa: number;
    stock: number;
}

// Ariketa 2
function eguneratuProduktua(produktua: Produktua, aldaketak: Partial < Produktua > ): Produktua {
    // Jatorrizkoa aldatu gabe, objektu berri bat itzultzen du kopia eginez
    return {
        ...produktua,
        ...aldaketak
    };
}

// Ariketa 3
type ProduktuLaburpena = Pick < Produktua, 'izena' | 'prezioa' > ;

function zerrendatuLaburpenak(produktuak: Produktua[]): ProduktuLaburpena[] {
    return produktuak.map(p => ({
        izena: p.izena,
        prezioa: p.prezioa
    }));
}

// Ariketa 4
async function lortuProduktuak(): Promise < Produktua[] > {
    return new Promise((ebatzi) => {
        setTimeout(() => {
            const datuak: Produktua[] = [{
                    id: 1,
                    izena: "Ordenagailua",
                    prezioa: 800,
                    stock: 10
                },
                {
                    id: 2,
                    izena: "Sagua",
                    prezioa: 25,
                    stock: 50
                },
                {
                    id: 3,
                    izena: "Teklatua",
                    prezioa: 45,
                    stock: 30
                }
            ];
            ebatzi(datuak);
        }, 1000);
    });
}

async function main() {
    console.log("Produktuak kargatzen... (Itxaron mesedez)");

    // Ariketa 5
    const produktuak = await lortuProduktuak();
    console.log("\n--- Produktu guztiak kargatuta ---");
    console.log(produktuak);

    const ordenagailuEguneratua = eguneratuProduktua(produktuak[0], {
        prezioa: 750,
        stock: 8
    });
    console.log("\n--- Produktu bat eguneratuta (Kopia berria) ---");
    console.log(ordenagailuEguneratua);
    console.log("Jatorrizkoa ez da aldatu:", produktuak[0].prezioa + "€");

    const laburpenak = zerrendatuLaburpenak(produktuak);
    console.log("\n--- Produktuen Laburpenak (Pick) ---");
    console.log(laburpenak);

    // Ariketa 6
    const nireBiltegia = new Biltegia < Produktua > ();

    produktuak.forEach(p => nireBiltegia.gehitu(p));

    console.log("\n--- Erronka: Biltegian gordetako produktuak ---");
    console.log(nireBiltegia.guztiakLortu());
}

main();