package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/**
 * Aplikazioko panelen oinarria.
 *
 * Panel guztiak estilo berean daude: letra mota bakarra (Tahoma 17), 5px kanpo
 * marra, GridBagLayout bat non edukia panelaren tamainarekin batera moldagarria
 * den, eta gutxienezko tamaina behar duen tamainarena.
 */
public abstract class BasePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/** Panel guztietan erabiliko den testu mota. */
	protected static final Font FONT = new Font("Tahoma", Font.PLAIN, 17);

	public BasePanel() {
		setFont(FONT);
		setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	/**
	 * Panelak behar duen gutxienezko tamaina hartzen du, berak ere bete beharreko
	 * tamaina baino txikiago ez daiteke.
	 *
	 * @return edukia osorik ikusteko beharrezko den tamaina
	 */
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	/**
	 * Testu mota osagai guztietan aplikatzen du, barnekoak barne
	 *
	 * @param component fonta aplikatuko zaion osagaia
	 */
	protected static void testuMotaAplikatu(Component component) {
		component.setFont(FONT);

		if (component instanceof Container) {
			for (Component child : ((Container) component).getComponents()) {
				testuMotaAplikatu(child);
			}
		}
	}

	/**
	 * Taularen testuak, buruzurbina eta lerroen altuerarekin batera testu mota
	 * berarekin lerrotzen ditu.
	 *
	 * @param taula testu mota aplikatuko zaion taula
	 */
	protected static void testuMotaAplikatuTaula(JTable taula) {
		taula.setFont(FONT);
		taula.getTableHeader().setFont(FONT);
		taula.setRowHeight(FONT.getSize() + 12);

		for (Class<?> klasea : new Class<?>[] { String.class, Object.class }) {
			TableCellRenderer renderer = taula.getDefaultRenderer(klasea);

			if (renderer instanceof Component) {
				((Component) renderer).setFont(FONT);
			}
		}
	}
}
