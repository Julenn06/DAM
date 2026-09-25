package vista;

import java.util.List;

public class TiendaView {

	public void mostrarMensajeError(String mensaje) {
		System.err.println("❌ ERROR: " + mensaje);
	}

	public void mostrarSeparador() {
		System.out.println("----------------------------------------------------------------------");
	}

	public void mostrarVentasPorCategoria(String categoria, double total) {
		System.out.printf("1. Suma de unidades vendidas de la categoría '%s': %.0f unidades.\n", categoria, total);
	}

	public void mostrarProductosBaratos(double precio, List<String> productos) {
		System.out.printf("2. Productos con precio inferior a %.2f€: %s\n", precio,
				productos.isEmpty() ? "Ninguno" : productos);
	}

	public void mostrarProductosMasVendidos(int cantidad, List<String> productos) {
		System.out.printf("3. Productos con más de %d unidades vendidas en una sola venta: %s\n", cantidad,
				productos.isEmpty() ? "Ninguno" : productos);
	}

	public void mostrarResponsableDelProducto(String producto, String responsable) {
		System.out.printf("4. Responsable del dpto. del producto '%s': %s\n", producto,
				responsable.isBlank() ? "No encontrado" : responsable);
	}

	public void mostrarResponsablesDeLaVenta(String fecha, List<String> responsables) {
		System.out.printf("5. Responsable(s) de la venta del día %s: %s\n", fecha,
				responsables.isEmpty() ? "No hubo ventas" : responsables);
	}

	public void mostrarProductosPocoStock(int limiteStock, double total) {
		System.out.printf("6. Número de productos con menos de %d unidades en stock: %.0f productos.\n", limiteStock,
				total);
	}

	public void mostrarDptoDelResponsable(String responsable, String dpto) {
		System.out.printf("7. Nombre del departamento cuyo responsable es '%s': %s\n", responsable,
				dpto.isBlank() ? "No encontrado" : dpto);
	}
}
