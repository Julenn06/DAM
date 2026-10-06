//==========Background Kolorea Ilunara Aldatzeko==========//

const argiaIluna = document.getElementById("argiaIluna");
const gorputza = document.body;
const formulario = document.querySelector(".bidaia-formularioa form");
const formularioZ = document.querySelector(".zerbitzua-form form");

argiaIluna.addEventListener("click", () => {
  const eguzkiAldaketaAktibatuta =
    gorputza.style.backgroundColor === "rgb(34, 34, 34)";

  if (eguzkiAldaketaAktibatuta) {
    gorputza.style.backgroundColor = "#f5f5f5";
    gorputza.style.color = "#000000";
    if (formulario) {
      formulario.style.backgroundColor = "#f5f5f5";
      formulario.style.color = "#000000";
    }
    if (formularioZ) {
      formularioZ.style.backgroundColor = "#f5f5f5";
      formularioZ.style.color = "#000000";
    }
    argiaIluna.classList.remove("iluna");
    localStorage.setItem("modoOscuro", "false");
  } else {
    gorputza.style.backgroundColor = "#222";
    gorputza.style.color = "#ffffff";
    if (formulario) {
      formulario.style.backgroundColor = "#333";
      formulario.style.color = "#ffffff";
    }
    if (formularioZ) {
      formularioZ.style.backgroundColor = "#333";
      formularioZ.style.color = "#ffffff";
    }
    argiaIluna.classList.add("iluna");
    localStorage.setItem("modoOscuro", "true");
  }
});

window.addEventListener("load", () => {
  const iluna = localStorage.getItem("modoOscuro");
  if (iluna === "true") {
    gorputza.style.backgroundColor = "#222";
    gorputza.style.color = "#ffffff";
    argiaIluna.classList.add("iluna");
    if (formulario) {
      formulario.style.backgroundColor = "#333";
      formulario.style.color = "#ffffff";
    }
    if (formularioZ) {
      formularioZ.style.backgroundColor = "#333";
      formularioZ.style.color = "#ffffff";
    }
  } else {
    gorputza.style.backgroundColor = "#f5f5f5";
    gorputza.style.color = "#000000";
    argiaIluna.classList.remove("iluna");
    if (formulario) {
      formulario.style.backgroundColor = "#f5f5f5";
      formulario.style.color = "#000000";
    }
    if (formularioZ) {
      formularioZ.style.backgroundColor = "#f5f5f5";
      formularioZ.style.color = "#000000";
    }
  }
});

//==========Formularioak Erakutsi==========//

function formularioakIkusi(idFormulario) {
  const formularioa = document.querySelectorAll('div[id^="form-"]');
  formularioa.forEach((formulario) => formulario.classList.add("hidden"));

  const formularioAukeratuta = document.getElementById(idFormulario);
  formularioAukeratuta.classList.remove("hidden");

  formularioa.forEach((form) => {
    const inputs = form.querySelectorAll("input, select, textarea");
    inputs.forEach((input) => {
      input.value = "";
    });
  });
}

//==========Hegaldi Mota Botoiak Okultatu==========//

document.addEventListener("DOMContentLoaded", function () {
  const aukeraPrintzipalak = document.querySelectorAll("input[name='opcion']");
  const subAukerkak = document.querySelectorAll("input[name='hegaldiMota']");

  aukeraPrintzipalak.forEach((opcion) => {
    opcion.addEventListener("change", function () {
      if (this.value !== "hegaldia") {
        document.getElementById("hegaldia-joanetorri").classList.add("hidden");
        subAukerkak.forEach((radio) => {
          radio.checked = false;
        });
      }
    });
  });
});

//==========Saioa Itxi==========//

function saioaItxi() {
  Swal.fire({
    title: "Saioa itxi nahi duzu?",
    text: "Ekintza hau ezin da desegin!",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "Bai, itxi!",
    cancelButtonText: "Ez, utzi horrela",
  }).then((result) => {
    if (result.isConfirmed) {
      window.location.href = "../php/logout.php";
    }
  });
}
