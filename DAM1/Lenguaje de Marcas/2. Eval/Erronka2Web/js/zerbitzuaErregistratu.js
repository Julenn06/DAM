async function datuakLortu() {
  const formulario = document.querySelector('div[id^="form-"]:not(.hidden)');
  let formularioAurkitua = null;
  let today = new Date().toISOString().split("T")[0];
  const datuak = {};

  if (formulario) {
    const inputs = formulario.querySelectorAll("input, select, textarea");
    let formularioOsorik = true;

    for (const input of inputs) {
      const balioa = input.value.trim();

      if (balioa === "") {
        formularioOsorik = false;
        Swal.fire("Errorea!", "Datu Guztiak Bete.", "error");
        return;
      }
    }
    if (formulario.id === "form-hegaldia") {
      const hasieraData = document.querySelector(".dataJoanHegaldia").value; // Joaneko data
      if (hasieraData < today) {
        formularioOsorik = false;
        Swal.fire(
          "Errorea!",
          "Hasiera data ezin da izan gaurko data baino lehenagokoa.",
          "error",
        );
        return;
      }
    } else if (formulario.id === "form-ostatua") {
      const hasieraData = document.querySelector(".dataJoanOstatua").value; // Joaneko data
      const amaieraData = document.querySelector(".dataEtorriOstatua").value; // Etorrerako data

      if (hasieraData < today) {
        formularioOsorik = false;
        Swal.fire(
          "Errorea!",
          "Hasiera data ezin da izan gaurko data baino lehenagokoa.",
          "error",
        );
        return;
      } else if (amaieraData < today) {
        formularioOsorik = false;
        Swal.fire(
          "Errorea!",
          "Amaiera data ezin da izan gaurko data baino lehenagokoa.",
          "error",
        );
        return;
      } else if (hasieraData > amaieraData) {
        formularioOsorik = false;
        Swal.fire(
          "Errorea!",
          "Hasiera data lehenagoa izan behar da hamaiera data baino.",
          "error",
        );
        return;
      }
    } else if (formulario.id === "form-joan-etorri") {
      const hasieraData = document.querySelector(".data-joan-etorri").value; // Joaneko data
      const amaieraData = document.querySelector(".data-etorri-etorri").value; // Etorrerako data

      if (hasieraData < today) {
        formularioOsorik = false;
        Swal.fire(
          "Errorea!",
          "Hasiera data ezin da izan gaurko data baino lehenagokoa.",
          "error",
        );
        return;
      } else if (amaieraData < today) {
        formularioOsorik = false;
        Swal.fire(
          "Errorea!",
          "Amaiera data ezin da izan gaurko data baino lehenagokoa.",
          "error",
        );
        return;
      } else if (hasieraData > amaieraData) {
        formularioOsorik = false;
        Swal.fire(
          "Errorea!",
          "Hasiera data lehenagoa izan behar da hamaiera data baino.",
          "error",
        );
        return;
      }
    } else if (formulario.id === "form-beste-bat") {
      const hasieraData = document.querySelector(".dataBesteBat").value; // Joaneko data

      if (hasieraData < today) {
        formularioOsorik = false;
        Swal.fire(
          "Errorea!",
          "Hasiera data ezin da izan gaurko data baino lehenagokoa.",
          "error",
        );
        return;
      }
    }

    const bidaiaMotaValue = document.getElementById("bidaiaMota").value;
    if (bidaiaMotaValue) {
      datuak["bidaiaMota"] = bidaiaMotaValue;
    }

    for (const input of inputs) {
      const izena = input.name;
      const balioa = input.value.trim();

      if (!balioa) {
        formularioOsorik = false;
        Swal.fire("Errorea!", "Datu Guztiak Bete.", "error");
        return;
      }

      if (input.type === "text" && balioa.length < 3) {
        formularioOsorik = false;
        Swal.fire("Errorea!", "Gutxienez 3 hizki idatzi.", "error");
        return;
      }

      if (input.type === "number" && parseFloat(balioa) <= 0) {
        formularioOsorik = false;
        Swal.fire("Errorea!", "Zenbaki positiboak sartu.", "error");
        return;
      }

      if (izena) {
        datuak[izena] = balioa;
      }
    }

    if (formularioOsorik) {
      formularioAurkitua = {
        idFormulario: formulario.id,
        datuak,
      };
    }
  }

  if (!formularioAurkitua) {
    Swal.fire("Errorea!", "Datu Guztiak Bete.", "error");
    return;
  }

  if (formulario.id === "form-hegaldia") {
    const taulaJ = document.querySelector("#taulaJ");
    if (taulaJ) {
      taulaJ.classList.remove("hidden");
      let body = taulaJ.tBodies[0];
      let lerroa = body.insertRow();
      let g1 = lerroa.insertCell();
      g1.innerText = "Joan";
      let g2 = lerroa.insertCell();
      g2.innerText = datuak.jatorria;
      let g3 = lerroa.insertCell();
      g3.innerText = datuak.helmuga;
      let g4 = lerroa.insertCell();
      g4.innerText = datuak.hegaldiaKodea;
      let g5 = lerroa.insertCell();
      g5.innerText = datuak.prezioahegaldia;
      let g6 = lerroa.insertCell();
      g6.innerText = datuak.data;
      let g7 = lerroa.insertCell();
      g7.innerText = datuak.ordua;
      let g8 = lerroa.insertCell();
      g8.innerText = datuak.denboraOrduak;
    }
  } else if (formulario.id === "form-ostatua") {
    const taulaO = document.querySelector("#taulaO");
    if (taulaO) {
      taulaO.classList.remove("hidden");
      let body = taulaO.tBodies[0];
      let lerroa = body.insertRow();
      let g1 = lerroa.insertCell();
      g1.innerText = "Ostatua";
      let g2 = lerroa.insertCell();
      g2.innerText = datuak.hotelIzena;
      let g3 = lerroa.insertCell();
      g3.innerText = datuak.ostatuHiria;
      let g4 = lerroa.insertCell();
      g4.innerText = datuak.prezioaostatua;
      let g5 = lerroa.insertCell();
      g5.innerText = datuak.dataJoan;
      let g6 = lerroa.insertCell();
      g6.innerText = datuak.dataEtorri;
      let g7 = lerroa.insertCell();
      g7.innerText = datuak.logelamota;
    }
  } else if (formulario.id === "form-joan-etorri") {
    const taulaJE = document.querySelector("#taulaJE");
    if (taulaJE) {
      taulaJE.classList.remove("hidden");
      let body = taulaJE.tBodies[0];
      let lerroa = body.insertRow();
      let g1 = lerroa.insertCell();
      g1.innerText = "Joan-Etorri";
      let g2 = lerroa.insertCell();
      g2.innerText = datuak.jatorria;
      let g3 = lerroa.insertCell();
      g3.innerText = datuak.helmuga;
      let g4 = lerroa.insertCell();
      g4.innerText = datuak.hegaldiaKodea;
      let g5 = lerroa.insertCell();
      g5.innerText = datuak.prezioahegaldia;
      let g6 = lerroa.insertCell();
      g6.innerText = datuak.data;
      let g7 = lerroa.insertCell();
      g7.innerText = datuak.ordua;
      let g8 = lerroa.insertCell();
      g8.innerText = datuak.denboraOrduak;
      let g9 = lerroa.insertCell();
      g9.innerText = datuak.itzuleraData;
      let g10 = lerroa.insertCell();
      g10.innerText = datuak.itzuleraOrdua;
      let g11 = lerroa.insertCell();
      g11.innerText = datuak.itzuleraOrduak;
      let g12 = lerroa.insertCell();
      g12.innerText = datuak.bueltakoHegaldiaKodea;
      let g13 = lerroa.insertCell();
      g13.innerText = datuak.bueltakoAirelinea;
    }
  } else if (formulario.id === "form-beste-bat") {
    const taulaB = document.querySelector("#taulaB");
    if (taulaB) {
      taulaB.classList.remove("hidden");
      let body = taulaB.tBodies[0];
      let lerroa = body.insertRow();
      let g1 = lerroa.insertCell();
      g1.innerText = "Beste Bat";
      let g2 = lerroa.insertCell();
      g2.innerText = datuak.bidaiaMota;
      let g3 = lerroa.insertCell();
      g3.innerText = datuak.zerbitzuIzena;
      let g4 = lerroa.insertCell();
      g4.innerText = datuak.data;
      let g5 = lerroa.insertCell();
      g5.innerText = datuak.deskribapena;
      let g6 = lerroa.insertCell();
      g6.innerText = datuak.kostua;
    }
  }

  const formData = new FormData();
  formData.append("idFormulario", formularioAurkitua.idFormulario);
  for (const key in formularioAurkitua.datuak) {
    formData.append(key, formularioAurkitua.datuak[key]);
  }

  try {
    const response = await fetch("../php/insertZerbitzuak.php", {
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
