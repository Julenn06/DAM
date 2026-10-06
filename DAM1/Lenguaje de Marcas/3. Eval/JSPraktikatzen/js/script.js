const min = 1;
const max = 9;

const emaitza = Math.floor(Math.random() * (max - min + 1) + min);
console.log(emaitza);

const taula = document.createElement("table");
for (let i = 0; i < 15; i++) {
  const tr = document.createElement("tr");
  for (let z = 0; z < 15; z++) {
    let ilara;
    if (i === 0) {
      ilara = document.createElement("th");
    } else {
      ilara = document.createElement("td");
    }
    ilara.textContent = Math.floor(Math.random() * (max - min + 1) + min);
    ilara.addEventListener("click", function () {
      if (ilara.textContent !== emaitza.toString()) {
        return;
      }
      alert(`Aukera egokia sakatu duzu: ${ilara.textContent}`);
      document.body.removeChild(taula);
      location.reload();
    });
    tr.appendChild(ilara);
  }
  taula.appendChild(tr);
}
document.body.appendChild(taula);
