package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import controller.LoginController;

public class LoginPanel extends BasePanel {

	private static final long serialVersionUID = 1L;
	private LoginController cont;
	private JTextField textFieldEmail;
	private JPasswordField passwordFieldPasahitza;
	private JButton btnSaioaHasi;
	private JButton btnRegistratu;
	private JProgressBar progressBar;
	private URL imgURL;
	private JLabel lblLoginLogo;

	public LoginPanel(Lehioa lehioa) {

		cont = new LoginController(this, lehioa);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 30, 107, 145, 150, 30, 0 };
		gridBagLayout.rowHeights = new int[] { 30, 42, 42, 37, 30, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.5, 0.5, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.5, 0.0, 0.0, 0.0, 0.5, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		lblLoginLogo = new JLabel("");
		lblLoginLogo.setFont(FONT);
		GridBagConstraints gbc_lblLoginLogo = new GridBagConstraints();
		gbc_lblLoginLogo.gridwidth = 2;
		gbc_lblLoginLogo.anchor = GridBagConstraints.CENTER;
		gbc_lblLoginLogo.insets = new Insets(10, 0, 15, 5);
		gbc_lblLoginLogo.gridx = 2;
		gbc_lblLoginLogo.gridy = 0;
		add(lblLoginLogo, gbc_lblLoginLogo);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(FONT);
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.WEST;
		gbc_lblEmail.fill = GridBagConstraints.VERTICAL;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 1;
		gbc_lblEmail.gridy = 1;
		add(lblEmail, gbc_lblEmail);

		textFieldEmail = new JTextField();
		textFieldEmail.setFont(FONT);
		GridBagConstraints gbc_textFieldEmail = new GridBagConstraints();
		gbc_textFieldEmail.gridwidth = 2;
		gbc_textFieldEmail.fill = GridBagConstraints.BOTH;
		gbc_textFieldEmail.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldEmail.gridx = 2;
		gbc_textFieldEmail.gridy = 1;
		add(textFieldEmail, gbc_textFieldEmail);
		textFieldEmail.setColumns(10);

		JLabel lblPasahitza = new JLabel("Pasahitza");
		lblPasahitza.setFont(FONT);
		GridBagConstraints gbc_lblPasahitza = new GridBagConstraints();
		gbc_lblPasahitza.anchor = GridBagConstraints.WEST;
		gbc_lblPasahitza.fill = GridBagConstraints.VERTICAL;
		gbc_lblPasahitza.insets = new Insets(0, 0, 5, 5);
		gbc_lblPasahitza.gridx = 1;
		gbc_lblPasahitza.gridy = 2;
		add(lblPasahitza, gbc_lblPasahitza);

		passwordFieldPasahitza = new JPasswordField();
		passwordFieldPasahitza.setFont(FONT);
		GridBagConstraints gbc_passwordFieldPasahitza = new GridBagConstraints();
		gbc_passwordFieldPasahitza.gridwidth = 2;
		gbc_passwordFieldPasahitza.fill = GridBagConstraints.BOTH;
		gbc_passwordFieldPasahitza.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldPasahitza.gridx = 2;
		gbc_passwordFieldPasahitza.gridy = 2;
		add(passwordFieldPasahitza, gbc_passwordFieldPasahitza);
		passwordFieldPasahitza.setColumns(10);

		btnSaioaHasi = new JButton("Saioa Hasi");
		btnSaioaHasi.setFont(FONT);
		GridBagConstraints gbc_btnWorkoutHasi = new GridBagConstraints();
		gbc_btnWorkoutHasi.fill = GridBagConstraints.BOTH;
		gbc_btnWorkoutHasi.insets = new Insets(0, 0, 5, 5);
		gbc_btnWorkoutHasi.gridx = 2;
		gbc_btnWorkoutHasi.gridy = 3;
		add(btnSaioaHasi, gbc_btnWorkoutHasi);

		btnRegistratu = new JButton("Registratu");
		btnRegistratu.setEnabled(false);
		btnRegistratu.setFont(FONT);
		GridBagConstraints gbc_btnRegistratu = new GridBagConstraints();
		gbc_btnRegistratu.insets = new Insets(0, 0, 5, 5);
		gbc_btnRegistratu.fill = GridBagConstraints.BOTH;
		gbc_btnRegistratu.gridx = 3;
		gbc_btnRegistratu.gridy = 3;
		add(btnRegistratu, gbc_btnRegistratu);

		progressBar = new JProgressBar();
		progressBar.setFont(FONT);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.gridwidth = 2;
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.insets = new Insets(0, 0, 5, 5);
		gbc_progressBar.gridx = 2;
		gbc_progressBar.gridy = 4;
		add(progressBar, gbc_progressBar);
		progressBar.setIndeterminate(true);
		progressBar.setVisible(false);

		imgURL = getClass().getResource("/img/logo.png");
		if (imgURL != null) {
			ImageIcon originalIcon = new ImageIcon(imgURL);
			Image originalImg = originalIcon.getImage();
			Image scaledImg = originalImg.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
			lblLoginLogo.setIcon(new ImageIcon(scaledImg));
		}

		cont.listenersHasieratu();
	}

	public JPasswordField getPasswordFieldPasahitza() {
		return passwordFieldPasahitza;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setTextFieldEmail(JTextField textFieldEmail) {
		this.textFieldEmail = textFieldEmail;
	}

	public JTextField getTextFieldEmail() {
		return textFieldEmail;
	}

	public JPasswordField getpasswordFieldPasahitza() {
		return passwordFieldPasahitza;
	}

	public JButton getBtnSaioaHasi() {
		return btnSaioaHasi;
	}

	public JButton getBtnRegistratu() {
		return btnRegistratu;
	}
}
