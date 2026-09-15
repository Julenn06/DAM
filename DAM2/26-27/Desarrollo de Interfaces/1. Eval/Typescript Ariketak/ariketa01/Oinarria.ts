// Ariketa 1
let izena: string = "Julen";
let adina: number = 20;
let matrikulatuta: boolean = true;

console.log("izena: " + izena);
console.log("adina: " + adina);
console.log("matrikulatuta: " + matrikulatuta);


// Ariketa 2
let egoera: number | string = 100;
console.log("Hasierako egoera (zenbakia):", egoera);

egoera = 'OSASUNTSU';
console.log("Aldatutako egoera (testua):", egoera);


// Ariketa 3
console.log({
    izena,
    adina,
    matrikulatuta
});


// Ariketa 4
// adina = 'hogei'; 
//
// Type 'string' is not assignable to type 'number'.
//
// Ezin duzu number bariable bati testu bat jarri