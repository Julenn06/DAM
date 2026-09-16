// Ariketa 1
function zenbakiaEdoTestua(balioa: number | string): string {
    if (typeof balioa === "number") {
        return `Zenbakia: ${balioa}`;
    } else {
        return `Testua: ${balioa}`;
    }
}

// Ariketa 2
class Katua {
    miaukaEgin(): void {
        console.log("Miau");
    }
}

class Txakurra {
    zaunkaEgin(): void {
        console.log("Guau");
    }
}

// Ariketa 3
function soinuaEgin(animalia: Katua | Txakurra): void {
    if (animalia instanceof Katua) {
        animalia.miaukaEgin();
    } else {
        animalia.zaunkaEgin();
    }
}

// Ariketa 4
interface Bezeroa {
    izena: string;
    helbidea: string;
}

interface Enpresa {
    izena: string;
    IFZ: string;
}

function isEnpresa(sarrera: Bezeroa | Enpresa): sarrera is Enpresa {
    return 'IFZ' in sarrera;
}

// Ariketa 5
function identifikatu(sarrera: Bezeroa | Enpresa): string {
    if (isEnpresa(sarrera)) {
        return `Enpresa: ${sarrera.izena} (IFZ: ${sarrera.IFZ})`;
    } else {
        return `Bezero partikularra: ${sarrera.izena} (Helbidea: ${sarrera.helbidea})`;
    }
}


console.log("--- 1. URBATSA: typeof ---");
console.log(zenbakiaEdoTestua(42));
console.log(zenbakiaEdoTestua("Agur"));

console.log("\n--- 3. URBATSA: instanceof ---");
const nireKatua = new Katua();
const nireTxakurra = new Txakurra();
soinuaEgin(nireKatua);
soinuaEgin(nireTxakurra);

console.log("\n--- 5. URBATSA: Type Guard (in) ---");
const bezeroa1: Bezeroa = {
    izena: "Koldo",
    helbidea: "Urkiola Kalea 5"
};
const enpresa1: Enpresa = {
    izena: "Tekno SL",
    IFZ: "B12345678"
};

console.log(identifikatu(bezeroa1));
console.log(identifikatu(enpresa1));