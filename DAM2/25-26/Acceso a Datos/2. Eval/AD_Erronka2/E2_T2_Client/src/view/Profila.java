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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import utils.Profile;
import utils.SessionManager;

public final class Profila extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JPanel edukiPanel;
	private final JTextField izenaTestua;
	private final JTextField abizenakTestua;
	private final JTextField emailaTestua;
	private final JTextField erabiltzaileIzenaTestua;
	private final JTextField dniTestua;
	private final JTextField telefonoa1Testua;
	private final JTextField telefonoa2Testua;
	private final JTextField helbideaTestua;
	private final JTextField erabiltzaileMotaTestua;
	private final JLabel profilIrudiaEtiketa;
	private final JLabel erabiltzaileIdEtiketa;
	private final JLabel egoerakoEtiketa;
	private final String oraingkoErabiltzailea;

	private final Profile profilBezeroa;
	private Profile.ErabiltzaileProfilData oraingkoProfila;

	public Profila(String erabiltzailea) {
		this.oraingkoErabiltzailea = erabiltzailea;
		this.profilBezeroa = new Profile();

		setIconImage(Toolkit.getDefaultToolkit().getImage(Profila.class.getResource("/img/elorrieta.png")));
		setTitle("Erabiltzaile Profila - EE Software");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);

		edukiPanel = new JPanel();
		edukiPanel.setBackground(new Color(240, 240, 240));
		edukiPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		edukiPanel.setLayout(null);
		setContentPane(edukiPanel);

		gehituGoiburuIzenburua();

		JPanel profilIrudiPanel = sortuProfilIrudiPanel();
		edukiPanel.add(profilIrudiPanel);

		JPanel infoPertsonal = sortuInfoPertsonalPanel();
		edukiPanel.add(infoPertsonal);

		// Buscar los JTextField dinámicamente en lugar de usar índices fijos
		JTextField[] textFields = new JTextField[8];
		int textFieldIndex = 0;
		for (int i = 0; i < infoPertsonal.getComponentCount() && textFieldIndex < 8; i++) {
			if (infoPertsonal.getComponent(i) instanceof JTextField) {
				textFields[textFieldIndex++] = (JTextField) infoPertsonal.getComponent(i);
			}
		}

		// Asignar a los campos final
		erabiltzaileIzenaTestua = textFields[0];
		dniTestua = textFields[1];
		izenaTestua = textFields[2];
		abizenakTestua = textFields[3];
		emailaTestua = textFields[4];
		telefonoa1Testua = textFields[5];
		telefonoa2Testua = textFields[6];
		helbideaTestua = textFields[7];

		JPanel profesionalInfo = sortuProfesionalInfoPanel();
		edukiPanel.add(profesionalInfo);

		erabiltzaileMotaTestua = (JTextField) profesionalInfo.getComponent(1);
		erabiltzaileIdEtiketa = (JLabel) profesionalInfo.getComponent(2);

		gehituEkintzaBotoiak();

		egoerakoEtiketa = new JLabel("Datuak kargatzen...");
		egoerakoEtiketa.setFont(new Font("Tahoma", Font.BOLD, 12));
		egoerakoEtiketa.setForeground(Color.BLUE);
		egoerakoEtiketa.setBounds(301, 555, 400, 25);
		edukiPanel.add(egoerakoEtiketa);

		// Buscar el JLabel de la imagen de perfil dinámicamente
		JLabel tempProfilIrudia = null;
		for (int i = 0; i < profilIrudiPanel.getComponentCount(); i++) {
			if (profilIrudiPanel.getComponent(i) instanceof JLabel) {
				tempProfilIrudia = (JLabel) profilIrudiPanel.getComponent(i);
				break;
			}
		}
		profilIrudiaEtiketa = (tempProfilIrudia != null) ? tempProfilIrudia : new JLabel();

		kargatuErabiltzaileDatuak();
	}

	private void gehituGoiburuIzenburua() {
		JLabel izenburua = new JLabel("Erabiltzaile Profila");
		izenburua.setFont(new Font("Tahoma", Font.BOLD, 24));
		izenburua.setForeground(new Color(51, 102, 153));
		izenburua.setBounds(50, 20, 300, 40);
		edukiPanel.add(izenburua);
	}

	private JPanel sortuProfilIrudiPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new LineBorder(new Color(200, 200, 200), 2));
		panel.setBounds(50, 80, 180, 200);
		panel.setLayout(null);

		JLabel irudia = new JLabel("");
		irudia.setBounds(10, 10, 160, 160);

		try {
			ImageIcon profilIkonoa = new ImageIcon(Profila.class.getResource("/img/ic_profile.png"));
			Image profilIrudia = profilIkonoa.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
			irudia.setIcon(new ImageIcon(profilIrudia));
		} catch (Exception e) {
			System.err.println("Ezin izan da profil irudia kargatu");
		}
		panel.add(irudia);

		JButton btnAldatuArgazkia = new JButton("Aldatu Argazkia");
		btnAldatuArgazkia.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnAldatuArgazkia.setBackground(new Color(70, 130, 180));
		btnAldatuArgazkia.setForeground(Color.WHITE);
		btnAldatuArgazkia.setBounds(10, 175, 160, 20);
		btnAldatuArgazkia.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"Argazkia aldatzeko funtzioa laster eskuragarri", "Informazioa", JOptionPane.INFORMATION_MESSAGE));
		panel.add(btnAldatuArgazkia);

		return panel;
	}

	private JPanel sortuInfoPertsonalPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new TitledBorder(new LineBorder(new Color(51, 102, 153), 2), "Informazio Pertsonala",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 14), new Color(51, 102, 153)));
		panel.setBounds(270, 80, 580, 240);
		panel.setLayout(null);

		gehituEremu(panel, "Erabiltzailea:", 20, 30, 130, 30, true, true);
		gehituEremu(panel, "DNI:", 330, 30, 400, 30, false, false);
		gehituEremu(panel, "Izena:", 20, 70, 130, 70, false, false);
		gehituEremu(panel, "Abizenak:", 330, 70, 400, 70, false, false);
		gehituEremu(panel, "Emaila:", 20, 110, 130, 110, true, true);
		gehituEremu(panel, "Telefonoa 1:", 20, 150, 130, 150, false, false);
		gehituEremu(panel, "Telefonoa 2:", 330, 150, 400, 150, false, false);
		gehituEremu(panel, "Helbidea:", 20, 190, 130, 190, false, false);

		return panel;
	}

	private void gehituEremu(JPanel panel, String etiketa, int etiketaX, int etiketaY, int eremuaX, int eremuaY,
			boolean irakurgarria, boolean grisaItzala) {
		JLabel lbl = new JLabel(etiketa);
		lbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbl.setBounds(etiketaX, etiketaY, 100, 25);
		panel.add(lbl);

		JTextField txt = new JTextField();
		txt.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txt.setEditable(!irakurgarria);
		if (grisaItzala) {
			txt.setBackground(new Color(245, 245, 245));
		}

		int zabalera = eremuaX == 400 ? 150 : (eremuaX == 130 && eremuaY > 100 ? 420 : 180);
		txt.setBounds(eremuaX, eremuaY, zabalera, 25);
		panel.add(txt);
	}

	private JPanel sortuProfesionalInfoPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new TitledBorder(new LineBorder(new Color(51, 102, 153), 2), "Sistemaren Informazioa",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 14), new Color(51, 102, 153)));
		panel.setBounds(50, 340, 800, 100);
		panel.setLayout(null);

		JLabel lblMota = new JLabel("Erabiltzaile Mota:");
		lblMota.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMota.setBounds(20, 30, 120, 25);
		panel.add(lblMota);

		JTextField txtMota = new JTextField();
		txtMota.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtMota.setEditable(false);
		txtMota.setBackground(new Color(245, 245, 245));
		txtMota.setBounds(150, 30, 200, 25);
		panel.add(txtMota);

		JLabel lblId = new JLabel("Erabiltzaile ID: ---");
		lblId.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblId.setForeground(new Color(70, 130, 180));
		lblId.setBounds(20, 65, 200, 20);
		panel.add(lblId);

		return panel;
	}

	private void gehituEkintzaBotoiak() {

		JButton btnGorde = new JButton("💾 Gorde Aldaketak");
		btnGorde.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnGorde.setBackground(new Color(34, 139, 34));
		btnGorde.setForeground(Color.WHITE);
		btnGorde.setBounds(670, 471, 180, 40);
		btnGorde.addActionListener(e -> gordeErabiltzaileaDatuak());
		edukiPanel.add(btnGorde);

		JButton btnAtzera = new JButton("← Atzera Menura");
		btnAtzera.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAtzera.setBackground(new Color(70, 130, 180));
		btnAtzera.setForeground(Color.WHITE);
		btnAtzera.setBounds(50, 474, 150, 35);
		btnAtzera.addActionListener(e -> {
			dispose();
			new Menua(oraingkoErabiltzailea).setVisible(true);
		});
		edukiPanel.add(btnAtzera);
	}

	private void kargatuErabiltzaileDatuak() {
		egoerakoEtiketa.setText("Profil datuak kargatzen...");
		egoerakoEtiketa.setForeground(Color.BLUE);

		new Thread(() -> {
			try {
				Long erabiltzaileId = SessionManager.getInstance().getErabiltzaileId();

				if (erabiltzaileId == null) {
					EventQueue.invokeLater(() -> {
						egoerakoEtiketa.setText("❌ Errorea: Ez dago saio aktiborik");
						egoerakoEtiketa.setForeground(Color.RED);
						JOptionPane.showMessageDialog(this,
								"Ezin izan da erabiltzailearen informazioa lortu.\n" + "Mesedez, hasi saioa berriro.",
								"Saio Errorea", JOptionPane.ERROR_MESSAGE);

						kargatuAdibideDatuak();
					});
					return;
				}

				oraingkoProfila = profilBezeroa.lortuErabiltzaileProfila(erabiltzaileId);

				if (oraingkoProfila != null) {
					String motaIzena = profilBezeroa.lortuMotaIzena(oraingkoProfila.getMota());

					EventQueue.invokeLater(() -> {
						eguneratuProfilEremuak(oraingkoProfila, motaIzena);
						egoerakoEtiketa.setText("✓ Datuak zuzen kargatu dira");
						egoerakoEtiketa.setForeground(new Color(0, 128, 0));
					});
				} else {
					EventQueue.invokeLater(() -> {
						egoerakoEtiketa.setText("❌ Ez da erabiltzailea aurkitu");
						egoerakoEtiketa.setForeground(Color.RED);
						JOptionPane.showMessageDialog(this, "Ez dira daturik aurkitu oraingo erabiltzailearentzat.",
								"Erabiltzailea Ez da Aurkitu", JOptionPane.WARNING_MESSAGE);

						kargatuAdibideDatuak();
					});
				}

			} catch (Exception e) {
				EventQueue.invokeLater(() -> {
					egoerakoEtiketa.setText("❌ Datuak kargatzeko errorea");
					egoerakoEtiketa.setForeground(Color.RED);

					JOptionPane.showMessageDialog(this,
							"Zerbitzariarekin konektatzeko errorea:\n" + e.getMessage()
									+ "\n\nEgiaztatu zerbitzaria martxan dagoela.",
							"Konexio Errorea", JOptionPane.ERROR_MESSAGE);

					kargatuAdibideDatuak();
				});
			}
		}).start();
	}

	private void eguneratuProfilEremuak(Profile.ErabiltzaileProfilData profila, String motaIzena) {
		erabiltzaileIzenaTestua.setText(profila.getErabiltzaileIzena());
		izenaTestua.setText(profila.getIzena());
		abizenakTestua.setText(profila.getAbizenak());
		emailaTestua.setText(profila.getEmail());
		dniTestua.setText(profila.getDni());
		telefonoa1Testua.setText(profila.getTelefonoa1());
		telefonoa2Testua.setText(profila.getTelefonoa2());
		helbideaTestua.setText(profila.getHelbidea());
		erabiltzaileMotaTestua.setText(motaIzena != null ? motaIzena : profila.getMotaIzena());

		erabiltzaileIdEtiketa.setText("Erabiltzaile ID: " + profila.getId());

		if (profila.getArgazkiaUrl() != null && !profila.getArgazkiaUrl().isEmpty()) {
			System.out.println("Profil argazkia URL: " + profila.getArgazkiaUrl());
		}
	}

	private void kargatuAdibideDatuak() {
		erabiltzaileIzenaTestua.setText("erabiltzailea.adibidea");
		izenaTestua.setText("Erabiltzailea");
		abizenakTestua.setText("Adibidekoa");
		emailaTestua.setText("erabiltzailea@elorrieta.com");
		dniTestua.setText("12345678A");
		telefonoa1Testua.setText("+34 123 456 789");
		telefonoa2Testua.setText("");
		helbideaTestua.setText("Adibide Kalea, 123");
		erabiltzaileMotaTestua.setText("Erabiltzailea");
		erabiltzaileIdEtiketa.setText("Erabiltzaile ID: ---");
		egoerakoEtiketa.setText("⚠ Adibide datuak erakusten (konexiorik gabe)");
		egoerakoEtiketa.setForeground(new Color(255, 140, 0));
	}

	private void gordeErabiltzaileaDatuak() {
		egoerakoEtiketa.setText("Datuak gordetzen...");
		egoerakoEtiketa.setForeground(Color.BLUE);

		new Thread(() -> {
			try {
				Long erabiltzaileId = SessionManager.getInstance().getErabiltzaileId();

				if (erabiltzaileId == null) {
					EventQueue.invokeLater(() -> {
						egoerakoEtiketa.setText("❌ Errorea: Ez dago saio aktiborik");
						egoerakoEtiketa.setForeground(Color.RED);
						JOptionPane.showMessageDialog(this,
								"Ezin izan da erabiltzailea eguneratu.\n" + "Mesedez, hasi saioa berriro.",
								"Saio Errorea", JOptionPane.ERROR_MESSAGE);
					});
					return;
				}

				// Validate required fields
				String izena = izenaTestua.getText().trim();
				String abizenak = abizenakTestua.getText().trim();
				String dni = dniTestua.getText().trim();
				String helbidea = helbideaTestua.getText().trim();
				String telefonoa1 = telefonoa1Testua.getText().trim();
				String telefonoa2 = telefonoa2Testua.getText().trim();

				if (izena.isEmpty() || abizenak.isEmpty()) {
					EventQueue.invokeLater(() -> {
						egoerakoEtiketa.setText("❌ Izena eta abizenak beteta egon behar dira");
						egoerakoEtiketa.setForeground(Color.RED);
						JOptionPane.showMessageDialog(this, "Izena eta abizenak beteta egon behar dira.",
								"Balioztatze Errorea", JOptionPane.WARNING_MESSAGE);
					});
					return;
				}

				boolean arrakasta = profilBezeroa.eguneratuErabiltzailea(erabiltzaileId, izena, abizenak, dni, helbidea,
						telefonoa1, telefonoa2);

				EventQueue.invokeLater(() -> {
					if (arrakasta) {
						egoerakoEtiketa.setText("✓ Datuak ondo gorde dira");
						egoerakoEtiketa.setForeground(new Color(0, 128, 0));
						JOptionPane.showMessageDialog(this, "Zure datuak ondo eguneratu dira.", "Arrakasta",
								JOptionPane.INFORMATION_MESSAGE);

						// Reload to reflect saved changes
						kargatuErabiltzaileDatuak();
					} else {
						egoerakoEtiketa.setText("❌ Errorea datuak gordetzean");
						egoerakoEtiketa.setForeground(Color.RED);
						JOptionPane.showMessageDialog(this,
								"Ezin izan dira datuak gorde.\n" + "Mesedez, saiatu berriro.", "Errorea",
								JOptionPane.ERROR_MESSAGE);
					}
				});

			} catch (Exception e) {
				EventQueue.invokeLater(() -> {
					egoerakoEtiketa.setText("❌ Ustekabeko errorea");
					egoerakoEtiketa.setForeground(Color.RED);

					JOptionPane.showMessageDialog(this,
							"Zerbitzariarekin konektatzeko errorea:\n" + e.getMessage()
									+ "\n\nEgiaztatu zerbitzaria martxan dagoela.",
							"Konexio Errorea", JOptionPane.ERROR_MESSAGE);
				});
			}
		}).start();
	}
}