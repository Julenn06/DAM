package controller;

import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jdatepicker.JDatePicker;

import dao.DAOGetter;
import dao.interfazeak.IErabiltzaileDAO;
import model.DAOException;
import model.Erabiltzailea;
import view.Lehioa;
import view.ProfilaPanel;

public class ProfilaController {

	private ProfilaPanel panel;
	private Lehioa lehioa;
	private JTextField tfIzena;
	private JTextField tfAbizena1;
	private JTextField tfAbizena2;
	private JTextField tfEmail;
	private JPasswordField pfPasahitza;
	private JPasswordField pfPasahitza2;
	private JDatePicker datePickerJaiotzeData;
	private JPasswordField tfPasahitzaZaharra;
	private JButton btnGorde;
	private JButton btnAtzera;

	private IErabiltzaileDAO dao = DAOGetter.getInstance().getErabiltzaileaDAO();

	public ProfilaController(ProfilaPanel panel, Lehioa lehioa) {
		this.panel = panel;
		this.lehioa = lehioa;
	}

	public void listenersHasieratu() {
		this.tfIzena = panel.getTfIzena();
		this.tfAbizena1 = panel.getTfAbizena1();
		this.tfAbizena2 = panel.getTfAbizena2();
		this.tfEmail = panel.getTfEmail();
		this.pfPasahitza = panel.getPfPasahitza();
		this.pfPasahitza2 = panel.getPfPasahitza2();
		this.datePickerJaiotzeData = panel.getDatePickerJaiotzeData();
		this.tfPasahitzaZaharra = panel.getTfPasahitzaZaharra();
		this.btnGorde = panel.getBtnGorde();
		this.btnAtzera = panel.getBtnAtzera();

		kargatuDatuakBistan();

		btnAtzera.addActionListener((ActionEvent e) -> {
			lehioa.cambiarVista("SECOND");
		});

		btnGorde.addActionListener((ActionEvent e) -> {
			String email = tfEmail.getText().trim();
			String izena = tfIzena.getText().trim();
			String abizena1 = tfAbizena1.getText().trim();
			String abizena2 = tfAbizena2.getText().trim();

			String pasahitzaZaharra = new String(tfPasahitzaZaharra.getPassword()).trim();
			String pasahitzaBerria = new String(pfPasahitza.getPassword()).trim();
			String pasahitzaBerriaErrepikatu = new String(pfPasahitza2.getPassword()).trim();

			Calendar cal = (Calendar) datePickerJaiotzeData.getModel().getValue();
			Date utilDate = (cal != null) ? cal.getTime() : null;

			boolean pasahitzaAldatu = !pasahitzaZaharra.isEmpty() || !pasahitzaBerria.isEmpty()
					|| !pasahitzaBerriaErrepikatu.isEmpty();

			if (pasahitzaAldatu) {
				if (pasahitzaZaharra.isEmpty()) {
					erakutsiBalidazioErrorea("Pasahitz zaharra beharrezkoa da!");
					return;
				}
				if (pasahitzaBerria.isEmpty()) {
					erakutsiBalidazioErrorea("Pasahitza berria ezin da hutsik egon!");
					return;
				}
				if (!pasahitzaBerria.equals(pasahitzaBerriaErrepikatu)) {
					erakutsiBalidazioErrorea("Pasahitz berriak ez datoz bat!");
					return;
				}
			}

			try {
				Erabiltzailea erab;
				Erabiltzailea saioa = Login.getErabil();

				if (pasahitzaAldatu) {
					erab = new Erabiltzailea(izena, abizena1, abizena2, email, utilDate,
							Login.getErabil().getMaila(), pasahitzaBerria);
					dao.profilaEguneratu(erab, pasahitzaZaharra);
					Login.getErabil().setPasahitza(pasahitzaBerria);

					tfPasahitzaZaharra.setText("");
					pfPasahitza.setText("");
					pfPasahitza2.setText("");
					panel.garbitu();
				} else {
					erab = new Erabiltzailea(izena, abizena1, abizena2, email);
					dao.izenaEguneratu(erab);
				}

				saioa.setIzena(izena);
				saioa.setAbizena1(abizena1);
				saioa.setAbizena2(abizena2);
				saioa.setEmail(email);
				saioa.setJaiotzeData(utilDate);

				kargatuErabiltzaileDatuak(erab);
				JOptionPane.showMessageDialog(panel, "Profila arrakastaz eguneratu da", "Arrakasta",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (DAOException e1) {
				String ekintza = pasahitzaAldatu ? "profila" : "izena";
				JOptionPane.showMessageDialog(panel, "Ezin izan da " + ekintza + " eguneratu: " + e1.getMessage(),
						"Errorea",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		if (!DAOGetter.getInstance().isLineazKanpo()) {
			panel.setGaituta(true);
		}
	}

	private void erakutsiBalidazioErrorea(String message) {
		JOptionPane.showMessageDialog(panel, message, "Errorea", JOptionPane.ERROR_MESSAGE);
	}

	public void kargatuDatuakBistan() {
		Erabiltzailea unekoErabiltzailea = Login.getErabil();
		if (unekoErabiltzailea != null) {
			kargatuErabiltzaileDatuak(unekoErabiltzailea);
		}
	}

	public void kargatuErabiltzaileDatuak(Erabiltzailea erab) {
		if (erab != null) {
			tfIzena.setText(erab.getIzena());
			tfAbizena1.setText(erab.getAbizena1());
			tfAbizena2.setText(erab.getAbizena2());
			tfEmail.setText(erab.getEmail());

			if (erab.getJaiotzeData() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(erab.getJaiotzeData());
				datePickerJaiotzeData.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH));
				datePickerJaiotzeData.getModel().setSelected(true);
			}
		}
	}

}
