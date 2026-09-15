"use strict";
// Ariketa 1
class Ibilgailua {
    marka;
    matrikula;
    constructor(marka, matrikula) {
        this.marka = marka;
        this.matrikula = matrikula;
    }
    informazioa() {
        return `Marka: ${this.marka} | Matrikula: ${this.matrikula}`;
    }
}
// Ariketa 2
class Autoa extends Ibilgailua {
    plazaKopurua;
    constructor(marka, matrikula, plazaKopurua) {
        super(marka, matrikula);
        this.plazaKopurua = plazaKopurua;
    }
}
// Ariketa 3
class Motorra extends Ibilgailua {
    zilindrada;
    constructor(marka, matrikula, zilindrada) {
        super(marka, matrikula);
        this.zilindrada = zilindrada;
    }
}
// Ariketa 4
const nireAutoa = new Autoa("Toyota", "1234BBB", 5);
const nireMotorra = new Motorra("Yamaha", "5678CCC", 600);
console.log(nireAutoa);
console.log(nireMotorra);
// Ariketa 5
console.log(nireAutoa.informazioa());
console.log(nireMotorra.informazioa());
