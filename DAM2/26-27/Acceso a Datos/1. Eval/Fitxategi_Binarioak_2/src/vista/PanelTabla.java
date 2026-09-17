package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.Partido;

public class PanelTabla extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tabla;
	private DefaultTableModel modeloTabla;

	public PanelTabla() {
		setLayout(new BorderLayout());
		String[] columnas = { "Equipo Local", "Equipo Visitante", "Goles Local", "Goles Visitante", "Lugar", "Fecha" };
		modeloTabla = new DefaultTableModel(columnas, 0);
		tabla = new JTable(modeloTabla);
		JScrollPane scrollPane = new JScrollPane(tabla);
		scrollPane.setPreferredSize(new Dimension(400, 150));
		add(scrollPane, BorderLayout.CENTER);
	}

	public void actualizarTabla(ArrayList<Partido> partidos) {
		modeloTabla.setRowCount(0);
		for (Partido p : partidos) {
			modeloTabla.addRow(new Object[] { p.getEquipoLocal(), p.getEquipoVisitante(), p.getGolesLocal(),
					p.getGolesVisitante(), p.getLugar(), p.getFecha() });
		}
	}
}
