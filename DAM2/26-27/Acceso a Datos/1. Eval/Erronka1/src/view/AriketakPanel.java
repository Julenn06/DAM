package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;

import controller.AriketaController;

public class AriketakPanel extends BasePanel {

	private static final long serialVersionUID = 1L;
	private TaulaAriketak taulaAriketak;
	private JLabel lblIzena;
	private JButton btnItzuli;
	private JButton btnAriketaHasi;
	private AriketaController cont;
	private JTable tablaVisual;
	private URL imgURL;
	private JLabel lblLoginLogo;

	public AriketakPanel(Lehioa lehioa) {

		cont = new AriketaController(this, lehioa);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 55, 145, 150, 150, 30, 0 };
		gridBagLayout.rowHeights = new int[] { 45, 300, 42, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.34, 0.33, 0.33, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		lblLoginLogo = new JLabel("");
		lblLoginLogo.setFont(FONT);
		GridBagConstraints gbc_lblLoginLogo = new GridBagConstraints();
		gbc_lblLoginLogo.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblLoginLogo.insets = new Insets(0, 0, 5, 5);
		gbc_lblLoginLogo.gridx = 0;
		gbc_lblLoginLogo.gridy = 0;
		add(lblLoginLogo, gbc_lblLoginLogo);

		lblIzena = new JLabel("Ariketak");
		lblIzena.setFont(FONT);
		GridBagConstraints gbc_lblIzena = new GridBagConstraints();
		gbc_lblIzena.gridwidth = 3;
		gbc_lblIzena.anchor = GridBagConstraints.WEST;
		gbc_lblIzena.fill = GridBagConstraints.BOTH;
		gbc_lblIzena.insets = new Insets(0, 0, 5, 5);
		gbc_lblIzena.gridx = 1;
		gbc_lblIzena.gridy = 0;
		add(lblIzena, gbc_lblIzena);

		taulaAriketak = new TaulaAriketak();
		GridBagConstraints gbc_taulaAriketak = new GridBagConstraints();
		gbc_taulaAriketak.gridwidth = 5;
		gbc_taulaAriketak.gridheight = 1;
		gbc_taulaAriketak.weightx = 1.0;
		gbc_taulaAriketak.weighty = 1.0;
		gbc_taulaAriketak.fill = GridBagConstraints.BOTH;
		gbc_taulaAriketak.insets = new Insets(0, 0, 5, 0);
		gbc_taulaAriketak.gridx = 0;
		gbc_taulaAriketak.gridy = 1;
		add(taulaAriketak, gbc_taulaAriketak);

		btnItzuli = new JButton("Itzuli");
		btnItzuli.setFont(FONT);
		GridBagConstraints gbc_btnItzuli = new GridBagConstraints();
		gbc_btnItzuli.fill = GridBagConstraints.BOTH;
		gbc_btnItzuli.insets = new Insets(0, 0, 0, 5);
		gbc_btnItzuli.gridx = 2;
		gbc_btnItzuli.gridy = 2;
		add(btnItzuli, gbc_btnItzuli);

		btnAriketaHasi = new JButton("Ariketa Hasi");
		btnAriketaHasi.setFont(FONT);
		GridBagConstraints gbc_btnAriketaHasi = new GridBagConstraints();
		gbc_btnAriketaHasi.fill = GridBagConstraints.BOTH;
		gbc_btnAriketaHasi.insets = new Insets(0, 0, 0, 5);
		gbc_btnAriketaHasi.gridx = 3;
		gbc_btnAriketaHasi.gridy = 2;
		add(btnAriketaHasi, gbc_btnAriketaHasi);

		tablaVisual = taulaAriketak.getTaula();

		imgURL = getClass().getResource("/img/user.png");

		cont.listenersHasieratu();
	}

	public JButton getBtnAriketaHasi() {
		return btnAriketaHasi;
	}

	public JButton getBtnItzuli() {
		return btnItzuli;
	}

	public JTable getTablaVisual() {
		return tablaVisual;
	}

	public TaulaAriketak getTaulaAriketak() {
		return taulaAriketak;
	}

	public URL getImgURL() {
		return imgURL;
	}

	public JLabel getLblLoginLogo() {
		return lblLoginLogo;
	}

	public JLabel getLblIzena() {
		return lblIzena;
	}
}
