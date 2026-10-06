async function bidaiaErregistratu(
  izena,
  bidaiaMota,
  hasieraData,
  hamaieraData,
  egunak,
  herrialdea,
  deskribapena,
) {
  if (
    !izena ||
    !bidaiaMota ||
    !hasieraData ||
    !hamaieraData ||
    !egunak ||
    !herrialdea ||
    !deskribapena
  ) {
    Swal.fire("Errorea!", "Datu guztiak bete.", "error");
    return;
  }

  let today = new Date().toISOString().split("T")[0];

  if (hasieraData < today) {
    Swal.fire(
      "Errorea!",
      "Hasiera data ezin da izan gaurko data baino lehenagokoa.",
      "error",
    );
    return;
  } else if (hamaieraData < today) {
    Swal.fire(
      "Errorea!",
      "Amaiera data ezin da izan gaurko data baino lehenagokoa.",
      "error",
    );
    return;
  } else if (hasieraData > hamaieraData) {
    Swal.fire(
      "Errorea!",
      "Hasiera data lehenagoa izan behar da hamaiera data baino.",
      "error",
    );
    return;
  }

  if (egunak < 1) {
    Swal.fire("Errorea!", "Egun kopurua ezin da 0 izan.", "error");
    return;
  }

  if (deskribapena.length < 5) {
    Swal.fire("Errorea!", "Deskribapena motzegia da.", "error");
    return;
  }

  const datuak = {};
  const formulario = document.querySelector("#form-bidaia");
  const inputs = formulario.querySelectorAll("input, select, textarea");
  let formularioAurkitua = null;

  for (const input of inputs) {
    const izena = input.name;
    const balioa = input.value.trim();

    if (izena) {
      datuak[izena] = balioa;
    }
  }
  formularioAurkitua = {
    idFormulario: formulario.id,
    datuak,
  };

  const taula = document.querySelector("#taula");

  if (taula) {
    taula.classList.remove("hidden");
  }

  let table = document.querySelector("#taula");
  let body = table.tBodies[0];
  let lerroa = body.insertRow();
  let g1 = lerroa.insertCell();
  g1.innerText = datuak.izena;
  let g2 = lerroa.insertCell();
  g2.innerText = datuak.bidaiaMota;
  let g3 = lerroa.insertCell();
  g3.innerText = datuak.hasieraData;
  let g4 = lerroa.insertCell();
  g4.innerText = datuak.amaieraData;
  let g5 = lerroa.insertCell();
  g5.innerText = datuak.egunak;
  let g6 = lerroa.insertCell();
  g6.innerText = datuak.herrialdea;
  let g7 = lerroa.insertCell();
  g7.innerText = datuak.deskribapena;

  const formData = new FormData();
  formData.append("idFormulario", formularioAurkitua.idFormulario);
  for (const key in formularioAurkitua.datuak) {
    formData.append(key, formularioAurkitua.datuak[key]);
  }

  try {
    const response = await fetch("../php/insertBidaia.php", {
      method: "POST",
      body: formData,
    });
    const data = await response.text();
    console.log("Zerbitzariaren Erantzuna:", data);
    Swal.fire("Ondo!", "Datuak Gordeko Dira.", "success");
  } catch (error) {
    console.error("Errorea bidaltzean:", error);
    Swal.fire("Errorea!", "Datuak Ez Dira Bidali.", "error");
  }

  const inputGuztiak = document.querySelectorAll(
    "#form-bidaia input, #form-bidaia select, #form-bidaia textarea",
  );
  inputGuztiak.forEach((input) => {
    input.value = "";
  });
}

function egunakKalkulatu() {
  let hasieraData = document.getElementById("hasieraData").value;
  let hamaieraData = document.getElementById("amaieraData").value;

  hasieraData = new Date(hasieraData);
  amaieraData = new Date(hamaieraData);

  let dataKenketa = amaieraData - hasieraData;
  let egunKopurua = dataKenketa / (1000 * 60 * 60 * 24) + 1;

  document.getElementById("egunak").value = egunKopurua;
}
