package servidor.vista;

import javax.swing.*;
import java.awt.*;

public class ServerView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtNumClientes;
	private JTextArea txtAreaMensajes;
	private JButton btnSalir;

	public ServerView() {
		setTitle("Servidor - Txat Aurreratua");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(400, 400);
		setResizable(false);
		setLocationRelativeTo(null);

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

	// Panel goikoa - Bezero kopurua
	JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
	panelSuperior.add(new JLabel("Konektatutako bezeroak:"));
		txtNumClientes = new JTextField("0", 5);
		txtNumClientes.setEditable(false);
		panelSuperior.add(txtNumClientes);
		add(panelSuperior, BorderLayout.NORTH);

	// Panel zentrala - TextArea mezuentzat
		txtAreaMensajes = new JTextArea();
		txtAreaMensajes.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(txtAreaMensajes);
		add(scrollPane, BorderLayout.CENTER);

	// Panel behekoa - Irten botoia
		JPanel panelInferior = new JPanel();
		btnSalir = new JButton("Irten");
		panelInferior.add(btnSalir);
		add(panelInferior, BorderLayout.SOUTH);
	}

	public void actualizarNumeroClientes(int num) {
		txtNumClientes.setText(String.valueOf(num));
	}

	public void agregarMensaje(String mensaje) {
		txtAreaMensajes.append(mensaje + "\n");
		txtAreaMensajes.setCaretPosition(txtAreaMensajes.getDocument().getLength());
	}

	public JButton getBtnSalir() {
		return btnSalir;
	}
}
