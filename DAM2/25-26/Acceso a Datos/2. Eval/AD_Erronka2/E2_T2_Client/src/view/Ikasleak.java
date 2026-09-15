package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.GetAllUsers;
import utils.SessionManager;
import utils.UserListTable;

public final class Ikasleak extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JPanel edukiPanel;
	private final UserListTable erabiltzaileTaula;
	private final JTextField bilaketaEremua;
	private final JButton ordutegiaBotoiaIkusi;
	private final JButton freskatuBotoia;
	private final JLabel hautatutakoErabiltzaileEtiketa;
	private final JLabel egoerakoEtiketa;

	public Ikasleak(String erabiltzailea) {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		setTitle("Ikasleen Kudeaketa - Elorrieta");

		edukiPanel = new JPanel();
		edukiPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		edukiPanel.setLayout(new BorderLayout());
		setContentPane(edukiPanel);

		erabiltzaileTaula = new UserListTable("Ikasleak");
		edukiPanel.add(erabiltzaileTaula, BorderLayout.CENTER);

		JPanel goiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JLabel bilaketaEtiketa = new JLabel("Bilatu:");
		bilaketaEremua = new JTextField(20);
		bilaketaEremua.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				erabiltzaileTaula.iragaziErabiltzaileak(bilaketaEremua.getText());
			}
		});

		freskatuBotoia = new JButton("🔄 Eguneratu");
		freskatuBotoia.addActionListener(e -> kargatuIkasleakZerbitzaritik());

		egoerakoEtiketa = new JLabel("Prest");

		goiPanel.add(bilaketaEtiketa);
		goiPanel.add(bilaketaEremua);
		goiPanel.add(freskatuBotoia);
		goiPanel.add(egoerakoEtiketa);

		JButton btnAtzera = new JButton("Atzera");
		btnAtzera.setForeground(Color.WHITE);
		btnAtzera.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAtzera.setBackground(new Color(108, 117, 125));
		btnAtzera.addActionListener(e -> {
			dispose();
			new Menua(erabiltzailea).setVisible(true);
		});
		goiPanel.add(btnAtzera);

		edukiPanel.add(goiPanel, BorderLayout.NORTH);

		JPanel azpiPanel = new JPanel(new FlowLayout());
		hautatutakoErabiltzaileEtiketa = new JLabel("Ez da ikaslerik hautatu");

		ordutegiaBotoiaIkusi = new JButton("Ordutegia Ikusi");
		ordutegiaBotoiaIkusi.setEnabled(false);
		ordutegiaBotoiaIkusi.addActionListener(e -> {
			Object[] erabiltzaileInfo = erabiltzaileTaula.getHautatutakoErabiltzaileInfo();
			if (erabiltzaileInfo != null) {
				JOptionPane.showMessageDialog(this, "Irekitzen: " + erabiltzaileInfo[1] + " " + erabiltzaileInfo[2]
						+ "\nEmaila: " + erabiltzaileInfo[3], "Ikaslearen Ordutegia", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		azpiPanel.add(hautatutakoErabiltzaileEtiketa);
		azpiPanel.add(ordutegiaBotoiaIkusi);
		edukiPanel.add(azpiPanel, BorderLayout.SOUTH);

		erabiltzaileTaula.gehituErabiltzaileHautatzeEntzulea((erabiltzaileId, erabiltzaileIzena, erabiltzaileMota) -> {
			hautatutakoErabiltzaileEtiketa
					.setText("Hautatutako ikaslea: " + erabiltzaileIzena + " (ID: " + erabiltzaileId + ")");
			ordutegiaBotoiaIkusi.setEnabled(true);
		});

		kargatuIkasleakZerbitzaritik();
	}

	private void kargatuIkasleakZerbitzaritik() {
		freskatuBotoia.setEnabled(false);
		egoerakoEtiketa.setText("Ikasleak kargatzen...");

		new Thread(() -> {
			try {
				Long profeId = SessionManager.getInstance().getErabiltzaileId();
				
				if (profeId == null) {
					EventQueue.invokeLater(() -> {
						egoerakoEtiketa.setText("❌ Ez dago saio aktiborik");
						freskatuBotoia.setEnabled(true);
						JOptionPane.showMessageDialog(this,
							"Ezin izan dira ikasleak kargatu.\nMesedez, hasi saioa berriro.",
							"Saio Errorea",
							JOptionPane.ERROR_MESSAGE);
					});
					return;
				}

				GetAllUsers bezeroa = new GetAllUsers();
				// Obtener solo los alumnos del profesor logueado
				List<GetAllUsers.ErabiltzaileData> ikasleak = bezeroa.lortuAlumnoakIrakaslearenArabera(profeId);

				EventQueue.invokeLater(() -> {
					erabiltzaileTaula.garbituerabiltzaileak();

					for (GetAllUsers.ErabiltzaileData ikaslea : ikasleak) {
						erabiltzaileTaula.gehituErabiltzailea(ikaslea.getId().intValue(), ikaslea.getIzena(),
								ikaslea.getAbizenak(), ikaslea.getEmail(), ikaslea.getMotaIzena());
					}

					egoerakoEtiketa.setText("✓ " + ikasleak.size() + " ikasle kargatuta");
					freskatuBotoia.setEnabled(true);

					if (ikasleak.isEmpty()) {
						JOptionPane.showMessageDialog(this,
								"Ez duzu ikaslerik une honetan.\nEgiaztatu zure moduluetan ikasleak badaudela.",
								"Ikaslerik gabe", JOptionPane.INFORMATION_MESSAGE);
					}
				});

			} catch (Exception e) {
				EventQueue.invokeLater(() -> {
					egoerakoEtiketa.setText("❌ Kargatzeko errorea");
					freskatuBotoia.setEnabled(true);

					JOptionPane.showMessageDialog(this,
							"Zerbitzariarekin konektatzeko errorea:\n" + e.getMessage()
									+ "\n\nEgiaztatu zerbitzaria 6000 portuan martxan dagoela.",
							"Konexio Errorea", JOptionPane.ERROR_MESSAGE);

					kargatuAdibideDatuak();
				});
			}
		}).start();
	}

	private void kargatuAdibideDatuak() {
		erabiltzaileTaula.garbituerabiltzaileak();
		erabiltzaileTaula.gehituErabiltzailea(1, "Jon", "García López", "jon.garcia@elorrieta.com", "Ikaslea");
		erabiltzaileTaula.gehituErabiltzailea(2, "María", "Fernández Silva", "maria.fernandez@elorrieta.com",
				"Ikaslea");
		erabiltzaileTaula.gehituErabiltzailea(3, "Carlos", "Rodríguez Martín", "carlos.rodriguez@elorrieta.com",
				"Ikaslea");
		erabiltzaileTaula.gehituErabiltzailea(4, "Ane", "López González", "ane.lopez@elorrieta.com", "Ikaslea");
		erabiltzaileTaula.gehituErabiltzailea(5, "Mikel", "Sánchez Ruiz", "mikel.sanchez@elorrieta.com", "Ikaslea");
		egoerakoEtiketa.setText("⚠ Adibide datuak (konexiorik gabe)");
	}
}