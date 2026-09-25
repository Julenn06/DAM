package vista;

import controlador.TiendaController;
import modelo.TiendaModel;

public class Main {
	public static void main(String[] args) {
		String rutaXML = "tienda_intermedia.xml";
		TiendaView vista = new TiendaView();

		try {
			// Inicialización de componentes siguiendo el patrón MVC
			TiendaModel modelo = new TiendaModel(rutaXML);
			TiendaController controlador = new TiendaController(modelo, vista);

			// El controlador coordina y ejecuta la lógica completa
			controlador.ejecutarConsultas();

		} catch (Exception e) {
			vista.mostrarMensajeError("No se pudo iniciar la aplicación o cargar el archivo XML: " + e.getMessage());
		}
	}
}