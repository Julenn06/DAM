package controller;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Ariketa;
import model.Workout;
import utils.TimeFormatter;
import view.AriketakPanel;
import view.Lehioa;
import view.TaulaAriketak;

public class AriketaController {

	private JButton btnItzuli;
	private Lehioa lehioa;
	private AriketakPanel panel;
	private JTable tablaVisual;
	private TaulaAriketak taulaAriketak;
	private URL imgURL;
	private JLabel lblLoginLogo;
	private JLabel lblIzena;
	private JButton btnAriketaHasi;
	private DefaultTableModel modeloTaula;
	private List<Ariketa> unekoAriketak = new ArrayList<>();

	public AriketaController(AriketakPanel panel, Lehioa lehioa) {
		this.panel = panel;
		this.lehioa = lehioa;
	}

	public void listenersHasieratu() {
		tablaVisual = panel.getTablaVisual();
		taulaAriketak = panel.getTaulaAriketak();
		imgURL = panel.getImgURL();
		lblLoginLogo = panel.getLblLoginLogo();
		btnItzuli = panel.getBtnItzuli();
		btnAriketaHasi = panel.getBtnAriketaHasi();
		taulaAriketak = panel.getTaulaAriketak();
		tablaVisual = taulaAriketak.getTaula();

		tablaVisual.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					int filaSeleccionada = tablaVisual.getSelectedRow();
					if (filaSeleccionada != -1) {
						Ariketa hautatutakoAriketa = getAriketaAt(filaSeleccionada);
						if (hautatutakoAriketa != null) {
						}
					}
				}
			}
		});

		btnItzuli.addActionListener(e -> lehioa.cambiarVista("SECOND"));

		btnAriketaHasi.addActionListener(e -> lehioa.cambiarVista("ARIKETAKEGIN"));

		if (imgURL != null) {
			ImageIcon originalIcon = new ImageIcon(imgURL);
			Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			lblLoginLogo.setIcon(new ImageIcon(scaledImage));
		} else {
			System.err.println("Error: No se pudo encontrar el archivo /img/user.png");
			lblLoginLogo.setText("[User Img]");
		}
		lblLoginLogo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lehioa.cambiarVista("PROFILA");
			}
		});
	}

	public void kargatuAriketak(Workout saioa) {
		lblIzena = panel.getLblIzena();
		taulaAriketak = panel.getTaulaAriketak();
		if (saioa != null) {
			lblIzena.setText("Workout: " + saioa.getIzena());
			taulaEguneratu(saioa.getAriketak());
		}
	}

	public Ariketa getAriketaAt(int index) {
		unekoAriketak = taulaAriketak.getUnekoAriketak();
		if (index >= 0 && index < unekoAriketak.size()) {
			return unekoAriketak.get(index);
		}
		return null;
	}

	public void taulaEguneratu(List<Ariketa> ariketak) {
		modeloTaula = taulaAriketak.getModeloTaula();

		taulaAriketak.getUnekoAriketak().clear();
		taulaAriketak.getUnekoAriketak().addAll(ariketak);

		modeloTaula.setRowCount(0);
		for (Ariketa ariketa : ariketak) {
			int iraupena = ariketa.getIraupena();
			String denbora = TimeFormatter.segunduak(iraupena);
			modeloTaula.addRow(new Object[] { ariketa.getIzena(), ariketa.getDeskribapena(),
					iraupena > 0 ? denbora : "Serierik ez" });
		}
	}
}
