function formulario() {
  const nombre = document.getElementById("izena").value;
  const email = document.getElementById("email").value;
  const telefono = document.getElementById("telefonoa").value;
  const helbidea = document.getElementById("helbidea").value;
  let nongoa = document.getElementsByName("aukera");
  const postakodea = document.getElementById("postakodea").value;
  const generoa = document.getElementById("generoa").value;

  if (
    nombre == "" ||
    email == "" ||
    telefono == "" ||
    helbidea == "" ||
    nongoa == "0" ||
    postakodea == "" ||
    generoa == ""
  ) {
    alert("Ezin da hutsik utzi eremuren bat");
    return false;
  }

  if (nombre.length < 3) {
    alert("Izena motzegia da");
    return false;
  }

  if (!email.includes("@") || !email.includes(".")) {
    alert("Email ez da zuzena");
    return false;
  }

  if (
    telefono.length < 9 ||
    telefono.length > 9 ||
    telefono[0] != 6 ||
    isNaN(telefono)
  ) {
    alert("Telefono zenbakia ez da zuzena");
    return false;
  }

  if (helbidea.length < 7) {
    alert("Helbidea motzegia da");
    return false;
  }

  if (postakodea.length != 5 || isNaN(postakodea)) {
    alert("Posta kodea ez da zuzena");
    return false;
  }

  if (generoa == "0") {
    alert("Generoa ez da zuzena");
    return false;
  }

  if (nongoa[0].checked == true) {
    nongoa = nongoa[0].value;
  } else if (nongoa[1].checked == true) {
    nongoa = nongoa[1].value;
  }

  alert("Formularioa ondo bete duzu");

  taulaBertikala(
    nombre,
    email,
    telefono,
    helbidea,
    nongoa,
    postakodea,
    generoa,
  );
  taulaHorizontala(
    nombre,
    email,
    telefono,
    helbidea,
    nongoa,
    postakodea,
    generoa,
  );
}

function taulaBertikala(
  nombre,
  email,
  telefono,
  helbidea,
  nongoa,
  postakodea,
  generoa,
) {
  document.querySelector("#taula").classList.remove("hidden");

  let table = document.querySelector("#taula");
  let body = table.tBodies[0];
  let lerroa = body.insertRow();
  let g1 = lerroa.insertCell();
  g1.innerText = nombre;
  let g2 = lerroa.insertCell();
  g2.innerText = email;
  let g3 = lerroa.insertCell();
  g3.innerText = telefono;
  let g4 = lerroa.insertCell();
  g4.innerText = helbidea;
  let g5 = lerroa.insertCell();
  g5.innerText = nongoa;
  let g6 = lerroa.insertCell();
  g6.innerText = postakodea;
  let g7 = lerroa.insertCell();
  g7.innerText = generoa;
}

function taulaHorizontala(
  nombre,
  email,
  telefono,
  helbidea,
  nongoa,
  postakodea,
  generoa,
) {
  document.querySelector("#taula2").classList.remove("hidden");

  let table = document.querySelector("#taula2");
  let rows = table.rows;

  rows[0].insertCell().innerText = nombre;
  rows[1].insertCell().innerText = email;
  rows[2].insertCell().innerText = telefono;
  rows[3].insertCell().innerText = helbidea;
  rows[4].insertCell().innerText = nongoa;
  rows[5].insertCell().innerText = postakodea;
  rows[6].insertCell().innerText = generoa;
}
