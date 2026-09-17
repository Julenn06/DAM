package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelFormulario extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField txtLocal, txtVisitante, txtGolesL, txtGolesV, txtLugar, txtFecha;
	private JButton btnAñadir, btnCargar, btnGuardar;

	public PanelFormulario() {
		// Usamos BorderLayout para separar los campos del formulario de los botones de
		// acción
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Subpanel exclusivo para la cuadrícula de datos (6 filas x 2 columnas)
		JPanel panelCampos = new JPanel(new GridLayout(6, 2, 8, 8));

		panelCampos.add(new JLabel("Equipo Local:"));
		txtLocal = new JTextField();
		panelCampos.add(txtLocal);

		panelCampos.add(new JLabel("Equipo Visitante:"));
		txtVisitante = new JTextField();
		panelCampos.add(txtVisitante);

		panelCampos.add(new JLabel("Goles Local:"));
		txtGolesL = new JTextField();
		panelCampos.add(txtGolesL);

		panelCampos.add(new JLabel("Goles Visitante:"));
		txtGolesV = new JTextField();
		panelCampos.add(txtGolesV);

		panelCampos.add(new JLabel("Lugar:"));
		txtLugar = new JTextField();
		panelCampos.add(txtLugar);

		panelCampos.add(new JLabel("Fecha (dd/MM/yy):"));
		txtFecha = new JTextField();
		panelCampos.add(txtFecha);

		// Componentes de control
		btnAñadir = new JButton("Añadir");
		btnCargar = new JButton("Cargar");
		btnGuardar = new JButton("Guardar");

		// Subpanel horizontal para albergar todos los botones alineados
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
		panelBotones.add(btnAñadir);
		panelBotones.add(btnCargar);
		panelBotones.add(btnGuardar);

		// Añadimos las zonas al contenedor principal de la vista del formulario
		add(panelCampos, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
	}

	// Getters de los componentes para el Controlador
	public JButton getBtnAñadir() {
		return btnAñadir;
	}

	public JButton getBtnCargar() {
		return btnCargar;
	}

	public JButton getBtnGuardar() {
		return btnGuardar;
	}

	public String getLocal() {
		return txtLocal.getText().trim();
	}

	public String getVisitante() {
		return txtVisitante.getText().trim();
	}

	public String getGolesL() {
		return txtGolesL.getText().trim();
	}

	public String getGolesV() {
		return txtGolesV.getText().trim();
	}

	public String getLugar() {
		return txtLugar.getText().trim();
	}

	public String getFecha() {
		return txtFecha.getText().trim();
	}

	public void limpiarCampos() {
		txtLocal.setText("");
		txtVisitante.setText("");
		txtGolesL.setText("");
		txtGolesV.setText("");
		txtLugar.setText("");
		txtFecha.setText("");
	}
}
