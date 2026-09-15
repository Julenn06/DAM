// Ariekta 5
export class Biltegia<T> {
    private elementuak: T[] = [];

    gehitu(elementua: T): void {
        this.elementuak.push(elementua);
    }

    zenbatGuztira(): number {
        return this.elementuak.length;
    }

    lortu(indizea: number): T | undefined {
        return this.elementuak[indizea];
    }

    lortuGuztiak(): T[] {
        return this.elementuak;
    }
}
