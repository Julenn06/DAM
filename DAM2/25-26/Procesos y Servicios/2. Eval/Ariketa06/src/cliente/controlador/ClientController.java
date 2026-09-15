package cliente.controlador;

import cliente.modelo.ClientModel;
import cliente.vista.ClientViewChat;
import cliente.vista.ClientViewLogin;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ClientController implements ClientModel.ClientModelListener {
	private ClientModel modelo;
	private ClientViewLogin vistaLogin;
	private ClientViewChat vistaChat;
	private String nickname;

	public ClientController() {
		modelo = new ClientModel(this);
		mostrarLogin();
	}

	private void mostrarLogin() {
		vistaLogin = new ClientViewLogin(null);
		vistaLogin.setVisible(true);

		if (vistaLogin.isConectar()) {
			nickname = vistaLogin.getNickname();
			if (modelo.conectar(nickname)) {
				mostrarChat();
			} else {
				JOptionPane.showMessageDialog(null, "Ezin da zerbitzariarekin konektatu", "Errorea",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void mostrarChat() {
		vistaChat = new ClientViewChat(nickname);

		// Listeners konfiguratu
		vistaChat.getBtnEnviar().addActionListener(e -> enviarMensaje());
		vistaChat.getTxtMensaje().addActionListener(e -> enviarMensaje());
		vistaChat.getBtnSalir().addActionListener(e -> salir());

		vistaChat.setVisible(true);
	}

	private void enviarMensaje() {
		String mensaje = vistaChat.obtenerMensaje();
		if (!mensaje.isEmpty()) {
			modelo.enviarMensaje(mensaje);
			vistaChat.limpiarCampoMensaje();
		}
	}

	private void salir() {
		int confirmacion = JOptionPane.showConfirmDialog(vistaChat, "Ziur txatetik irten nahi duzula?", "Irten",
				JOptionPane.YES_NO_OPTION);

		if (confirmacion == JOptionPane.YES_OPTION) {
			modelo.desconectar();
			vistaChat.dispose();
			System.exit(0);
		}
	}

	@Override
	public void onMensajeRecibido(String mensaje) {
		SwingUtilities.invokeLater(() -> {
			vistaChat.agregarMensaje(mensaje);
		});
	}

	@Override
	public void onServidorCerrado() {
		SwingUtilities.invokeLater(() -> {
			vistaChat.agregarMensaje("*** Zerbitzaria itxi da ***");
			vistaChat.deshabilitarEnvio();
			JOptionPane.showMessageDialog(vistaChat, "Zerbitzaria itxi da", "Informazioa",
					JOptionPane.INFORMATION_MESSAGE);
		});
	}

	@Override
	public void onError(String error) {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(vistaChat != null ? vistaChat : null, error, "Errorea",
					JOptionPane.ERROR_MESSAGE);
		});
	}
}
