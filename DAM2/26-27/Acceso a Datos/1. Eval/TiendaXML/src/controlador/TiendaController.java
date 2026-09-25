package controlador;

import modelo.TiendaModel;
import vista.TiendaView;

public class TiendaController {
	private TiendaModel model;
	private TiendaView view;

	public TiendaController(TiendaModel model, TiendaView view) {
		this.model = model;
		this.view = view;
	}

	public void ejecutarConsultas() {
		try {
			view.mostrarSeparador();
			System.out.println("            RESULTADOS DE LAS CONSULTAS XPATH (MVC)");
			view.mostrarSeparador();

			// 1. Unidades vendidas de "carne"
			view.mostrarVentasPorCategoria("carne", model.getVentasPorCategoria("carne"));

			// 2. Productos baratos (< 1.50)
			view.mostrarProductosBaratos(1.50, model.getProductosBaratos(1.50));

			// 3. Ventas superiores a 5 unidades
			view.mostrarProductosMasVendidos(5, model.getProductosMasVendidos(5));

			// 4. Responsable de "Naranjas"
			view.mostrarResponsableDelProducto("Naranjas", model.getResponsableDelProducto("Naranjas"));

			// 5. Responsable de venta en fecha "2013-05-05"
			view.mostrarResponsablesDeLaVenta("2013-05-05", model.getResponsablesDeLaVenta("2013-05-05"));

			// 6. Productos con stock menor a 20
			view.mostrarProductosPocoStock(20, model.getProductosPocoStock(20));

			// 7. Departamento de "Iker Zabala"
			view.mostrarDptoDelResponsable("Iker Zabala", model.getDptoDelResponsable("Iker Zabala"));

			view.mostrarSeparador();

		} catch (Exception e) {
			view.mostrarMensajeError("Hubo un fallo al procesar las consultas XPath: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
