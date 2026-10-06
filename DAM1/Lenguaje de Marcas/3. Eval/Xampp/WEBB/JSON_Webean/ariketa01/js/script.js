let pelikulak = [];

fetch("pelikulak.json")
  .then((response) => response.json())
  .then((data) => {
    pelikulak = data;
    erakutsiPelikulak(pelikulak);
  })
  .catch((error) => console.error("Error fetching JSON:", error));

function erakutsiPelikulak(pelikulak) {
  const kontenedorea = document.getElementById("kontenedorea");
  while (kontenedorea.firstChild) {
    kontenedorea.removeChild(kontenedorea.firstChild);
  }
  pelikulak.forEach((pelikula) => {
    const txartela = document.createElement("div");
    txartela.className = "txartela";

    const irudia = document.createElement("img");
    irudia.src = pelikula.irudia;
    irudia.alt = pelikula.titulua;

    const titulua = document.createElement("h2");
    titulua.textContent = pelikula.titulua;

    const kaleratzeurtea = document.createElement("p");
    kaleratzeurtea.textContent = "Kaleratze Urtea: " + pelikula.kaleratzeurtea;

    const zuzendaria = document.createElement("p");
    zuzendaria.textContent = "Zuzendaria: " + pelikula.zuzendaria;

    const linka = document.createElement("a");
    linka.href = irudia.src;
    linka.textContent = "Informazio gehigarria";

    txartela.appendChild(irudia);
    txartela.appendChild(titulua);
    txartela.appendChild(kaleratzeurtea);
    txartela.appendChild(zuzendaria);
    txartela.appendChild(linka);

    kontenedorea.appendChild(txartela);
  });
}

document.getElementById("bilaketa").addEventListener("input", (e) => {
  const bilaketaTestua = e.target.value.toLowerCase();
  const filtratutakoPelikulak = pelikulak.filter((pelikula) =>
    pelikula.titulua.toLowerCase().includes(bilaketaTestua),
  );
  erakutsiPelikulak(filtratutakoPelikulak);
});
