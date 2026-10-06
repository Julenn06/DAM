function eragiketa() {
  let aukera = document.getElementsByName("produktua");
  let kantitatea = document.getElementById("kantitatea").value;
  let balidazioa = true;
  let hautatutakoPrezioa = 0;
  let produktuaBalidatu = false;

  if (kantitatea == null || kantitatea == 0) {
    window.alert("Aukeratu kantitatea");
    balidazioa = false;
  } else if (kantitatea < 0) {
    window.alert("Aukeratu kantitate positibo bat");
    balidazioa = false;
  }

  for (let i = 0; i < aukera.length; i++) {
    if (aukera[i].checked) {
      hautatutakoPrezioa = parseFloat(aukera[i].value);
      produktuaBalidatu = true;
      break;
    }
  }

  if (produktuaBalidatu == false) {
    window.alert("Aukeratu Produktu Bat");
    balidazioa = false;
  }

  if (balidazioa == true) {
    let ordainketaMota = document.getElementById("ordainketa_metodoa").value;
    let emaitza = document.getElementById("emaitza");
    let total = hautatutakoPrezioa * kantitatea;
    let garraioa = document.getElementById("garraioa").checked;

    if (ordainketaMota == "dirua") {
      total = total * 0.9;
    } else if (ordainketaMota == "hile1") {
      total = total * 0.95;
    }
    if (garraioa == true) {
      total = total + 10;
    }

    emaitza.value = `${total.toFixed(2)} €`;
  }
}
