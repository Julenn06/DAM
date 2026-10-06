function taula() {
  const datuak = {};
  const inputs = document.querySelectorAll("input, select");
  let total = 0;
  const windowsSE = document.getElementsByName("aukera");

  if (windowsSE[0].checked) {
    total = 100;
  }

  for (const input of inputs) {
    const izena = input.name;
    let balioa = input.value.trim();

    if (izena) {
      if (balioa == 0) {
        alert("Balio guztiak derrigorrezkoak dira");
        return;
      }
      if (izena == "telefonoa") {
        if (balioa.length != 9) {
          alert("Telefonoa ez da zuzena");
          return;
        }
      }
      if (izena == "email") {
        if (!balioa.includes("@")) {
          alert("Emaila ez da zuzena");
          return;
        }
      }
      datuak[izena] = balioa;
    }
  }

  const aukerak = document.querySelectorAll("select");
  for (const select of aukerak) {
    const selectedOption = select.options[select.selectedIndex];

    const value2 = selectedOption.getAttribute("data-value2");
    if (value2) {
      total += parseInt(value2);
    }
  }

  document.querySelector("#taula").classList.remove("hidden");
  const emaitza = `${datuak.prozezadorea}, ${datuak.grafikoa}, ${datuak.memoria}, ${datuak.diskoa}`;

  let table = document.querySelector("#taula");
  let body = table.tBodies[0];
  let lerroa = body.insertRow();
  let g1 = lerroa.insertCell();
  g1.innerText = datuak.izena;
  let g2 = lerroa.insertCell();
  g2.innerText = datuak.email;
  let g3 = lerroa.insertCell();
  g3.innerText = datuak.telefonoa;
  let g4 = lerroa.insertCell();
  g4.innerText = emaitza;
  let g5 = lerroa.insertCell();
  g5.innerText = total;
}
