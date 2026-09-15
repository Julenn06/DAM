package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public final class AstekoOrdutegia extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final Color KLASE_ARRUNTA = new Color(70, 130, 180);
	public static final Color BILERA_PROGRAMATUA = new Color(34, 139, 34);
	public static final Color BILERA_EZEZTUA = new Color(220, 53, 69);
	public static final Color DENBORA_LIBRE = new Color(240, 240, 240);
	public static final Color GELAXKA_HAUTATUA = new Color(255, 215, 0);

	private final JTable tabla;
	private final DefaultTableModel taulaModeloa;
	private final Map<String, Color> gelaxkaKoloreak;

	private final String[] denboraEremuak = { "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-11:30", "11:30-12:30",
			"12:30-13:30", "13:30-14:30", "14:30-15:30" };

	private final String[] egunak = { "Ordua", "Astelehena", "Asteartea", "Asteazkena", "Osteguna", "Ostirala" };

	public AstekoOrdutegia() {
		gelaxkaKoloreak = new HashMap<>();

		setLayout(new BorderLayout());

		taulaModeloa = new DefaultTableModel(egunak, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column != 0;
			}
		};

		for (String denboraEremua : denboraEremuak) {
			Object[] errenkada = new Object[6];
			errenkada[0] = denboraEremua;
			for (int i = 1; i < 6; i++) {
				errenkada[i] = "";
			}
			taulaModeloa.addRow(errenkada);
		}

		tabla = new JTable(taulaModeloa);
		konfiguratuTabla();

		add(sortuGoiburua(), BorderLayout.NORTH);
		add(sortuTaulaPanela(), BorderLayout.CENTER);
		add(sortuLegendaPanela(), BorderLayout.SOUTH);
	}

	private void konfiguratuTabla() {
		tabla.setRowHeight(40);
		tabla.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tabla.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
		tabla.getTableHeader().setBackground(new Color(51, 102, 153));
		tabla.getTableHeader().setForeground(Color.WHITE);

		TableColumnModel zutabeModeloa = tabla.getColumnModel();
		zutabeModeloa.getColumn(0).setPreferredWidth(100);
		for (int i = 1; i < 6; i++) {
			zutabeModeloa.getColumn(i).setPreferredWidth(150);
		}

		tabla.setDefaultRenderer(Object.class, new GelaxkaErrendatzailea());
	}

	private JLabel sortuGoiburua() {
		JLabel izenburua = new JLabel("Asteko Ordutegia", JLabel.CENTER);
		izenburua.setFont(new Font("Tahoma", Font.BOLD, 18));
		izenburua.setForeground(new Color(51, 102, 153));
		izenburua.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		return izenburua;
	}

	private JScrollPane sortuTaulaPanela() {
		JScrollPane scrollPane = new JScrollPane(tabla);
		scrollPane.setPreferredSize(new Dimension(800, 350));
		return scrollPane;
	}

	private JPanel sortuLegendaPanela() {
		JPanel legendaPanel = new JPanel(new FlowLayout());
		legendaPanel.setBorder(BorderFactory.createTitledBorder("Legenda"));

		legendaPanel.add(sortuLegendaElementua("Klase Arrunta", KLASE_ARRUNTA));
		legendaPanel.add(sortuLegendaElementua("Bilera Programatua", BILERA_PROGRAMATUA));
		legendaPanel.add(sortuLegendaElementua("Bilera Ezeztua", BILERA_EZEZTUA));
		legendaPanel.add(sortuLegendaElementua("Denbora Librea", DENBORA_LIBRE));

		return legendaPanel;
	}

	private JPanel sortuLegendaElementua(String testua, Color kolorea) {
		JPanel elementua = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

		JPanel koloreKoadroa = new JPanel();
		koloreKoadroa.setBackground(kolorea);
		koloreKoadroa.setPreferredSize(new Dimension(15, 15));
		koloreKoadroa.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		JLabel etiketa = new JLabel(testua);
		etiketa.setFont(new Font("Tahoma", Font.PLAIN, 11));

		elementua.add(koloreKoadroa);
		elementua.add(etiketa);

		return elementua;
	}

	public void ezarriGelaxkaEdukia(int eguna, int denboraIndizea, String edukia) {
		if (eguna >= 1 && eguna <= 5 && denboraIndizea >= 0 && denboraIndizea < denboraEremuak.length) {
			taulaModeloa.setValueAt(edukia, denboraIndizea, eguna);
		}
	}

	public void ezarriGelaxkaKolorea(int eguna, int denboraIndizea, Color kolorea) {
		if (eguna >= 1 && eguna <= 5 && denboraIndizea >= 0 && denboraIndizea < denboraEremuak.length) {
			String gakoa = denboraIndizea + "," + eguna;
			gelaxkaKoloreak.put(gakoa, kolorea);
			tabla.repaint();
		}
	}

	public void garbiOrdutegia() {
		for (int i = 0; i < denboraEremuak.length; i++) {
			for (int j = 1; j <= 5; j++) {
				taulaModeloa.setValueAt("", i, j);
			}
		}
		gelaxkaKoloreak.clear();
		tabla.repaint();
	}

	public void kargatuOrdutegiaData(String[][] ordutegiaData) {
		for (int i = 0; i < Math.min(ordutegiaData.length, denboraEremuak.length); i++) {
			for (int j = 0; j < Math.min(ordutegiaData[i].length, 5); j++) {
				ezarriGelaxkaEdukia(j + 1, i, ordutegiaData[i][j]);
			}
		}
	}

	public String lortuGelaxkaEdukia(int eguna, int denboraIndizea) {
		if (eguna >= 1 && eguna <= 5 && denboraIndizea >= 0 && denboraIndizea < denboraEremuak.length) {
			return (String) taulaModeloa.getValueAt(denboraIndizea, eguna);
		}
		return "";
	}

	public void ezarriOrdutegiIzenburua(String izenburua) {
		Component[] osagaiak = getComponents();
		for (Component comp : osagaiak) {
			if (comp instanceof JLabel) {
				((JLabel) comp).setText(izenburua);
				break;
			}
		}
	}

	private class GelaxkaErrendatzailea extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable tabla, Object balioa, boolean hautatua, boolean fokusDu,
				int errenkada, int zutabea) {

			Component c = super.getTableCellRendererComponent(tabla, balioa, hautatua, fokusDu, errenkada, zutabea);

			if (zutabea == 0) {
				c.setBackground(new Color(220, 220, 220));
				c.setForeground(Color.BLACK);
				setHorizontalAlignment(CENTER);
			} else {
				String gakoa = errenkada + "," + zutabea;
				Color gelaxkaKolorea = gelaxkaKoloreak.get(gakoa);

				if (gelaxkaKolorea != null) {
					c.setBackground(gelaxkaKolorea);
					c.setForeground(Color.WHITE);
				} else {
					c.setBackground(Color.WHITE);
					c.setForeground(Color.BLACK);
				}
				setHorizontalAlignment(CENTER);
			}

			if (hautatua && zutabea != 0) {
				c.setBackground(GELAXKA_HAUTATUA);
				c.setForeground(Color.BLACK);
			}

			setBorder(BorderFactory.createLineBorder(Color.GRAY));
			return c;
		}
	}
}