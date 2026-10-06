let NANHizkiak = [
  "T",
  "R",
  "W",
  "A",
  "G",
  "M",
  "Y",
  "F",
  "P",
  "D",
  "X",
  "B",
  "N",
  "J",
  "Z",
  "S",
  "Q",
  "V",
  "H",
  "L",
  "C",
  "K",
  "E",
];
let numero = window.prompt(
  "Sartu zure NAN zenbakia (8 zenbaki eta letra bat):",
);

if (numero && numero.length === 9) {
  let letraCalculada = NANHizkiak[parseInt(numero.slice(0, 8)) % 23];
  let letra = numero.charAt(8).toUpperCase();

  if (letra === letraCalculada) {
    window.alert("NAN zenbakia zuzena da");
  } else {
    window.alert("NAN zenbakia ez da zuzena");
  }

  console.log(letraCalculada);
} else {
  window.alert(
    "Errorea: NAN zenbakia 8 zenbaki eta letra bat izan behar ditu.",
  );
}
