package controller;

import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.apache.commons.validator.routines.EmailValidator;

import dao.DAOGetter;
import dao.interfazeak.IErabiltzaileDAO;
import model.DAOException;
import model.Erabiltzailea;
import view.Lehioa;
import view.LoginPanel;

public class LoginController {

	private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

	private LoginPanel panel;
	private Lehioa lehioa;
	private IErabiltzaileDAO dao = DAOGetter.getInstance().getErabiltzaileaDAO();
	private JButton btnRegistratu;
	private JButton btnSaioaHasi;
	private JTextField textFieldEmail;
	private JPasswordField passwordFieldPasahitza;
	private Erabiltzailea sarbidea;
	private JProgressBar progressBar;

	public LoginController(LoginPanel panel, Lehioa lehioa) {
		this.panel = panel;
		this.lehioa = lehioa;
	}

	/**
	 * EventListner guztiak hasieratu
	 */
	public void listenersHasieratu() {
		btnRegistratu = panel.getBtnRegistratu();
		btnSaioaHasi = panel.getBtnSaioaHasi();
		textFieldEmail = panel.getTextFieldEmail();
		passwordFieldPasahitza = panel.getpasswordFieldPasahitza();

		btnRegistratu.addActionListener(e -> lehioa.cambiarVista("REGISTRO"));

		btnSaioaHasi.addActionListener(e -> saioaHasi());

		if (DAOGetter.getInstance().isLineazKanpo()) {
			JOptionPane.showMessageDialog(panel,
					"Lineaz kanpo zaude, aplikazioaren ezaugarri asko ez dira eskuragarri egongo.");
		} else {
			// Online
			btnRegistratu.setEnabled(true);
		}
	}

	/**
	 * Saioa hasten du. Lehenik Firebase bidez saiatzen da. Konexio errorerik
	 * badago, automatikoki aldatzen du ".dat" fitxategi lokalera (Modu Offlinea).
	 */
	private void saioaHasi() {
		final String email = textFieldEmail.getText().trim();
		char[] pasahitzaChar = passwordFieldPasahitza.getPassword();
		final String pasahitza = new String(pasahitzaChar).trim();
		Arrays.fill(pasahitzaChar, '\0');

		if (!EMAIL_VALIDATOR.isValid(email)) {
			JOptionPane.showMessageDialog(null, "emaila ez da baliozkoa", null, JOptionPane.ERROR_MESSAGE);
			return;
		} else if (pasahitza.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Pasahitza Bete", null, JOptionPane.ERROR_MESSAGE);
			return;
		}

		setKargatzen(true);

		new SwingWorker<Erabiltzailea, Void>() {
			private boolean offlineModua = false;

			@Override
			protected Erabiltzailea doInBackground() throws Exception {
				Erabiltzailea erabiltzailea = null;
				try {
					erabiltzailea = dao.saioaHasi(email, pasahitza);
				} catch (DAOException e) {
					JOptionPane.showMessageDialog(panel, "Ezin izan da saioa hasi. Barkatu eragozpenak.");
				}

				if (erabiltzailea != null) {
					Login.setErabil(erabiltzailea);

					if (!DAOGetter.getInstance().isLineazKanpo()) {
						backupProzesuaHasi();

					}
				}
				return erabiltzailea;
			}

			private void backupProzesuaHasi() {
				String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator
						+ "java";
				String classpath = System.getProperty("java.class.path");

				ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath,
						"utils.BackupsProzesua");
				builder.inheritIO(); // Para que enseñe los mensajes en la consola

				try {
					Process process = builder.start();
					process.onExit().thenAccept(p -> {
						int exitCode = p.exitValue();
						if (exitCode == 0) {
							System.out.println(
									"[INFO] Babes kopiak arrakastaz sortu dira.");
						} else {
							System.err.println(
									"[ERROR] Babes kopien prozesua " + exitCode
											+ " irteera-kodearekin amaitu da.");
						}
					});
				} catch (IOException e) {
					System.out.println("[WARN] Ezin izan dira babes kopiak sortu");
				}
			}

			@Override
			protected void done() {
				setKargatzen(false);

				try {
					sarbidea = get();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					erroreaJakinarazi();
					return;
				} catch (ExecutionException e) {
					erroreaJakinarazi();
					return;
				}

				if (sarbidea != null) {
					textFieldEmail.setText("");
					passwordFieldPasahitza.setText("");
					Login.erabil = sarbidea;

					if (offlineModua) {
						JOptionPane.showMessageDialog(panel,
								"Sare konexiorik gabe zaude. Lineaz kanpoko moduan (.dat) sartu zara.",
								"Lineaz kanpoko modua", JOptionPane.INFORMATION_MESSAGE);
					}

					lehioa.getPanelSecond().getCont().datuakKargatu();
					lehioa.cambiarVista("SECOND");
				} else {
					JOptionPane.showMessageDialog(panel,
							"E-maila edo pasahitza okerrak dira (edo fitxategi lokala hutsik dago)!",
							"Errorea saioa hastean", JOptionPane.ERROR_MESSAGE);
				}
			}
		}.execute();
	}

	/**
	 * Errorearen mezua erakusten du.
	 */
	private void erroreaJakinarazi() {
		JOptionPane.showMessageDialog(panel, "Ezin izan da saioa hasi!", "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Saioa hasteko prozesuan daudenean erakusten du (EDT ez da blokeatzen).
	 *
	 * @param kargatzen true saioa hasteko lanari dagokionez
	 */
	public void setKargatzen(boolean kargatzen) {

		btnSaioaHasi = panel.getBtnSaioaHasi();
		btnRegistratu = panel.getBtnRegistratu();
		progressBar = panel.getProgressBar();
		textFieldEmail = panel.getTextFieldEmail();
		passwordFieldPasahitza = panel.getpasswordFieldPasahitza();

		progressBar.setVisible(kargatzen);
		btnSaioaHasi.setEnabled(!kargatzen);
		btnRegistratu.setEnabled(!kargatzen);
		textFieldEmail.setEnabled(!kargatzen);
		passwordFieldPasahitza.setEnabled(!kargatzen);

		if (kargatzen) {
			panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			panel.setCursor(Cursor.getDefaultCursor());
		}
	}
}
