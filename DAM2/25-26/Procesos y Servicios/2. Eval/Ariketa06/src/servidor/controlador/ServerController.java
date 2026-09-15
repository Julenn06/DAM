package servidor.controlador;

import servidor.modelo.ServerModel;
import servidor.vista.ServerView;
import javax.swing.SwingUtilities;

public class ServerController implements ServerModel.ServerModelListener {
	private ServerView vista;
	private ServerModel modelo;

	public ServerController() {
		vista = new ServerView();
		modelo = new ServerModel(this);

		// Listeners konfiguratu
		vista.getBtnSalir().addActionListener(e -> salir());

		// Zerbitzaria hasi
		modelo.start();

		// Bista erakutsi
		vista.setVisible(true);
	}

	private void salir() {
		int confirmacion = javax.swing.JOptionPane.showConfirmDialog(vista,
				"Ziur zaude zerbitzaria itxi nahi duzula?", "Irteera baieztatu",
				javax.swing.JOptionPane.YES_NO_OPTION);

		if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
			modelo.desconectar();
			vista.dispose();
			System.exit(0);
		}
	}

	@Override
	public void onClienteConectado(String nickname, int numClientes) {
		SwingUtilities.invokeLater(() -> {
			vista.agregarMensaje(nickname + " txatean sartzen da");
			vista.actualizarNumeroClientes(numClientes);
		});
	}

	@Override
	public void onClienteDesconectado(String nickname, int numClientes) {
		SwingUtilities.invokeLater(() -> {
			vista.agregarMensaje(nickname + "-ek txatetik irten da");
			vista.actualizarNumeroClientes(numClientes);
		});
	}

	@Override
	public void onMensaje(String mensaje) {
		SwingUtilities.invokeLater(() -> {
			vista.agregarMensaje(mensaje);
		});
	}
}
