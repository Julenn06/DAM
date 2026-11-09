package controller;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.validator.routines.EmailValidator;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import dao.DAOGetter;
import dao.interfazeak.IErabiltzaileDAO;
import model.DAOException;
import model.Erabiltzailea;
import view.Lehioa;
import view.RegisterPanel;

public class RegistratuController {

	private RegisterPanel panel;
	private Lehioa lehioa;
	private IErabiltzaileDAO dao = DAOGetter.getInstance().getErabiltzaileaDAO();
	private JPasswordField passwordFieldPasahitza;
	private JButton btnRegistratu;
	private JTextField textFieldIzena;
	private JTextField textFieldAbizena1;
	private JTextField textFieldAbizena2;
	private JTextField textFieldEmail;
	private JDatePicker datePickerJaiotzeData;
	private JButton btnItzuli;

	private String izena;
	private String abizena1;
	private String abizena2;
	private String email;
	private String pasahitza;
	private Date jaiotzeData;

	public RegistratuController(RegisterPanel panel, Lehioa lehioa) {
		this.panel = panel;
		this.lehioa = lehioa;
	}

	private void erregistratu() {
		passwordFieldPasahitza = panel.getPasswordFieldPasahitza();
		textFieldIzena = panel.getTextFieldIzena();
		textFieldAbizena1 = panel.getTextFieldAbizena1();
		textFieldAbizena2 = panel.getTextFieldAbizena2();
		textFieldEmail = panel.getTextFieldEmail();
		datePickerJaiotzeData = panel.getDatePickerJaiotzeData();

		izena = textFieldIzena.getText().trim();
		abizena1 = textFieldAbizena1.getText().trim();
		abizena2 = textFieldAbizena2.getText().trim();
		email = textFieldEmail.getText().trim();
		pasahitza = new String(passwordFieldPasahitza.getPassword()).trim();
		jaiotzeData = jasoData(datePickerJaiotzeData.getModel());

		if (izena.isEmpty() || abizena1.isEmpty() || abizena2.isEmpty() || pasahitza.isEmpty() || email.isEmpty()
				|| jaiotzeData == null) {
			JOptionPane.showMessageDialog(lehioa, "Eremu guztiak bete behar dira", "Error", JOptionPane.ERROR_MESSAGE);
		}

		if (!EmailValidator.getInstance().isValid(email)) {
			JOptionPane.showMessageDialog(lehioa, "Emaila ez da baliozkoa", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			Erabiltzailea erab = new Erabiltzailea(izena, abizena1, abizena2, email, jaiotzeData, 0, pasahitza);

			boolean registratu = dao.registratu(erab);

			if (registratu) {
				JOptionPane.showMessageDialog(lehioa, "Arrakastaz erregistratu da", "Mezua",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (DAOException e1) {
			JOptionPane.showMessageDialog(lehioa, "Ezin izan da erabiltzaile erregistratu", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private Date jasoData(DateModel<?> model) {
		Object balioa = model.getValue();
		if (balioa.getClass() == Date.class) {
			return (Date) balioa;
		} else if (balioa.getClass() == GregorianCalendar.class) {
			return ((GregorianCalendar) balioa).getTime();
		}
		return null;
	}

	public void listenersHasieratu() {
		btnRegistratu = panel.getBtnRegistratu();
		btnItzuli = panel.getBtnItzuli();

		btnRegistratu.addActionListener(e -> erregistratu());

		btnItzuli.addActionListener(e -> lehioa.cambiarVista("LOGIN"));

		if (!DAOGetter.getInstance().isLineazKanpo()) {
			panel.ezarriGaituta(true);
		}
	}
}
