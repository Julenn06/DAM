package vista;

import javax.swing.SwingUtilities;

import controlador.Controlador;
import modelo.GestorResultados;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			GestorResultados modelo = new GestorResultados();
			VentanaPrincipal vista = new VentanaPrincipal();
			new Controlador(vista, modelo);

			vista.setVisible(true);
		});
	}
}
