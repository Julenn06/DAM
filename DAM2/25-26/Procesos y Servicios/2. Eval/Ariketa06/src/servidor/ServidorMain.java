package servidor;

import servidor.controlador.ServerController;
import javax.swing.SwingUtilities;

public class ServidorMain {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new ServerController();
		});
	}
}
