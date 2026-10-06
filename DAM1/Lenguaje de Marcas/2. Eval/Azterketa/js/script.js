function prezioaArruntak() {
  let arrunta = document.getElementById("arrunta").value;
  let emaitzaArrunta = document.getElementById("emaitzaArrunta");
  let prezioa = arrunta * 12.4;
  emaitzaArrunta.value = `${prezioa.toFixed(2)} €`;
}

function prezioaHaurrak() {
  let haurrak = document.getElementById("haurrak").value;
  let emaitzaHaurra = document.getElementById("emaitzaHaurra");
  let prezioa = haurrak * 10.4;
  emaitzaHaurra.value = `${prezioa.toFixed(2)} €`;
}

function prezioaNagusiak() {
  let nagusiak = document.getElementById("nagusiak").value;
  let emaitzaNagusia = document.getElementById("emaitzaNagusia");
  let prezioa = nagusiak * 10.4;
  emaitzaNagusia.value = `${prezioa.toFixed(2)} €`;
}

function adinaPelikula(balidatu) {
  let arrunta = document.getElementById("arrunta").value;
  let haurrak = document.getElementById("haurrak").value;
  let nagusiak = document.getElementById("nagusiak").value;
  let filma = document.getElementById("filma").value;

  if (filma == "") {
    alert("Filma Bat Aukeratu");
    return (balidatu = false);
  }

  if (arrunta == 0 && haurrak == 0 && nagusiak == 0) {
    alert("Gutxienez sarrera Bat erosi");
    return (balidatu = false);
  }

  if (haurrak >= 1) {
    if (filma != "TP") {
      alert("Haurrek ezin dute pelikula hori ikusi");
      return (balidatu = false);
    }
  }

  return (balidatu = true);
}

function palomita() {
  let palomitak = document.getElementById("palomitak").checked;
  let kantitatePalomitak = document.querySelector("#kantitatePalomitak");

  if (palomitak) {
    kantitatePalomitak.removeAttribute("readonly");
  } else {
    kantitatePalomitak.setAttribute("readonly", true);
    kantitatePalomitak.value = 0;
  }
}

function freskagarria() {
  let freskagarriak = document.getElementById("freskagarriak").checked;
  let kantitateFreskagarriak = document.querySelector(
    "#kantitateFreskagarriak",
  );

  if (freskagarriak) {
    kantitateFreskagarriak.removeAttribute("readonly");
  } else {
    kantitateFreskagarriak.setAttribute("readonly", true);
    kantitateFreskagarriak.value = 0;
  }
}

function emaitzaFinala() {
  let izena = document.getElementById("izena").value;
  let eposta = document.getElementById("eposta").value;
  let telefonoa = document.getElementById("telefonoa").value;
  let palomitak = document.getElementById("kantitatePalomitak").value;
  let freskagarriak = document.getElementById("kantitateFreskagarriak").value;
  let emaitzaArrunta = parseFloat(
    document.getElementById("emaitzaArrunta").value,
  );
  let emaitzaHaurra = parseFloat(
    document.getElementById("emaitzaHaurra").value,
  );
  let emaitzaNagusia = parseFloat(
    document.getElementById("emaitzaNagusia").value,
  );

  let balidatu = true;

  if (izena == "" || eposta == "" || telefonoa == "") {
    alert("Zure Datu Pertsonal Guztiak Bete");
    return;
  }

  if (!eposta.includes("@") || !eposta.includes(".")) {
    alert("Email ez da zuzena");
    return;
  }

  if (
    telefonoa.length < 9 ||
    telefonoa.length > 9 ||
    telefonoa[0] != 6 ||
    isNaN(telefonoa)
  ) {
    alert("Telefono zenbakia ez da zuzena");
    return;
  }

  balidatu = adinaPelikula(balidatu);

  if (balidatu == true) {
    palomitak = palomitak * 5;
    freskagarriak = freskagarriak * 3;

    let totala = (
      palomitak +
      freskagarriak +
      emaitzaArrunta +
      emaitzaHaurra +
      emaitzaNagusia
    ).toFixed(2);

    alert(izena + ", kobratuko zaizun totala " + totala + "€ dira.");

    location.href = "../erreserbaDatuak.php";
  }
}
