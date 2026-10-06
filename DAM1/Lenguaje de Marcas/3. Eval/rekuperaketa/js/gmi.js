document
  .querySelector('button[type="button"]')
  .addEventListener("click", function () {
    const izena = document.getElementById("izena").value;
    const altuera = parseFloat(document.getElementById("altuera").value) / 100;
    const pisua = parseFloat(document.getElementById("pisua").value);

    if (
      izena == "" ||
      isNaN(altuera) ||
      isNaN(pisua) ||
      altuera <= 0 ||
      pesoa <= 0
    ) {
      alert("Sartu balio egokiak.");
      return;
    }

    const gmi = pisua / (altuera * altuera);
    const gmiElementua = document.getElementById("gmi");
    gmiElementua.value = gmi.toFixed(2);

    if (gmi < 18.5) {
      gmiElementua.style.background = "red";
    } else if (gmi >= 18.5 && gmi <= 24.9) {
      gmiElementua.style.background = "green";
    } else if (gmi >= 25.0 && gmi <= 29.9) {
      gmiElementua.style.background = "yellow";
    } else {
      gmiElementua.style.background = "red";
    }
  });

document
  .querySelector('button[type="submit"]')
  .addEventListener("click", function () {
    document.getElementById("gmi").value = "";
    document.getElementById("gmi").style.background = "white";
    document.getElementById("izena").value = "";
    document.getElementById("altuera").value = "";
    document.getElementById("pesoa").value = "";
  });
