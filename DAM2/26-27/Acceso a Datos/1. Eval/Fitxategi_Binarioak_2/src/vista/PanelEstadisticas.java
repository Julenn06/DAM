package vista;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelEstadisticas extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblResumen;

	public PanelEstadisticas() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBackground(new Color(200, 230, 201));
		lblResumen = new JLabel("Estadísticas en memoria — 0 partidos — 0-0 goles totales (local-visitante)");
		add(lblResumen);
	}

	public void actualizarResumen(int partidos, int golesL, int golesV) {
		lblResumen
				.setText(String.format("Estadísticas en memoria — %d partidos — %d-%d goles totales (local-visitante)",
						partidos, golesL, golesV));
	}
}
