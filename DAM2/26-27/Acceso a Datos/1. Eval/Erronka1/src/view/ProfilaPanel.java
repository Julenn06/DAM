package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jdatepicker.JDatePicker;

import controller.ProfilaController;

public class ProfilaPanel extends BasePanel {

	private static final long serialVersionUID = 1L;

	private JTextField tfIzena;
	private JTextField tfAbizena1;
	private JTextField tfAbizena2;
	private JPasswordField pfPasahitza;
	private JPasswordField pfPasahitza2;
	private JDatePicker datePickerJaiotzeData;
	private JPasswordField tfPasahitzaZaharra;
	private JButton btnGorde;
	private JButton btnAtzera;
	private JTextField tfEmail;
	private ProfilaController cont;

	public ProfilaPanel(Lehioa lehioa) {

		this.cont = new ProfilaController(this, lehioa);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 30, 260, 145, 150, 30, 0 };
		gridBagLayout.rowHeights = new int[] { 30, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 30, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.5, 0.5, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel title = new JLabel("Nire Profila");
		title.setFont(FONT);
		GridBagConstraints gbc_title = new GridBagConstraints();
		gbc_title.gridwidth = 3;
		gbc_title.anchor = GridBagConstraints.WEST;
		gbc_title.fill = GridBagConstraints.BOTH;
		gbc_title.insets = new Insets(0, 0, 5, 5);
		gbc_title.gridx = 1;
		gbc_title.gridy = 1;
		add(title, gbc_title);

		JLabel lblIzena = new JLabel("Izena");
		lblIzena.setFont(FONT);
		GridBagConstraints gbc_lblIzena = new GridBagConstraints();
		gbc_lblIzena.anchor = GridBagConstraints.WEST;
		gbc_lblIzena.fill = GridBagConstraints.VERTICAL;
		gbc_lblIzena.insets = new Insets(0, 0, 5, 5);
		gbc_lblIzena.gridx = 1;
		gbc_lblIzena.gridy = 2;
		add(lblIzena, gbc_lblIzena);

		tfIzena = new JTextField();
		tfIzena.setFont(FONT);
		GridBagConstraints gbc_tfIzena = new GridBagConstraints();
		gbc_tfIzena.gridwidth = 2;
		gbc_tfIzena.fill = GridBagConstraints.BOTH;
		gbc_tfIzena.insets = new Insets(0, 0, 5, 5);
		gbc_tfIzena.gridx = 2;
		gbc_tfIzena.gridy = 2;
		add(tfIzena, gbc_tfIzena);

		JLabel lblAbizena1 = new JLabel("Abizena 1");
		lblAbizena1.setFont(FONT);
		GridBagConstraints gbc_lblAbizena1 = new GridBagConstraints();
		gbc_lblAbizena1.anchor = GridBagConstraints.WEST;
		gbc_lblAbizena1.fill = GridBagConstraints.VERTICAL;
		gbc_lblAbizena1.insets = new Insets(0, 0, 5, 5);
		gbc_lblAbizena1.gridx = 1;
		gbc_lblAbizena1.gridy = 3;
		add(lblAbizena1, gbc_lblAbizena1);

		tfAbizena1 = new JTextField();
		tfAbizena1.setFont(FONT);
		GridBagConstraints gbc_tfAbizena1 = new GridBagConstraints();
		gbc_tfAbizena1.gridwidth = 2;
		gbc_tfAbizena1.fill = GridBagConstraints.BOTH;
		gbc_tfAbizena1.insets = new Insets(0, 0, 5, 5);
		gbc_tfAbizena1.gridx = 2;
		gbc_tfAbizena1.gridy = 3;
		add(tfAbizena1, gbc_tfAbizena1);

		JLabel lblAbizena2 = new JLabel("Abizena 2");
		lblAbizena2.setFont(FONT);
		GridBagConstraints gbc_lblAbizena2 = new GridBagConstraints();
		gbc_lblAbizena2.anchor = GridBagConstraints.WEST;
		gbc_lblAbizena2.fill = GridBagConstraints.VERTICAL;
		gbc_lblAbizena2.insets = new Insets(0, 0, 5, 5);
		gbc_lblAbizena2.gridx = 1;
		gbc_lblAbizena2.gridy = 4;
		add(lblAbizena2, gbc_lblAbizena2);

		tfAbizena2 = new JTextField();
		tfAbizena2.setFont(FONT);
		GridBagConstraints gbc_tfAbizena2 = new GridBagConstraints();
		gbc_tfAbizena2.gridwidth = 2;
		gbc_tfAbizena2.fill = GridBagConstraints.BOTH;
		gbc_tfAbizena2.insets = new Insets(0, 0, 5, 5);
		gbc_tfAbizena2.gridx = 2;
		gbc_tfAbizena2.gridy = 4;
		add(tfAbizena2, gbc_tfAbizena2);

		JLabel lblPasahitzaZaharra = new JLabel("Zure Pasahitza");
		lblPasahitzaZaharra.setFont(FONT);
		GridBagConstraints gbc_lblPasahitzaZaharra = new GridBagConstraints();
		gbc_lblPasahitzaZaharra.anchor = GridBagConstraints.WEST;
		gbc_lblPasahitzaZaharra.fill = GridBagConstraints.VERTICAL;
		gbc_lblPasahitzaZaharra.insets = new Insets(0, 0, 5, 5);
		gbc_lblPasahitzaZaharra.gridx = 1;
		gbc_lblPasahitzaZaharra.gridy = 5;
		add(lblPasahitzaZaharra, gbc_lblPasahitzaZaharra);

		tfPasahitzaZaharra = new JPasswordField();
		tfPasahitzaZaharra.setFont(FONT);
		GridBagConstraints gbc_tfPasahitzaZaharra = new GridBagConstraints();
		gbc_tfPasahitzaZaharra.gridwidth = 2;
		gbc_tfPasahitzaZaharra.fill = GridBagConstraints.BOTH;
		gbc_tfPasahitzaZaharra.insets = new Insets(0, 0, 5, 5);
		gbc_tfPasahitzaZaharra.gridx = 2;
		gbc_tfPasahitzaZaharra.gridy = 5;
		add(tfPasahitzaZaharra, gbc_tfPasahitzaZaharra);

		JLabel lblPasahitza = new JLabel("Pasahitza Berria");
		lblPasahitza.setFont(FONT);
		GridBagConstraints gbc_lblPasahitza = new GridBagConstraints();
		gbc_lblPasahitza.anchor = GridBagConstraints.WEST;
		gbc_lblPasahitza.fill = GridBagConstraints.VERTICAL;
		gbc_lblPasahitza.insets = new Insets(0, 0, 5, 5);
		gbc_lblPasahitza.gridx = 1;
		gbc_lblPasahitza.gridy = 6;
		add(lblPasahitza, gbc_lblPasahitza);

		pfPasahitza = new JPasswordField();
		pfPasahitza.setFont(FONT);
		GridBagConstraints gbc_pfPasahitza = new GridBagConstraints();
		gbc_pfPasahitza.gridwidth = 2;
		gbc_pfPasahitza.fill = GridBagConstraints.BOTH;
		gbc_pfPasahitza.insets = new Insets(0, 0, 5, 5);
		gbc_pfPasahitza.gridx = 2;
		gbc_pfPasahitza.gridy = 6;
		add(pfPasahitza, gbc_pfPasahitza);

		JLabel lblPasahitza2 = new JLabel("Pasahitza Berria Errepikatu");
		lblPasahitza2.setFont(FONT);
		GridBagConstraints gbc_lblPasahitza2 = new GridBagConstraints();
		gbc_lblPasahitza2.anchor = GridBagConstraints.WEST;
		gbc_lblPasahitza2.fill = GridBagConstraints.VERTICAL;
		gbc_lblPasahitza2.insets = new Insets(0, 0, 5, 5);
		gbc_lblPasahitza2.gridx = 1;
		gbc_lblPasahitza2.gridy = 7;
		add(lblPasahitza2, gbc_lblPasahitza2);

		pfPasahitza2 = new JPasswordField();
		pfPasahitza2.setFont(FONT);
		GridBagConstraints gbc_pfPasahitza2 = new GridBagConstraints();
		gbc_pfPasahitza2.gridwidth = 2;
		gbc_pfPasahitza2.fill = GridBagConstraints.BOTH;
		gbc_pfPasahitza2.insets = new Insets(0, 0, 5, 5);
		gbc_pfPasahitza2.gridx = 2;
		gbc_pfPasahitza2.gridy = 7;
		add(pfPasahitza2, gbc_pfPasahitza2);

		JLabel lblJaiotzeData = new JLabel("Jaiotze Data");
		lblJaiotzeData.setFont(FONT);
		GridBagConstraints gbc_lblJaiotzeData = new GridBagConstraints();
		gbc_lblJaiotzeData.anchor = GridBagConstraints.WEST;
		gbc_lblJaiotzeData.fill = GridBagConstraints.VERTICAL;
		gbc_lblJaiotzeData.insets = new Insets(0, 0, 5, 5);
		gbc_lblJaiotzeData.gridx = 1;
		gbc_lblJaiotzeData.gridy = 8;
		add(lblJaiotzeData, gbc_lblJaiotzeData);

		datePickerJaiotzeData = new JDatePicker();
		testuMotaAplikatu(datePickerJaiotzeData);
		GridBagConstraints gbc_datePickerJaiotzeData = new GridBagConstraints();
		gbc_datePickerJaiotzeData.gridwidth = 2;
		gbc_datePickerJaiotzeData.fill = GridBagConstraints.BOTH;
		gbc_datePickerJaiotzeData.insets = new Insets(0, 0, 5, 5);
		gbc_datePickerJaiotzeData.gridx = 2;
		gbc_datePickerJaiotzeData.gridy = 8;
		add(datePickerJaiotzeData, gbc_datePickerJaiotzeData);
		datePickerJaiotzeData.setTextEditable(false);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(FONT);
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.WEST;
		gbc_lblEmail.fill = GridBagConstraints.VERTICAL;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 1;
		gbc_lblEmail.gridy = 9;
		add(lblEmail, gbc_lblEmail);

		tfEmail = new JTextField();
		tfEmail.setFont(FONT);
		GridBagConstraints gbc_tfEmail = new GridBagConstraints();
		gbc_tfEmail.gridwidth = 2;
		gbc_tfEmail.fill = GridBagConstraints.BOTH;
		gbc_tfEmail.insets = new Insets(0, 0, 5, 5);
		gbc_tfEmail.gridx = 2;
		gbc_tfEmail.gridy = 9;
		add(tfEmail, gbc_tfEmail);

		btnAtzera = new JButton("Atzera");
		btnAtzera.setFont(FONT);
		GridBagConstraints gbc_btnAtzera = new GridBagConstraints();
		gbc_btnAtzera.fill = GridBagConstraints.BOTH;
		gbc_btnAtzera.insets = new Insets(0, 0, 5, 5);
		gbc_btnAtzera.gridx = 2;
		gbc_btnAtzera.gridy = 10;
		add(btnAtzera, gbc_btnAtzera);

		btnGorde = new JButton("Gorde");
		btnGorde.setFont(FONT);
		GridBagConstraints gbc_btnGorde = new GridBagConstraints();
		gbc_btnGorde.fill = GridBagConstraints.BOTH;
		gbc_btnGorde.insets = new Insets(0, 0, 5, 5);
		gbc_btnGorde.gridx = 3;
		gbc_btnGorde.gridy = 10;
		add(btnGorde, gbc_btnGorde);

		setGaituta(false);

		cont.listenersHasieratu();
	}

	public void setGaituta(boolean gaituta) {
		tfIzena.setEnabled(gaituta);
		tfAbizena1.setEnabled(gaituta);
		tfAbizena2.setEnabled(gaituta);
		tfEmail.setEnabled(gaituta);
		pfPasahitza.setEnabled(gaituta);
		pfPasahitza2.setEnabled(gaituta);
		tfPasahitzaZaharra.setEnabled(gaituta);
		datePickerJaiotzeData.setEnabled(gaituta);
		btnGorde.setEnabled(gaituta);
	}

	public void garbitu() {
		tfIzena.setText("");
		tfAbizena1.setText("");
		tfAbizena2.setText("");
		tfEmail.setText("");
		pfPasahitza.setText("");
		pfPasahitza2.setText("");
		tfPasahitzaZaharra.setText("");

		datePickerJaiotzeData.getModel().setValue(null);
	}

	public JButton getBtnAtzera() {
		return btnAtzera;
	}

	public JTextField getTfIzena() {
		return tfIzena;
	}

	public JTextField getTfAbizena1() {
		return tfAbizena1;
	}

	public JTextField getTfAbizena2() {
		return tfAbizena2;
	}

	public JPasswordField getPfPasahitza() {
		return pfPasahitza;
	}

	public JPasswordField getPfPasahitza2() {
		return pfPasahitza2;
	}

	public JDatePicker getDatePickerJaiotzeData() {
		return datePickerJaiotzeData;
	}

	public JPasswordField getTfPasahitzaZaharra() {
		return tfPasahitzaZaharra;
	}

	public JTextField getTfEmail() {
		return tfEmail;
	}

	public JButton getBtnGorde() {
		return btnGorde;
	}

	public ProfilaController getController() {
		return cont;
	}
}
