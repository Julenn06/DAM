interface Helbidea {
    kalea: string;
    herria: string;
    PK: string;
}

interface Langilea {
    izena: string;
    kargua: string;
}

interface Enpresa {
    izena: string;
    helbidea: Helbidea;
    langileKopurua: number;
    langileZerrenda: Langilea[];
    datuOsoak(): string;
}

const nireEnpresa: Enpresa = {
    izena: "Teknologia Berriak S.L.",
    helbidea: {
        kalea: "Urkixo Zumarkalea 45",
        herria: "Bilbo",
        PK: "48011"
    },
    langileKopurua: 3,
    langileZerrenda: [{
            izena: "Ane Garcia",
            kargua: "Software Garatzailea"
        },
        {
            izena: "Jon Lopez",
            kargua: "Sistemako Administratzailea"
        },
        {
            izena: "Miren Urkijo",
            kargua: "Proiektu Kudeatzailea"
        }
    ],
    datuOsoak: function (this: Enpresa): string {
        return `${this.izena} enpresak ${this.langileKopurua} langile ditu ${this.helbidea.herria}-n.`;
    }
};

// Ariketa 1-2
const {
    izena: izenSoziala,
    langileKopurua,
    helbidea: {
        herria
    }
} = nireEnpresa;
console.log(izenSoziala);
console.log(langileKopurua);
console.log(herria);


// 3. Ariketa
const jokalariak: string[] = ["Unai", "Mikel", "Ane", "Jon", "Sara"];

const [lehenengoa, bigarrena, ...gainerakoak] = jokalariak;

console.log("\n--- 3. Ariketaren emaitzak ---");
console.log(lehenengoa); // "Unai"
console.log(bigarrena); // "Mikel"
console.log(gainerakoak); // ["Ane", "Jon", "Sara"]


// 4. Ariketa
function bataz_bestekoa2({
    nota1,
    nota2,
    nota3
}: {
    nota1: number;nota2: number;nota3: number
}): number {
    return (nota1 + nota2 + nota3) / 3;
}

const ikasleNotak = {
    nota1: 7,
    nota2: 8,
    nota3: 9
};

const emaitza = bataz_bestekoa2(ikasleNotak);

console.log("\n--- 4. Ariketaren emaitza ---");
console.log(`Batez besteko nota: ${emaitza}`); // 8