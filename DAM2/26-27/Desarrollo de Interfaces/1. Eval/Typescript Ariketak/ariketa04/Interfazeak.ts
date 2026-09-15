// Ariketa 1
interface Helbidea {
    kalea: string;
    herria: string;
    PK: string;
}

interface Langilea {
    izena: string;
    kargua: string;
}

// Ariketa 2
interface Enpresa {
    izena: string;
    helbidea: Helbidea;
    langileKopurua: number;
    langileZerrenda: Langilea[];
    datuOsoak(): string;
}

// Ariketa 3
const nireEnpresa2: Enpresa = {
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

// Ariketa 4
console.log(nireEnpresa2.datuOsoak());

console.log("\n--- Langileen Zerrenda ---");

nireEnpresa2.langileZerrenda.forEach(langilea => {
    console.log(`Langilea: ${langilea.izena} | Kargua: ${langilea.kargua}`);
});