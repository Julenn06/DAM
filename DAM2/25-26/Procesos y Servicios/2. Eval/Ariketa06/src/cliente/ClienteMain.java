package cliente;

import cliente.controlador.ClientController;
import javax.swing.SwingUtilities;

public class ClienteMain {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new ClientController();
		});
	}
}
