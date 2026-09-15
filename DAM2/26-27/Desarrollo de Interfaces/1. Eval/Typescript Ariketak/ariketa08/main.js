// Ariketa 7
import { lehenengoElementua, eskariKopurua } from './modeloak.js';
import { Biltegia } from './biltegia.js';
const stringArray = ["A", "B", "C"];
const numberArray = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
const bezeroArray = [{
        izena: "Ane"
    }, {
        izena: "Jon"
    }];
console.log("String lehenengoa:", lehenengoElementua(stringArray));
console.log("Number lehenengoa:", lehenengoElementua(numberArray));
console.log("Bezero lehenengoa:", lehenengoElementua(bezeroArray)?.izena);
const bezeroenBiltegia = new Biltegia();
bezeroenBiltegia.gehitu({
    izena: "Mikel",
    eskariak: [{
            zenbakia: 1,
            produktuak: ["Ordenagailua", "Sagua"]
        },
        {
            zenbakia: 2,
            produktuak: ["Teklatua"]
        }
    ]
});
bezeroenBiltegia.gehitu({
    izena: "Sara",
    eskariak: []
});
bezeroenBiltegia.gehitu({
    izena: "Gorka"
});
bezeroenBiltegia.lortuGuztiak().forEach(bezeroa => {
    const kopurua = eskariKopurua(bezeroa);
    console.log(`Bezeroa: ${bezeroa.izena} | Eskari kopurua: ${kopurua}`);
});
