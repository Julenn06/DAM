package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.CheckLogin;
import utils.SessionManager;

public final class FirstView extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JPanel edukiPanel;
	private final JTextField erabiltzaileEremua;
	private final JPasswordField pasahitzaEremua;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				FirstView markoa = new FirstView();
				markoa.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public FirstView() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(FirstView.class.getResource("/img/elorrieta.png")));
		setTitle("Login - EE Software");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);

		edukiPanel = new JPanel();
		edukiPanel.setBackground(new Color(240, 240, 240));
		edukiPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		edukiPanel.setLayout(null);
		setContentPane(edukiPanel);

		gehituIzenburua();
		gehituLogoa();

		JLabel lblErabiltzailea = sortuEtiketa("Erabiltzailea:", 250, 260);
		edukiPanel.add(lblErabiltzailea);

		erabiltzaileEremua = sortuTestuEremua(250, 285);
		edukiPanel.add(erabiltzaileEremua);

		JLabel lblPasahitza = sortuEtiketa("Pasahitza:", 250, 340);
		edukiPanel.add(lblPasahitza);

		pasahitzaEremua = sortuPasahitzaEremua(250, 365);
		edukiPanel.add(pasahitzaEremua);

		JButton btnLogin = sortuLoginBotoia();
		edukiPanel.add(btnLogin);

		gehituOinIzenburua();
	}

	private void gehituIzenburua() {
		JLabel izenburua = new JLabel("Eskola Kudeaketa Sistema");
		izenburua.setFont(new Font("Tahoma", Font.BOLD, 28));
		izenburua.setForeground(new Color(51, 102, 153));
		izenburua.setBounds(200, 30, 400, 50);
		edukiPanel.add(izenburua);
	}

	private void gehituLogoa() {
		JLabel lblLogoa = new JLabel();
		lblLogoa.setOpaque(true);
		lblLogoa.setBackground(Color.WHITE);
		lblLogoa.setBounds(300, 100, 200, 120);

		ImageIcon logoIkonoa = new ImageIcon(getClass().getResource("/img/elorrieta.png"));
		Image logoIrudia = logoIkonoa.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH);
		lblLogoa.setIcon(new ImageIcon(logoIrudia));
		edukiPanel.add(lblLogoa);
	}

	private JLabel sortuEtiketa(String testua, int x, int y) {
		JLabel etiketa = new JLabel(testua);
		etiketa.setFont(new Font("Tahoma", Font.BOLD, 16));
		etiketa.setForeground(new Color(51, 102, 153));
		etiketa.setBounds(x, y, 120, 25);
		return etiketa;
	}

	private JTextField sortuTestuEremua(int x, int y) {
		JTextField eremua = new JTextField();
		eremua.setFont(new Font("Tahoma", Font.PLAIN, 14));
		eremua.setBounds(x, y, 300, 35);
		eremua.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
				javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		eremua.setColumns(10);
		return eremua;
	}

	private JPasswordField sortuPasahitzaEremua(int x, int y) {
		JPasswordField eremua = new JPasswordField();
		eremua.setFont(new Font("Tahoma", Font.PLAIN, 14));
		eremua.setBounds(x, y, 300, 35);
		eremua.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
				javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		return eremua;
	}

	private JButton sortuLoginBotoia() {
		JButton btnLogin = new JButton("Sartu");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnLogin.setBackground(new Color(70, 130, 180));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBounds(350, 430, 100, 45);
		btnLogin.setFocusPainted(false);

		btnLogin.addActionListener(e -> {
			String erabiltzailea = erabiltzaileEremua.getText();
			String pasahitza = new String(pasahitzaEremua.getPassword());

			btnLogin.setEnabled(false);
			btnLogin.setText("Balidatzen...");

			new Thread(() -> {
				CheckLogin checkLogin = new CheckLogin();
				CheckLogin.LoginErantzuna erantzuna = checkLogin.balidatuLogin(erabiltzailea, pasahitza);

				EventQueue.invokeLater(() -> {
					if (erantzuna.isArrakastatsua()) {
						SessionManager.getInstance().saioaHasi(erantzuna.getErabiltzailea());

						System.out.println("Login arrakastatsua: " + erantzuna.getErabiltzailea().getIzenOsoa());
						dispose();
						new Menua(erantzuna.getErabiltzailea().getErabiltzaileIzena()).setVisible(true);
					} else {
						btnLogin.setEnabled(true);
						btnLogin.setText("Sartu");

						JOptionPane.showMessageDialog(FirstView.this, erantzuna.getMezua(), "Login Errorea",
								JOptionPane.ERROR_MESSAGE);
					}
				});
			}).start();
		});

		return btnLogin;
	}

	private void gehituOinIzenburua() {
		JLabel oinIzenburua = new JLabel("Elorrieta Erreka Mari BHI");
		oinIzenburua.setFont(new Font("Tahoma", Font.ITALIC, 12));
		oinIzenburua.setForeground(new Color(102, 102, 102));
		oinIzenburua.setBounds(320, 520, 200, 20);
		edukiPanel.add(oinIzenburua);
	}
}