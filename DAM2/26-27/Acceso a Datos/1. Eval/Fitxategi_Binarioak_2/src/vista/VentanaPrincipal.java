package vista;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class VentanaPrincipal extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelFormulario panelFormulario;
	private PanelTabla panelTabla;
	private PanelEstadisticas panelEstadisticas;

	public VentanaPrincipal() {
		setTitle("Gestión de Resultados");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(480, 620);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		panelFormulario = new PanelFormulario();
		panelTabla = new PanelTabla();
		panelEstadisticas = new PanelEstadisticas();

		add(panelFormulario, BorderLayout.NORTH);
		add(panelTabla, BorderLayout.CENTER);
		add(panelEstadisticas, BorderLayout.SOUTH);
	}

	public PanelFormulario getPanelFormulario() {
		return panelFormulario;
	}

	public PanelTabla getPanelTabla() {
		return panelTabla;
	}

	public PanelEstadisticas getPanelEstadisticas() {
		return panelEstadisticas;
	}
}
