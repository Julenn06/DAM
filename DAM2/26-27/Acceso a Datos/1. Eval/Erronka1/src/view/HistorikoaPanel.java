package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JLabel;

import controller.HistorikoaController;

public class HistorikoaPanel extends BasePanel {

	private static final long serialVersionUID = 1L;
	private HistorikoaTaula historikoaTaula;
	private HistorikoaController cont;
	private JButton btnVolver;
	private URL imgURL;
	private JLabel lblLoginLogo;

	public HistorikoaPanel(Lehioa lehioa) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 55, 200, 0, 150, 0 };
		gridBagLayout.rowHeights = new int[] { 45, 300, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.5, 0.3, 0.2, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		lblLoginLogo = new JLabel("");
		lblLoginLogo.setFont(FONT);
		GridBagConstraints gbc_lblLoginLogo = new GridBagConstraints();
		gbc_lblLoginLogo.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblLoginLogo.insets = new Insets(0, 0, 5, 5);
		gbc_lblLoginLogo.gridx = 0;
		gbc_lblLoginLogo.gridy = 0;
		add(lblLoginLogo, gbc_lblLoginLogo);

		JLabel lblTitulo = new JLabel("Entrenamenduen Historikoa");
		lblTitulo.setFont(FONT);
		GridBagConstraints gbc_lblTitulo = new GridBagConstraints();
		gbc_lblTitulo.anchor = GridBagConstraints.WEST;
		gbc_lblTitulo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitulo.gridx = 1;
		gbc_lblTitulo.gridy = 0;
		add(lblTitulo, gbc_lblTitulo);

		btnVolver = new JButton("Atzera");
		btnVolver.setFont(FONT);
		GridBagConstraints gbc_btnVolver = new GridBagConstraints();
		gbc_btnVolver.anchor = GridBagConstraints.EAST;
		gbc_btnVolver.insets = new Insets(0, 0, 5, 0);
		gbc_btnVolver.gridx = 3;
		gbc_btnVolver.gridy = 0;
		add(btnVolver, gbc_btnVolver);

		historikoaTaula = new HistorikoaTaula(lehioa);
		GridBagConstraints gbc_panelTabla = new GridBagConstraints();
		gbc_panelTabla.gridwidth = 4;
		gbc_panelTabla.weightx = 1.0;
		gbc_panelTabla.weighty = 1.0;
		gbc_panelTabla.fill = GridBagConstraints.BOTH;
		gbc_panelTabla.gridx = 0;
		gbc_panelTabla.gridy = 1;
		add(historikoaTaula, gbc_panelTabla);

		imgURL = getClass().getResource("/img/user.png");

		cont = new HistorikoaController(this, lehioa);
	}

	public HistorikoaTaula getHistorikoaTaula() {
		return historikoaTaula;
	}

	public HistorikoaController getController() {
		return cont;
	}

	public JLabel getLblLoginLogo() {
		return lblLoginLogo;
	}

	public URL getImgURL() {
		return imgURL;
	}

	public JButton getBtnVolver() {
		return btnVolver;
	}
}
