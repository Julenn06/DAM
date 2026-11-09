package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.HistorikoaItem;

public class HistorikoaTaula extends BasePanel {
	public DefaultTableModel getModeloTaula() {
		return modeloTaula;
	}

	public List<HistorikoaItem> getUnekoHistorikoak() {
		return unekoHistorikoak;
	}

	private static final long serialVersionUID = 1L;
	private JTable taula;
	private DefaultTableModel modeloTaula;
	private List<HistorikoaItem> unekoHistorikoak = new ArrayList<>();

	public HistorikoaTaula(Lehioa lehioa) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights = new double[] { 1.0 };
		setLayout(gridBagLayout);

		String[] columnas = { "Workout Izena", "Maila", "Denbora Osoa", "Aurreikusitako Denbora", "Data",
				"% Amaituta" };

		modeloTaula = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			private static final long serialVersionUID = 1L;

		};

		taula = new JTable(modeloTaula);
		testuMotaAplikatuTaula(taula);

		JScrollPane scrollPane = new JScrollPane(taula);
		scrollPane.setPreferredSize(new Dimension(400, 150));
		testuMotaAplikatu(scrollPane);
		testuMotaAplikatu(taula.getTableHeader());

		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		gbc_scrollPane.weightx = 1.0;
		gbc_scrollPane.weighty = 1.0;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		add(scrollPane, gbc_scrollPane);
	}

	public JTable getTaula() {
		return taula;
	}

	public HistorikoaItem getWorkoutAt(int index) {
		if (index >= 0 && index < unekoHistorikoak.size()) {
			return unekoHistorikoak.get(index);
		}
		return null;
	}
}
