function izkutatu() {
  document.getElementById("p1").style.visibility = "hidden";
}

function erakutsi() {
  document.getElementById("p1").style.visibility = "visible";
}

function izkutatu2() {
  let elementua = (document.getElementsByTagName("p")[0].style.visibility =
    "hidden");
  console.log(elementua);
}

function erakutsi2() {
  let elementua = (document.getElementsByTagName("p")[0].style.visibility =
    "visible");
  console.log(elementua);
}

function izkutatu3() {
  document.querySelector("p").style.display = "none";
}

function erakutsi3() {
  document.querySelector("p").style.display = "block";
}
