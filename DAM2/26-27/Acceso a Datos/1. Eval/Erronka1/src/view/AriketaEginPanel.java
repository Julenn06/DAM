package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import controller.AriketakEginController;

public class AriketaEginPanel extends BasePanel {

	private static final long serialVersionUID = 1L;

	public static final String KRONOMETRO_IKONOA = "\u23f2";
	public static final String DENBORA_IKONOA = "\u23f3";
	public static final String ATSEDEN_IKONOA = "\uD83E\uDDD8";

	private JPanel pnlCabeceraNegra;

	private JLabel lblWorkoutInfo;
	private JLabel lblWorkoutKrono;

	private JLabel lblAriketaKrono;
	private JLabel lblAriketaIzena;
	private JLabel lblAriketaDesc;

	private JPanel pnlSeriesContenedor;

	private JPanel pnlAbisuenEremua;
	private JLabel lblAbisuTestua;

	private JButton btnItzuli;
	private JButton btnHasiPausatu;
	private JButton btnHurrengoa;

	private AriketakEginController cont;

	public AriketaEginPanel(Lehioa lehioa) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 30, 0, 30, 0 };
		gridBagLayout.rowHeights = new int[] { 85, 50, 140, 60, 55, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		pnlCabeceraNegra = new JPanel();
		pnlCabeceraNegra.setBackground(new Color(24, 24, 24));
		pnlCabeceraNegra.setLayout(new GridBagLayout());
		pnlCabeceraNegra.setBorder(new LineBorder(Color.DARK_GRAY, 1));

		GridBagConstraints gbc_pnlCabecera = new GridBagConstraints();
		gbc_pnlCabecera.fill = GridBagConstraints.BOTH;
		gbc_pnlCabecera.insets = new Insets(15, 15, 10, 15);
		gbc_pnlCabecera.gridx = 1;
		gbc_pnlCabecera.gridy = 0;
		add(pnlCabeceraNegra, gbc_pnlCabecera);

		lblWorkoutInfo = new JLabel();
		lblWorkoutInfo.setForeground(Color.WHITE);
		lblWorkoutInfo.setFont(new Font("SansSerif", Font.BOLD, 18));
		GridBagConstraints gbc_lblWorkoutInfo = new GridBagConstraints();
		gbc_lblWorkoutInfo.anchor = GridBagConstraints.WEST;
		gbc_lblWorkoutInfo.weightx = 1.0;
		gbc_lblWorkoutInfo.insets = new Insets(0, 20, 0, 0);
		pnlCabeceraNegra.add(lblWorkoutInfo, gbc_lblWorkoutInfo);

		lblWorkoutKrono = new JLabel();
		lblWorkoutKrono.setForeground(Color.WHITE);
		lblWorkoutKrono.setFont(new Font("SansSerif", Font.BOLD, 22));
		GridBagConstraints gbc_lblWorkoutKrono = new GridBagConstraints();
		gbc_lblWorkoutKrono.anchor = GridBagConstraints.EAST;
		gbc_lblWorkoutKrono.insets = new Insets(0, 0, 0, 20);
		pnlCabeceraNegra.add(lblWorkoutKrono, gbc_lblWorkoutKrono);

		lblAriketaIzena = new JLabel();
		lblAriketaIzena.setFont(new Font("SansSerif", Font.BOLD, 22));
		GridBagConstraints gbc_lblAriketaIzena = new GridBagConstraints();
		gbc_lblAriketaIzena.anchor = GridBagConstraints.WEST;
		gbc_lblAriketaIzena.insets = new Insets(5, 15, 5, 15);
		gbc_lblAriketaIzena.gridx = 1;
		gbc_lblAriketaIzena.gridy = 1;
		add(lblAriketaIzena, gbc_lblAriketaIzena);

		lblAriketaKrono = new JLabel(KRONOMETRO_IKONOA);
		lblAriketaKrono.setFont(new Font("SansSerif", Font.BOLD, 22));
		lblAriketaKrono.setForeground(new Color(205, 30, 30));
		GridBagConstraints gbc_lblAriketaKrono = new GridBagConstraints();
		gbc_lblAriketaKrono.anchor = GridBagConstraints.EAST;
		gbc_lblAriketaKrono.insets = new Insets(5, 15, 5, 25);
		gbc_lblAriketaKrono.gridx = 1;
		gbc_lblAriketaKrono.gridy = 1;
		add(lblAriketaKrono, gbc_lblAriketaKrono);

		JPanel pnlCuerpoCentral = new JPanel(new GridBagLayout());
		GridBagConstraints gbc_pnlCuerpo = new GridBagConstraints();
		gbc_pnlCuerpo.fill = GridBagConstraints.BOTH;
		gbc_pnlCuerpo.insets = new Insets(0, 15, 5, 15);
		gbc_pnlCuerpo.gridx = 1;
		gbc_pnlCuerpo.gridy = 2;
		add(pnlCuerpoCentral, gbc_pnlCuerpo);

		lblAriketaDesc = new JLabel();
		lblAriketaDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblAriketaDesc.setForeground(Color.GRAY);
		GridBagConstraints gbc_lblDesc = new GridBagConstraints();
		gbc_lblDesc.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblDesc.weightx = 1.0;
		gbc_lblDesc.insets = new Insets(0, 0, 10, 0);
		gbc_lblDesc.gridx = 0;
		gbc_lblDesc.gridy = 0;
		pnlCuerpoCentral.add(lblAriketaDesc, gbc_lblDesc);

		pnlSeriesContenedor = new JPanel(new GridLayout(1, 0, 15, 0));
		GridBagConstraints gbc_pnlSeries = new GridBagConstraints();
		gbc_pnlSeries.fill = GridBagConstraints.BOTH;
		gbc_pnlSeries.weightx = 1.0;
		gbc_pnlSeries.weighty = 1.0;
		gbc_pnlSeries.gridx = 0;
		gbc_pnlSeries.gridy = 1;
		pnlCuerpoCentral.add(pnlSeriesContenedor, gbc_pnlSeries);

		pnlAbisuenEremua = new JPanel();
		pnlAbisuenEremua.setBorder(new LineBorder(new Color(220, 220, 220), 1));
		pnlAbisuenEremua.setBackground(new Color(248, 248, 248));
		pnlAbisuenEremua.setLayout(new GridBagLayout());

		GridBagConstraints gbc_pnlAbisuen = new GridBagConstraints();
		gbc_pnlAbisuen.fill = GridBagConstraints.BOTH;
		gbc_pnlAbisuen.insets = new Insets(5, 15, 10, 15);
		gbc_pnlAbisuen.gridx = 1;
		gbc_pnlAbisuen.gridy = 3;
		add(pnlAbisuenEremua, gbc_pnlAbisuen);

		lblAbisuTestua = new JLabel(DENBORA_IKONOA + "Hasteko... / Atsedena");
		lblAbisuTestua.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblAbisuTestua.setForeground(Color.DARK_GRAY);
		pnlAbisuenEremua.add(lblAbisuTestua);

		JPanel pnlBotoiak = new JPanel(new GridLayout(1, 3, 15, 0));
		GridBagConstraints gbc_pnlBotoiak = new GridBagConstraints();
		gbc_pnlBotoiak.fill = GridBagConstraints.BOTH;
		gbc_pnlBotoiak.insets = new Insets(0, 15, 15, 15);
		gbc_pnlBotoiak.gridx = 1;
		gbc_pnlBotoiak.gridy = 4;
		add(pnlBotoiak, gbc_pnlBotoiak);

		btnItzuli = new JButton("Irten");
		btnItzuli.setFont(FONT);
		pnlBotoiak.add(btnItzuli);

		btnHasiPausatu = new JButton("Hasi");
		btnHasiPausatu.setFont(FONT);
		pnlBotoiak.add(btnHasiPausatu);

		btnHurrengoa = new JButton("Siguiente");
		btnHurrengoa.setFont(FONT);
		pnlBotoiak.add(btnHurrengoa);

		cont = new AriketakEginController(this, lehioa);
	}

	public JPanel getPnlCabeceraNegra() {
		return pnlCabeceraNegra;
	}

	public JLabel getLblAriketaDesc() {
		return lblAriketaDesc;
	}

	public JPanel getPnlSeriesContenedor() {
		return pnlSeriesContenedor;
	}

	public JPanel getPnlAbisuenEremua() {
		return pnlAbisuenEremua;
	}

	public JLabel getLblWorkoutInfo() {
		return lblWorkoutInfo;
	}

	public JLabel getLblWorkoutKrono() {
		return lblWorkoutKrono;
	}

	public JLabel getLblAriketaKrono() {
		return lblAriketaKrono;
	}

	public JLabel getLblAbisuTestua() {
		return lblAbisuTestua;
	}

	public JButton getBtnItzuli() {
		return btnItzuli;
	}

	public JButton getBtnHasiPausatu() {
		return btnHasiPausatu;
	}

	public JButton getBtnHurrengoa() {
		return btnHurrengoa;
	}

	public JLabel getLblAriketaIzena() {
		return lblAriketaIzena;
	}

	public AriketakEginController getController() {
		return cont;
	}

}
