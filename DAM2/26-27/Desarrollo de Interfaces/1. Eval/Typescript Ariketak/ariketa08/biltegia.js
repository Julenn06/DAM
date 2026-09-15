// Ariekta 5
export class Biltegia {
    elementuak = [];
    gehitu(elementua) {
        this.elementuak.push(elementua);
    }
    zenbatGuztira() {
        return this.elementuak.length;
    }
    lortu(indizea) {
        return this.elementuak[indizea];
    }
    lortuGuztiak() {
        return this.elementuak;
    }
}
