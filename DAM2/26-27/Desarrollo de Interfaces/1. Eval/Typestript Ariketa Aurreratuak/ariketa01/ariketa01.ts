class KontuBankarioa {
    // Ariketa 1
    public readonly titularra: string;
    private saldoa: number;

    constructor(titularra: string, hasierakoSaldoa: number) {
        this.titularra = titularra;
        this.saldoa = hasierakoSaldoa >= 0 ? hasierakoSaldoa : 0;
    }

    // Ariketa 2
    public sartu(kopurua: number): void {
        if (kopurua > 0) {
            this.saldoa += kopurua;
            console.log(`${kopurua}€ sartu dira. Saldo berria: ${this.saldoa}€`);
        } else {
            console.log("Errorea: Sartu beharreko kopuruak positiboa izan behar du.");
        }
    }

    // Ariketa 3
    public atera(kopurua: number): void {
        if (kopurua > this.saldoa) {
            console.log(`Errorea: Ez dago nahiko saldorik (${this.saldoa}€ dituzu).`);
        } else if (kopurua <= 0) {
            console.log("Errorea: Kopuruak positiboa izan behar du.");
        } else {
            this.saldoa -= kopurua;
            console.log(`${kopurua}€ atera dira. Saldo berria: ${this.saldoa}€`);
        }
    }

    // Ariketa 4
    public get unekoSaldoa(): number {
        return this.saldoa;
    }
}

// Ariketa 5
const nireKontua = new KontuBankarioa("Ane Garcia", 500);
console.log(`Titularra: ${nireKontua.titularra}`);
console.log(`Hasierako saldoa: ${nireKontua.unekoSaldoa}€`);

nireKontua.sartu(200);
nireKontua.sartu(-50);
nireKontua.atera(150);
nireKontua.atera(1000);

// Ariketa 6 (Kode honek konpilazio errorea emango luke):
// nireKontua.saldoa = 2000; 
// nireKontua.titularra = "Jon";