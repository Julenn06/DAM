package cliente.vista;

import javax.swing.*;
import java.awt.*;

public class ClientViewChat extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtMensaje;
	private JTextArea txtAreaChat;
	private JButton btnEnviar;
	private JButton btnSalir;

	public ClientViewChat(String nickname) {
		setTitle("Txat - " + nickname);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(450, 350);
		setResizable(false);
		setLocationRelativeTo(null);

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		// Panel goikoa - Testu-eremua eta bidali botoia
		JPanel panelSuperior = new JPanel(new BorderLayout());
		txtMensaje = new JTextField();
		btnEnviar = new JButton("Bidali");
		panelSuperior.add(txtMensaje, BorderLayout.CENTER);
		panelSuperior.add(btnEnviar, BorderLayout.EAST);
		add(panelSuperior, BorderLayout.NORTH);

		// Panel zentrala - TextArea txaterako
		txtAreaChat = new JTextArea();
		txtAreaChat.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(txtAreaChat);
		add(scrollPane, BorderLayout.CENTER);

		// Panel behekoa - Irten botoia
		JPanel panelInferior = new JPanel();
		btnSalir = new JButton("Irten");
		panelInferior.add(btnSalir);
		add(panelInferior, BorderLayout.SOUTH);
	}

	public void agregarMensaje(String mensaje) {
		txtAreaChat.append(mensaje + "\n");
		txtAreaChat.setCaretPosition(txtAreaChat.getDocument().getLength());
	}

	public String obtenerMensaje() {
		return txtMensaje.getText().trim();
	}

	public void limpiarCampoMensaje() {
		txtMensaje.setText("");
	}

	public void deshabilitarEnvio() {
		txtMensaje.setEnabled(false);
		btnEnviar.setEnabled(false);
	}

	public JTextField getTxtMensaje() {
		return txtMensaje;
	}

	public JButton getBtnEnviar() {
		return btnEnviar;
	}

	public JButton getBtnSalir() {
		return btnSalir;
	}
}
