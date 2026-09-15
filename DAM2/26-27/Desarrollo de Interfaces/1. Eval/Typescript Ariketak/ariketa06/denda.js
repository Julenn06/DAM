// Ariketa 2
import { kalkulatuLaburpena } from './produktuak.js';
// Ariketa 3
const nireProduktuak = [
    { izena: "Ordenagailua", prezioa: 850, kategoria: "Teknologia" },
    { izena: "Sagua", prezioa: 25, kategoria: "Teknologia" },
    { izena: "Teklatua", prezioa: 45, kategoria: "Teknologia" },
    { izena: "Monitorea", prezioa: 180, kategoria: "Teknologia" }
];
const [kopurua, guztira] = kalkulatuLaburpena(nireProduktuak);
// Ariketa 4
console.log(`Guztira ${kopurua} produktu, ${guztira}€-ko balioarekin.`);
