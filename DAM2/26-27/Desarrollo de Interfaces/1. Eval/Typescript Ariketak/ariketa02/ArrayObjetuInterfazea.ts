// Ariketa 1
interface Ikasle {
    izena: string;
    nota: number;
    gaitasunak: string[];
    errepikatzailea ? : boolean;
}

// Ariketa 2
const ikasleak: Ikasle[] = [{
        izena: "Ane",
        nota: 8.5,
        gaitasunak: ["Programazioa", "Diseinua"]
    },
    {
        izena: "Mikel",
        nota: 6.2,
        gaitasunak: ["Datu-baseak", "Sareak"]
    },
    {
        izena: "Jon",
        nota: 7.8,
        gaitasunak: ["Logika", "Matematika"]
    }
];

// Ariketa 3
console.log("--- Ikasleen notak ---");
for (const ikasle of ikasleak) {
    console.log(`${ikasle.izena}: ${ikasle.nota}`);
}

// Ariketa 4
let notaGuztira = 0;
for (const ikasle of ikasleak) {
    notaGuztira += ikasle.nota;
}
const batezBestekoa = notaGuztira / ikasleak.length;

console.log(`\nKlaseko nota-batez bestekoa: ${batezBestekoa.toFixed(2)}`);

// Ariketa 5
ikasleak[0].errepikatzailea = true;

console.log("\n--- Ikasleen taula (Aldaketarekin) ---");
console.table(ikasleak);