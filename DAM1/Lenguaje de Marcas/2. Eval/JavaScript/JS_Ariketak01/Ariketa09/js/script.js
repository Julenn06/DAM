let zenbakia = Math.floor(Math.random() * 10) + 1;

let zbkSartu = 0;

console.log(zenbakia);

do {
  zbkSartu = window.prompt();

  if (zbkSartu > zenbakia) {
    window.alert("Zenbakia handiagoa da, zailatu berriro");
  } else if (zbkSartu < zenbakia) {
    window.alert("Zenbakia txikiagoa da, zailatu berriro");
  }
} while (zbkSartu != zenbakia);

window.alert("Zenbakia berdina da, oso ondo");
