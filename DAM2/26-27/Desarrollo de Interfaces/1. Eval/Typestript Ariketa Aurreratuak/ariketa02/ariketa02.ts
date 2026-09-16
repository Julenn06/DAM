interface Marrazgarria {
    marraztu(): void;
}

// Ariketa 1
abstract class Forma {
    abstract azalera(): number;

    deskribapena(): string {
        return `Azalera: ${this.azalera().toFixed(2)}`;
    }
}

// 2. Sortu Zirkulua klasea, Forma-tik heredatzen duena eta Marrazgarria inplementatzen duena
class Zirkulua extends Forma implements Marrazgarria {
    public erradioa: number;

    constructor(erradioa: number) {
        super();
        this.erradioa = erradioa;
    }

    azalera(): number {
        return Math.PI * Math.pow(this.erradioa, 2);
    }

    // Ariketa 5
    marraztu(): void {
        console.log(`Zirkulu bat marrazten... (Erradioa: ${this.erradioa})`);
    }
}

// Ariketa 3
class Laukizuzena extends Forma {
    public zabalera: number;
    public altuera: number;

    constructor(zabalera: number, altuera: number) {
        super();
        this.zabalera = zabalera;
        this.altuera = altuera;
    }

    azalera(): number {
        return this.zabalera * this.altuera;
    }
}

// Ariketa 4
const nireZirkulua = new Zirkulua(5);
const nireLaukizuzena = new Laukizuzena(4, 6);

const formak: Forma[] = [nireZirkulua, nireLaukizuzena];

formak.forEach((forma) => {
    console.log(forma.deskribapena());
});

// Ariketa 6
nireZirkulua.marraztu();