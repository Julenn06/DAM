package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jdatepicker.JDatePicker;

import controller.RegistratuController;

public class RegisterPanel extends BasePanel {

	private static final long serialVersionUID = 1L;

	private JTextField textFieldIzena;
	private JTextField textFieldAbizena1;
	private JTextField textFieldAbizena2;
	private JTextField textFieldEmail;
	private JPasswordField passwordFieldPasahitza;
	private JDatePicker datePickerJaiotzeData;
	private JButton btnRegistratu;
	private JButton btnItzuli;

	public RegisterPanel(Lehioa lehioa) {

		RegistratuController cont = new RegistratuController(this, lehioa);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 30, 170, 145, 150, 30, 0 };
		gridBagLayout.rowHeights = new int[] { 30, 42, 42, 42, 42, 42, 42, 42, 30, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.5, 0.5, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblIzena = new JLabel("Izena");
		lblIzena.setFont(FONT);
		GridBagConstraints gbc_lblIzena = new GridBagConstraints();
		gbc_lblIzena.anchor = GridBagConstraints.WEST;
		gbc_lblIzena.fill = GridBagConstraints.VERTICAL;
		gbc_lblIzena.insets = new Insets(0, 0, 5, 5);
		gbc_lblIzena.gridx = 1;
		gbc_lblIzena.gridy = 1;
		add(lblIzena, gbc_lblIzena);

		textFieldIzena = new JTextField();
		textFieldIzena.setFont(FONT);
		GridBagConstraints gbc_textFieldIzena = new GridBagConstraints();
		gbc_textFieldIzena.gridwidth = 2;
		gbc_textFieldIzena.fill = GridBagConstraints.BOTH;
		gbc_textFieldIzena.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldIzena.gridx = 2;
		gbc_textFieldIzena.gridy = 1;
		add(textFieldIzena, gbc_textFieldIzena);
		textFieldIzena.setColumns(10);

		JLabel lblAbizena1 = new JLabel("Lehenengo Abizena");
		lblAbizena1.setFont(FONT);
		GridBagConstraints gbc_lblAbizena1 = new GridBagConstraints();
		gbc_lblAbizena1.anchor = GridBagConstraints.WEST;
		gbc_lblAbizena1.fill = GridBagConstraints.VERTICAL;
		gbc_lblAbizena1.insets = new Insets(0, 0, 5, 5);
		gbc_lblAbizena1.gridx = 1;
		gbc_lblAbizena1.gridy = 2;
		add(lblAbizena1, gbc_lblAbizena1);

		textFieldAbizena1 = new JTextField();
		textFieldAbizena1.setFont(FONT);
		GridBagConstraints gbc_textFieldAbizena1 = new GridBagConstraints();
		gbc_textFieldAbizena1.gridwidth = 2;
		gbc_textFieldAbizena1.fill = GridBagConstraints.BOTH;
		gbc_textFieldAbizena1.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldAbizena1.gridx = 2;
		gbc_textFieldAbizena1.gridy = 2;
		add(textFieldAbizena1, gbc_textFieldAbizena1);
		textFieldAbizena1.setColumns(10);

		JLabel lblAbizena2 = new JLabel("Bigarren Abizena");
		lblAbizena2.setFont(FONT);
		GridBagConstraints gbc_lblAbizena2 = new GridBagConstraints();
		gbc_lblAbizena2.anchor = GridBagConstraints.WEST;
		gbc_lblAbizena2.fill = GridBagConstraints.VERTICAL;
		gbc_lblAbizena2.insets = new Insets(0, 0, 5, 5);
		gbc_lblAbizena2.gridx = 1;
		gbc_lblAbizena2.gridy = 3;
		add(lblAbizena2, gbc_lblAbizena2);

		textFieldAbizena2 = new JTextField();
		textFieldAbizena2.setFont(FONT);
		GridBagConstraints gbc_textFieldAbizena2 = new GridBagConstraints();
		gbc_textFieldAbizena2.gridwidth = 2;
		gbc_textFieldAbizena2.fill = GridBagConstraints.BOTH;
		gbc_textFieldAbizena2.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldAbizena2.gridx = 2;
		gbc_textFieldAbizena2.gridy = 3;
		add(textFieldAbizena2, gbc_textFieldAbizena2);
		textFieldAbizena2.setColumns(10);

		JLabel lblJaiotzeData = new JLabel("Jaiotze Data");
		lblJaiotzeData.setFont(FONT);
		GridBagConstraints gbc_lblJaiotzeData = new GridBagConstraints();
		gbc_lblJaiotzeData.anchor = GridBagConstraints.WEST;
		gbc_lblJaiotzeData.fill = GridBagConstraints.VERTICAL;
		gbc_lblJaiotzeData.insets = new Insets(0, 0, 5, 5);
		gbc_lblJaiotzeData.gridx = 1;
		gbc_lblJaiotzeData.gridy = 4;
		add(lblJaiotzeData, gbc_lblJaiotzeData);

		datePickerJaiotzeData = new JDatePicker();
		testuMotaAplikatu(datePickerJaiotzeData);
		GridBagConstraints gbc_datePickerJaiotzeData = new GridBagConstraints();
		gbc_datePickerJaiotzeData.gridwidth = 2;
		gbc_datePickerJaiotzeData.fill = GridBagConstraints.BOTH;
		gbc_datePickerJaiotzeData.insets = new Insets(0, 0, 5, 5);
		gbc_datePickerJaiotzeData.gridx = 2;
		gbc_datePickerJaiotzeData.gridy = 4;
		add(datePickerJaiotzeData, gbc_datePickerJaiotzeData);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(FONT);
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.WEST;
		gbc_lblEmail.fill = GridBagConstraints.VERTICAL;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 1;
		gbc_lblEmail.gridy = 5;
		add(lblEmail, gbc_lblEmail);

		textFieldEmail = new JTextField();
		textFieldEmail.setFont(FONT);
		GridBagConstraints gbc_textFieldEmail = new GridBagConstraints();
		gbc_textFieldEmail.gridwidth = 2;
		gbc_textFieldEmail.fill = GridBagConstraints.BOTH;
		gbc_textFieldEmail.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldEmail.gridx = 2;
		gbc_textFieldEmail.gridy = 5;
		add(textFieldEmail, gbc_textFieldEmail);
		textFieldEmail.setColumns(10);

		JLabel lblPasahitza = new JLabel("Pasahitza");
		lblPasahitza.setFont(FONT);
		GridBagConstraints gbc_lblPasahitza = new GridBagConstraints();
		gbc_lblPasahitza.anchor = GridBagConstraints.WEST;
		gbc_lblPasahitza.fill = GridBagConstraints.VERTICAL;
		gbc_lblPasahitza.insets = new Insets(0, 0, 5, 5);
		gbc_lblPasahitza.gridx = 1;
		gbc_lblPasahitza.gridy = 6;
		add(lblPasahitza, gbc_lblPasahitza);

		passwordFieldPasahitza = new JPasswordField();
		passwordFieldPasahitza.setFont(FONT);
		GridBagConstraints gbc_passwordFieldPasahitza = new GridBagConstraints();
		gbc_passwordFieldPasahitza.gridwidth = 2;
		gbc_passwordFieldPasahitza.fill = GridBagConstraints.BOTH;
		gbc_passwordFieldPasahitza.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldPasahitza.gridx = 2;
		gbc_passwordFieldPasahitza.gridy = 6;
		add(passwordFieldPasahitza, gbc_passwordFieldPasahitza);
		passwordFieldPasahitza.setColumns(10);

		btnItzuli = new JButton("Itzuli");
		btnItzuli.setFont(FONT);
		GridBagConstraints gbc_btnItzuli = new GridBagConstraints();
		gbc_btnItzuli.fill = GridBagConstraints.BOTH;
		gbc_btnItzuli.insets = new Insets(0, 0, 5, 5);
		gbc_btnItzuli.gridx = 2;
		gbc_btnItzuli.gridy = 7;
		add(btnItzuli, gbc_btnItzuli);

		btnRegistratu = new JButton("Registratu");
		btnRegistratu.setFont(FONT);
		GridBagConstraints gbc_btnRegistratu = new GridBagConstraints();
		gbc_btnRegistratu.insets = new Insets(0, 0, 5, 5);
		gbc_btnRegistratu.fill = GridBagConstraints.BOTH;
		gbc_btnRegistratu.gridx = 3;
		gbc_btnRegistratu.gridy = 7;
		add(btnRegistratu, gbc_btnRegistratu);

		ezarriGaituta(false);

		cont.listenersHasieratu();
	}

	public void ezarriGaituta(boolean gaituta) {
		textFieldIzena.setEnabled(gaituta);
		textFieldAbizena1.setEnabled(gaituta);
		textFieldAbizena2.setEnabled(gaituta);
		textFieldEmail.setEnabled(gaituta);
		passwordFieldPasahitza.setEnabled(gaituta);
		datePickerJaiotzeData.setEnabled(gaituta);
		btnRegistratu.setEnabled(gaituta);
		btnItzuli.setEnabled(gaituta);
	}

	public JButton getBtnRegistratu() {
		return btnRegistratu;
	}

	public JButton getBtnItzuli() {
		return btnItzuli;
	}

	public JTextField getTextFieldIzena() {
		return textFieldIzena;
	}

	public JTextField getTextFieldAbizena1() {
		return textFieldAbizena1;
	}

	public JTextField getTextFieldAbizena2() {
		return textFieldAbizena2;
	}

	public JTextField getTextFieldEmail() {
		return textFieldEmail;
	}

	public JPasswordField getPasswordFieldPasahitza() {
		return passwordFieldPasahitza;
	}

	public JDatePicker getDatePickerJaiotzeData() {
		return datePickerJaiotzeData;
	}
}
