// Ariketa 1
class Ibilgailua {
    constructor(
        public marka: string,
        public matrikula: string
    ) {}

    informazioa(): string {
        return `Marka: ${this.marka} | Matrikula: ${this.matrikula}`;
    }
}

// Ariketa 2
class Autoa extends Ibilgailua {
    public plazaKopurua: number;

    constructor(marka: string, matrikula: string, plazaKopurua: number) {
        super(marka, matrikula);
        this.plazaKopurua = plazaKopurua;
    }
}

// Ariketa 3
class Motorra extends Ibilgailua {
    public zilindrada ? : number;

    constructor(marka: string, matrikula: string, zilindrada ? : number) {
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