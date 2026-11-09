package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import controller.WorkoutController;

public class WorkoutPanel extends BasePanel {

	private static final long serialVersionUID = 1L;
	private TaulaWorkoutak panelTabla;
	private JComboBox<String> comboMaila;
	private WorkoutController cont;
	private URL imgURL;
	private JLabel lblLoginLogo;
	private JButton btnHistorikoa;
	private JButton btnWorkoutItxi;

	public WorkoutPanel(Lehioa lehioa) {

		cont = new WorkoutController(this, lehioa);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 55, 145, 150, 150, 30, 0 };
		gridBagLayout.rowHeights = new int[] { 45, 300, 42, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.4, 0.3, 0.3, 0.0, Double.MIN_VALUE };
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

		btnWorkoutItxi = new JButton("Workout itxi");
		btnWorkoutItxi.setFont(FONT);
		GridBagConstraints gbc_btnWorkoutItxi = new GridBagConstraints();
		gbc_btnWorkoutItxi.fill = GridBagConstraints.BOTH;
		gbc_btnWorkoutItxi.insets = new Insets(0, 0, 5, 5);
		gbc_btnWorkoutItxi.gridx = 1;
		gbc_btnWorkoutItxi.gridy = 0;
		add(btnWorkoutItxi, gbc_btnWorkoutItxi);

		JLabel lblAriketak = new JLabel("Entrenamenduen Workoutak");
		lblAriketak.setFont(FONT);
		GridBagConstraints gbc_lblAriketak = new GridBagConstraints();
		gbc_lblAriketak.anchor = GridBagConstraints.WEST;
		gbc_lblAriketak.fill = GridBagConstraints.BOTH;
		gbc_lblAriketak.insets = new Insets(0, 0, 5, 5);
		gbc_lblAriketak.gridx = 2;
		gbc_lblAriketak.gridy = 0;
		add(lblAriketak, gbc_lblAriketak);

		comboMaila = new JComboBox<>();
		testuMotaAplikatu(comboMaila);
		comboMaila.setToolTipText("Zure maila aukeratu");
		GridBagConstraints gbc_comboMaila = new GridBagConstraints();
		gbc_comboMaila.fill = GridBagConstraints.BOTH;
		gbc_comboMaila.insets = new Insets(0, 0, 5, 5);
		gbc_comboMaila.gridx = 3;
		gbc_comboMaila.gridy = 0;
		add(comboMaila, gbc_comboMaila);

		panelTabla = new TaulaWorkoutak();
		GridBagConstraints gbc_panelTabla = new GridBagConstraints();
		gbc_panelTabla.gridwidth = 5;
		gbc_panelTabla.gridheight = 1;
		gbc_panelTabla.weightx = 1.0;
		gbc_panelTabla.weighty = 1.0;
		gbc_panelTabla.fill = GridBagConstraints.BOTH;
		gbc_panelTabla.insets = new Insets(0, 0, 5, 0);
		gbc_panelTabla.gridx = 0;
		gbc_panelTabla.gridy = 1;
		add(panelTabla, gbc_panelTabla);

		btnHistorikoa = new JButton("Historikoa Ikusi");
		btnHistorikoa.setFont(FONT);
		GridBagConstraints gbc_btnHistorikoa = new GridBagConstraints();
		gbc_btnHistorikoa.fill = GridBagConstraints.BOTH;
		gbc_btnHistorikoa.insets = new Insets(0, 0, 0, 5);
		gbc_btnHistorikoa.gridx = 1;
		gbc_btnHistorikoa.gridy = 2;
		add(btnHistorikoa, gbc_btnHistorikoa);

		imgURL = getClass().getResource("/img/user.png");

		cont.listenersHasieratu();
	}

	public JLabel getLblLoginLogo() {
		return lblLoginLogo;
	}

	public URL getImgURL() {
		return imgURL;
	}

	public WorkoutController getCont() {
		return cont;
	}

	public JComboBox<String> getComboMaila() {
		return comboMaila;
	}

	public TaulaWorkoutak getPanelTabla() {
		return panelTabla;
	}

	public JButton getBtnHistorikoa() {
		return btnHistorikoa;
	}

	public JButton getBtnWorkoutItxi() {
		return btnWorkoutItxi;
	}
}
