package controller;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.DAOGetter;
import dao.interfazeak.IHistorikoaDAO;
import model.DAOException;
import model.HistorikoaItem;
import utils.TimeFormatter;
import view.HistorikoaPanel;
import view.HistorikoaTaula;
import view.Lehioa;

public class HistorikoaController {

	private HistorikoaPanel panel;
	private Lehioa lehioa;
	private IHistorikoaDAO dao;
	private HistorikoaTaula historikoaTaula;
	private URL imgURL;
	private JLabel lblLoginLogo;
	private JButton btnVolver;
	private DefaultTableModel modeloTaula;
	private List<HistorikoaItem> unekoHistorikoak = new ArrayList<>();

	public HistorikoaController(HistorikoaPanel panel, Lehioa lehioa) {
		this.panel = panel;
		this.lehioa = lehioa;
		this.dao = DAOGetter.getInstance().getHistorikoaDAO();
	}

	public void listenersHasieratu() {
		historikoaTaula = panel.getHistorikoaTaula();
		imgURL = panel.getImgURL();
		lblLoginLogo = panel.getLblLoginLogo();
		btnVolver = panel.getBtnVolver();

		irudiaHasieratu();

		lblLoginLogo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lehioa.cambiarVista("PROFILA");
			}
		});

		btnVolver.addActionListener(e -> {
			lehioa.cambiarVista("SECOND");
		});

		datuakIrakurri();
	}

	private void irudiaHasieratu() {
		if (imgURL != null) {
			ImageIcon originalIcon = new ImageIcon(imgURL);
			Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			lblLoginLogo.setIcon(new ImageIcon(scaledImage));
		} else {
			System.err.println("Error: No se pudo encontrar el archivo /img/user.png");
			lblLoginLogo.setText("[User Img]");
		}
	}

	/**
	 * Historikoaren datuak kargatzen ditu. Sarea badago Firebase-tik jasotzen ditu,
	 * bestela automatikoki "historikoa.xml" fitxategi lokaletik.
	 */
	public void datuakIrakurri() {
		new Thread(() -> {
			List<HistorikoaItem> datos = null;

			try {
				datos = dao.lortuErabiltzailearenHistorikoak(Login.getErabil().getEmail());
			} catch (DAOException e) {
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(panel,
							"Errorea datuak kargatzean", "Errorea",
							JOptionPane.ERROR_MESSAGE);
				});
			}

			if (datos != null) {
				final List<HistorikoaItem> azkenDatuak = datos;
				SwingUtilities.invokeLater(() -> {
					taulaEguneratu(azkenDatuak);

					if (azkenDatuak.isEmpty()) {
						JOptionPane.showMessageDialog(panel,
								"Errorea datuak kargatzean", "Errorea",
								JOptionPane.ERROR_MESSAGE);
					}
				});
			}
		}).start();
	}

	public void taulaEguneratu(List<HistorikoaItem> historikoak) {
		if (historikoaTaula == null) {
			historikoaTaula = panel.getHistorikoaTaula();
		}

		if (historikoaTaula != null) {

			unekoHistorikoak = historikoaTaula.getUnekoHistorikoak();
			modeloTaula = historikoaTaula.getModeloTaula();
		} else {
			System.err.println("[ERROR] Ezin izan da HistorikoaTaula eskuratu.");
			return;
		}

		this.unekoHistorikoak = historikoak;
		modeloTaula.setRowCount(0);

		Collections.sort(this.unekoHistorikoak, new Comparator<HistorikoaItem>() {
			@Override
			public int compare(HistorikoaItem item1, HistorikoaItem item2) {
				if (item1.getData() == null && item2.getData() == null)
					return 0;
				if (item1.getData() == null)
					return 1;
				if (item2.getData() == null)
					return -1;
				return item2.getData().compareTo(item1.getData());
			}
		});

		for (HistorikoaItem historikoa : this.unekoHistorikoak) {
			String denboraGuztiraFormateatuta = TimeFormatter.segunduak(historikoa.getDenboraGuztira());
			String aurreikusitakoDenboraFormateatuta = TimeFormatter.segunduak(historikoa.getAurreikusitakoDenbora());

			modeloTaula
					.addRow(new Object[] { historikoa.getWorkout(), historikoa.getMaila(), denboraGuztiraFormateatuta,
							aurreikusitakoDenboraFormateatuta, historikoa.getData(), historikoa.getOsatua() + "%" });
		}
	}

}
