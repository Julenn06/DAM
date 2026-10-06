let zenbakiak = [4, 8, 15, 16, 23, 42];

let totala = 0;

let handiena = zenbakiak[0];

let txikiena = zenbakiak[0];

for (let i = 0; i < zenbakiak.length; i++) {
  totala = totala + zenbakiak[i];

  if (handiena < zenbakiak[i]) {
    handiena = zenbakiak[i];
  }

  if (txikiena < txikiena[i]) {
    txikiena = txikiena[i];
  }
}

console.log(totala);
console.log(handiena);
console.log(txikiena);
