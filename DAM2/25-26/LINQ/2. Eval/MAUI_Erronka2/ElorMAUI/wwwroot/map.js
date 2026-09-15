window.createMap = function (lat, lon, nombre, ccen, html) {
    var map = L.map('map').setView([lat, lon], 13); // Crea el mapa centrado en las coordenadas

    // Capa de OpenStreetMap
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    // Crea el marcador en el mapa
    var marker = L.marker([lat, lon]).addTo(map);

    // Asigna el HTML recibido desde Blazor como contenido del popup
    marker.bindPopup(html);

    // Abre el popup al hacer click en el marcador
    marker.on('click', function () {
        marker.openPopup();
    });
};
