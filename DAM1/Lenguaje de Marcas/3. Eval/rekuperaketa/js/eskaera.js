function kalkulatu() {
  const balidazioa = balidatu();

  if (balidazioa) {
    const izena = document.getElementById("izena").value.trim();
    const telefonoa = document.getElementById("telefonoa").value.trim();
    const helbidea = document.getElementById("helbidea").value.trim();
    const cp = document.getElementById("cp").value.trim();
    const herria = document.getElementById("herria").value.trim();
    const prozesadorea = parseFloat(
      document.querySelector('input[name="prozesadorea"]:checked').value,
    );
    const memoria = parseFloat(
      document.querySelector('input[name="memoria"]:checked').value,
    );
    const biltegiratzea = parseFloat(
      document.querySelector('input[name="biltegiratzea"]:checked').value,
    );
    const elikatzeIturria = parseFloat(
      document.getElementById("complemento1").value,
    );

    let gehigarriak = 0;
    document.querySelectorAll(".gehigarria:checked").forEach((checkedBox) => {
      gehigarriak += parseFloat(checkedBox.value);
    });

    let prezioa =
      prozesadorea + memoria + biltegiratzea + elikatzeIturria + gehigarriak;

    let bezKalkulatu = (prezioa * 1.21).toFixed(2);

    const taula = document.querySelector("#taula");

    if (taula) {
      taula.classList.remove("hidden");
    }

    let body = taula.tBodies[0];
    let lerroa = body.insertRow();
    let g1 = lerroa.insertCell();
    g1.innerText =
      izena + " " + telefonoa + " " + helbidea + " " + cp + " " + herria;
    let g2 = lerroa.insertCell();
    g2.innerText =
      document
        .querySelector('input[name="prozesadorea"]:checked')
        .getAttribute("data-label") +
      ", " +
      document
        .querySelector('input[name="memoria"]:checked')
        .getAttribute("data-label") +
      ", " +
      document
        .querySelector('input[name="biltegiratzea"]:checked')
        .getAttribute("data-label") +
      ", " +
      document
        .getElementById("complemento1")
        .selectedOptions[0].getAttribute("data-label");
    let g3 = lerroa.insertCell();
    g3.innerText = prezioa + "€";
    let g4 = lerroa.insertCell();
    g4.innerText = bezKalkulatu + "€";
  }
}

function balidatu() {
  const izena = document.getElementById("izena").value.trim();
  const telefonoa = document.getElementById("telefonoa").value.trim();
  const dni = document.getElementById("dni").value.trim();
  const helbidea = document.getElementById("helbidea").value.trim();
  const cp = document.getElementById("cp").value.trim();
  const prozesadorea = document.querySelector(
    'input[name="prozesadorea"]:checked',
  );
  const memoria = document.querySelector('input[name="memoria"]:checked');
  const biltegiratzea = document.querySelector(
    'input[name="biltegiratzea"]:checked',
  );
  const elikatzeIturria = document.getElementById("complemento1").value;
  const bidali = document.getElementById("bidali").checked;

  if (
    !izena ||
    !telefonoa ||
    !dni ||
    !helbidea ||
    !cp ||
    !prozesadorea ||
    !memoria ||
    !biltegiratzea ||
    elikatzeIturria === "Ez da aukeratu" ||
    !bidali
  ) {
    alert("Bete derrigorrezko eremu guztiak.");
    return false;
  }
  if (telefonoa.length !== 9) {
    alert("Telefono zenbakia ez da zuzena.");
    return false;
  }
  if (dni.length !== 9 || !isNaN(dni[dni.length - 1])) {
    alert("DNI zenbakia ez da zuzena.");
    return false;
  }
  if (cp.length !== 5) {
    alert("Posta kodea ez da zuzena.");
    return false;
  }
  return true;
}
