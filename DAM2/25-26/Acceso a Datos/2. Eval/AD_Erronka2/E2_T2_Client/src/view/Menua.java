package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public final class Menua extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JPanel edukiPanel;
	private final String erabiltzailea;

	public Menua(String erabiltzailea) {
		this.erabiltzailea = erabiltzailea;

		setIconImage(Toolkit.getDefaultToolkit().getImage(FirstView.class.getResource("/img/elorrieta.png")));
		setTitle("Menua - EE Software");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);

		edukiPanel = new JPanel();
		edukiPanel.setBackground(new Color(240, 240, 240));
		edukiPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		edukiPanel.setLayout(null);
		setContentPane(edukiPanel);

		gehituGoiburua();
		gehituMenuBotoiak();
		gehituEskolaLogoa();
		gehituAzpiBotoiak();
	}

	private void gehituGoiburua() {
		JLabel ongiEtorria = new JLabel("Ongi etorri, " + erabiltzailea + "!");
		ongiEtorria.setFont(new Font("Tahoma", Font.BOLD, 24));
		ongiEtorria.setForeground(new Color(51, 102, 153));
		ongiEtorria.setBounds(50, 30, 600, 50);
		edukiPanel.add(ongiEtorria);

		JButton btnProfila = new JButton("");
		btnProfila.setBackground(new Color(51, 102, 153));
		ImageIcon originalIkonoa = new ImageIcon(Menua.class.getResource("/img/ic_profile.png"));
		Image tamainaldatuIrudia = originalIkonoa.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		btnProfila.setIcon(new ImageIcon(tamainaldatuIrudia));
		btnProfila.setBounds(738, 11, 36, 33);
		btnProfila.addActionListener(e -> {
			dispose();
			new Profila(erabiltzailea).setVisible(true);
		});
		edukiPanel.add(btnProfila);
	}

	private void gehituMenuBotoiak() {
		JButton btnOrdutegia = sortuMenuBotoia("Ordutegia", 200, 150);
		btnOrdutegia.addActionListener(e -> {
			dispose();
			new Ordutegiak(erabiltzailea).setVisible(true);
		});
		edukiPanel.add(btnOrdutegia);

		JButton btnKontsultak = sortuMenuBotoia("Kontsultak", 430, 150);
		btnKontsultak.addActionListener(e -> {
			dispose();
			new Kontsultak(erabiltzailea).setVisible(true);
		});
		edukiPanel.add(btnKontsultak);

		JButton btnBilerak = sortuMenuBotoia("Bilerak", 200, 270);
		btnBilerak.addActionListener(e -> {
			dispose();
			new Bilerak(erabiltzailea).setVisible(true);
		});
		edukiPanel.add(btnBilerak);

		JButton btnIkasleak = sortuMenuBotoia("Ikasleak", 430, 270);
		btnIkasleak.addActionListener(e -> {
			dispose();
			new Ikasleak(erabiltzailea).setVisible(true);
		});
		edukiPanel.add(btnIkasleak);
	}

	private JButton sortuMenuBotoia(String testua, int x, int y) {
		JButton botoia = new JButton(testua);
		botoia.setFont(new Font("Tahoma", Font.BOLD, 16));
		botoia.setBackground(new Color(70, 130, 180));
		botoia.setForeground(Color.WHITE);
		botoia.setBounds(x, y, 170, 80);
		return botoia;
	}

	private void gehituEskolaLogoa() {
		JLabel lblEskolaLogoa = new JLabel("");
		ImageIcon logoIkonoa = new ImageIcon(Menua.class.getResource("/img/elorrieta.png"));
		Image logoIrudia = logoIkonoa.getImage().getScaledInstance(150, 90, Image.SCALE_SMOOTH);
		lblEskolaLogoa.setIcon(new ImageIcon(logoIrudia));
		lblEskolaLogoa.setBounds(325, 390, 150, 90);
		edukiPanel.add(lblEskolaLogoa);
	}

	private void gehituAzpiBotoiak() {
		JButton btnSaioaItxi = new JButton("Saioa Itxi");
		btnSaioaItxi.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSaioaItxi.setBackground(new Color(220, 53, 69));
		btnSaioaItxi.setForeground(Color.WHITE);
		btnSaioaItxi.setBounds(584, 510, 90, 40);
		btnSaioaItxi.addActionListener(e -> {
			dispose();
			new FirstView().setVisible(true);
		});
		edukiPanel.add(btnSaioaItxi);

		JButton btnIrten = new JButton("Irten");
		btnIrten.setForeground(Color.WHITE);
		btnIrten.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnIrten.setBackground(new Color(192, 192, 192));
		btnIrten.setBounds(684, 510, 90, 40);
		btnIrten.addActionListener(e -> System.exit(0));
		edukiPanel.add(btnIrten);
	}
}