package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import utils.Meetings;
import utils.SessionManager;

public final class Bilerak extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JPanel edukiPanel;
	private final JTable bilerakTabla;
	private final DefaultTableModel taulaModeloa;
	private final JButton btnFreskatu;
	private final JButton btnIkusiXehetasunak;
	private final JButton btnIragaziIrakaslea;
	private final JButton btnIragaziIkaslea;
	private final JButton btnErakutsiGuztiak;
	private final JLabel egoerakoEtiketa;
	private final JLabel guztiraEtiketa;
	private final JComboBox<String> egoerakoIragazkiCombo;
	private final JTextField bilatuTestua;
	private final Meetings bileraBezeroa;
	private final String erabiltzailea;
	private List<Meetings.BileraData> oraingkoBilerak;

	@SuppressWarnings("unchecked")
	public Bilerak(String erabiltzailea) {
		this.bileraBezeroa = new Meetings();
		this.erabiltzailea = erabiltzailea;

		setTitle("Bileren Kudeaketa - Elorrieta");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);

		edukiPanel = new JPanel();
		edukiPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		edukiPanel.setLayout(new BorderLayout(10, 10));
		setContentPane(edukiPanel);

		JPanel goiPanel = sortuGoiPanel();
		edukiPanel.add(goiPanel, BorderLayout.NORTH);

		String[] zutabeak = { "ID", "Izenburua", "Egoera", "Irakasle ID", "Ikasle ID", "Data", "Ordua", "Gela",
				"Gaia" };

		taulaModeloa = new DefaultTableModel(zutabeak, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		bilerakTabla = new JTable(taulaModeloa);
		bilerakTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bilerakTabla.setRowHeight(25);
		bilerakTabla.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
		bilerakTabla.getTableHeader().setBackground(new Color(70, 130, 180));
		bilerakTabla.getTableHeader().setForeground(Color.WHITE);

		konfiguratuZutabeZabalera();

		JScrollPane scrollPane = new JScrollPane(bilerakTabla);
		JPanel zentroPanel = new JPanel(new BorderLayout());
		zentroPanel.add(scrollPane, BorderLayout.CENTER);
		edukiPanel.add(zentroPanel, BorderLayout.CENTER);

		JPanel azpiPanel = sortuAzpiPanel();
		edukiPanel.add(azpiPanel, BorderLayout.SOUTH);

		// Obtener componentes del goiPanel (BorderLayout: NORTH=titulo, CENTER=iragazkiPanel, SOUTH=egoerakoPanel)
		JPanel iragazkiPanel = null;
		JPanel egoerakoPanel = null;
		for (int i = 0; i < goiPanel.getComponentCount(); i++) {
			Component comp = goiPanel.getComponent(i);
			if (comp instanceof JPanel) {
				JPanel panel = (JPanel) comp;
				if (panel.getLayout() instanceof FlowLayout) {
					if (panel.getComponentCount() > 5) {
						iragazkiPanel = panel;
					} else if (panel.getComponentCount() >= 3) {
						egoerakoPanel = panel;
					}
				}
			}
		}
		
		// Asignar componentes del egoerakoPanel
		if (egoerakoPanel != null && egoerakoPanel.getComponentCount() >= 3) {
			egoerakoEtiketa = (JLabel) egoerakoPanel.getComponent(0);
			guztiraEtiketa = (JLabel) egoerakoPanel.getComponent(2);
		} else {
			throw new IllegalStateException("No se pudo encontrar el panel de estado");
		}
		
		// Asignar componentes del iragazkiPanel
		if (iragazkiPanel != null && iragazkiPanel.getComponentCount() >= 6) {
			bilatuTestua = (JTextField) iragazkiPanel.getComponent(1);
			egoerakoIragazkiCombo = (JComboBox<String>) iragazkiPanel.getComponent(3);
			btnFreskatu = (JButton) iragazkiPanel.getComponent(4);
			btnErakutsiGuztiak = (JButton) iragazkiPanel.getComponent(5);
		} else {
			throw new IllegalStateException("No se pudo encontrar el panel de filtros");
		}
		
		// Asignar componentes del azpiPanel
		if (azpiPanel.getComponentCount() >= 1) {
			Component primeraFila = azpiPanel.getComponent(0);
			if (primeraFila instanceof JPanel && ((JPanel) primeraFila).getComponentCount() >= 3) {
				btnIkusiXehetasunak = (JButton) ((JPanel) primeraFila).getComponent(0);
				btnIragaziIrakaslea = (JButton) ((JPanel) primeraFila).getComponent(1);
				btnIragaziIkaslea = (JButton) ((JPanel) primeraFila).getComponent(2);
			} else {
				throw new IllegalStateException("No se pudo encontrar el panel de botones");
			}
		} else {
			throw new IllegalStateException("No se pudo encontrar el panel inferior");
		}

		kargatuBileraGuztiak();
	}

	private JPanel sortuGoiPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		JLabel izenburua = new JLabel("Bileren Kudeaketa");
		izenburua.setFont(new Font("Tahoma", Font.BOLD, 24));
		izenburua.setForeground(new Color(51, 102, 153));
		panel.add(izenburua, BorderLayout.NORTH);

		JPanel iragazkiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

		JLabel bilatuEtiketa = new JLabel("Bilatu:");
		JTextField bilatu = new JTextField(15);
		bilatu.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				iragaziBilerak();
			}
		});

		JLabel egoerakoIragazkiEtiketa = new JLabel("Egoera:");
		JComboBox<String> egoerakoIragazkia = new JComboBox<>();
		egoerakoIragazkia.setModel(
				new DefaultComboBoxModel<>(new String[] { "Guztiak", "Onartzeke", "Onartua", "Ukatua", "Gatazka" }));
		egoerakoIragazkia.addActionListener(e -> iragaziBilerak());

		JButton freskatu = new JButton("🔄 Eguneratu");
		freskatu.addActionListener(e -> kargatuBileraGuztiak());

		JButton erakutsiGuztiak = new JButton("Bilera Guztiak");
		erakutsiGuztiak.addActionListener(e -> kargatuBileraGuztiak());

		iragazkiPanel.add(bilatuEtiketa);
		iragazkiPanel.add(bilatu);
		iragazkiPanel.add(egoerakoIragazkiEtiketa);
		iragazkiPanel.add(egoerakoIragazkia);
		iragazkiPanel.add(freskatu);
		iragazkiPanel.add(erakutsiGuztiak);

		JButton btnAtzera = new JButton("Atzera");
		btnAtzera.setForeground(Color.WHITE);
		btnAtzera.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAtzera.setBackground(new Color(108, 117, 125));
		btnAtzera.addActionListener(e -> {
			dispose();
			new Menua(erabiltzailea).setVisible(true);
		});
		iragazkiPanel.add(btnAtzera);

		panel.add(iragazkiPanel, BorderLayout.CENTER);

		JPanel egoerakoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel egoera = new JLabel("Prest");
		JLabel guztira = new JLabel("Guztira: 0 bilera");
		guztira.setFont(new Font("Tahoma", Font.BOLD, 12));
		egoerakoPanel.add(egoera);
		egoerakoPanel.add(new JLabel(" | "));
		egoerakoPanel.add(guztira);
		panel.add(egoerakoPanel, BorderLayout.SOUTH);

		return panel;
	}

	private void konfiguratuZutabeZabalera() {
		bilerakTabla.getColumnModel().getColumn(0).setPreferredWidth(50);
		bilerakTabla.getColumnModel().getColumn(1).setPreferredWidth(150);
		bilerakTabla.getColumnModel().getColumn(2).setPreferredWidth(80);
		bilerakTabla.getColumnModel().getColumn(3).setPreferredWidth(80);
		bilerakTabla.getColumnModel().getColumn(4).setPreferredWidth(80);
		bilerakTabla.getColumnModel().getColumn(5).setPreferredWidth(100);
		bilerakTabla.getColumnModel().getColumn(6).setPreferredWidth(60);
		bilerakTabla.getColumnModel().getColumn(7).setPreferredWidth(60);
		bilerakTabla.getColumnModel().getColumn(8).setPreferredWidth(200);
	}

	private JPanel sortuAzpiPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		JPanel botoiPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

		JButton ikusiXehetasunak = new JButton("📋 Ikusi Xehetasunak");
		ikusiXehetasunak.setEnabled(false);
		ikusiXehetasunak.addActionListener(e -> ikusiXehetasunakBilera());

		JButton iragaziIrakaslea = new JButton("👨‍🏫 Iragazi Irakasleaz");
		iragaziIrakaslea.addActionListener(e -> iragaziIrakasleaArabera());

		JButton iragaziIkaslea = new JButton("👨‍🎓 Iragazi Ikasleaz");
		iragaziIkaslea.addActionListener(e -> iragaziIkasleaArabera());

		botoiPanel1.add(ikusiXehetasunak);
		botoiPanel1.add(iragaziIrakaslea);
		botoiPanel1.add(iragaziIkaslea);

		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel info = new JLabel("Hautatu bilera bat xehetasunak ikusteko");
		info.setFont(new Font("Tahoma", Font.ITALIC, 11));
		info.setForeground(Color.GRAY);
		infoPanel.add(info);

		panel.add(botoiPanel1);
		panel.add(infoPanel);

		bilerakTabla.getSelectionModel()
				.addListSelectionListener(e -> ikusiXehetasunak.setEnabled(bilerakTabla.getSelectedRow() != -1));

		return panel;
	}

	private void kargatuBileraGuztiak() {
		btnFreskatu.setEnabled(false);
		egoerakoEtiketa.setText("Bilerak kargatzen...");
		egoerakoEtiketa.setForeground(Color.BLUE);

		new Thread(() -> {
			try {
				// Obtener el ID del profesor desde SessionManager
				Long irakasleaId = SessionManager.getInstance().getErabiltzaileId();
				if (irakasleaId != null) {
					oraingkoBilerak = bileraBezeroa.lortuBilerakIrakasleaArabera(irakasleaId);
				} else {
					// Fallback: cargar todas las reuniones si no hay ID
					oraingkoBilerak = bileraBezeroa.lortuBileraGuztiak();
				}

				EventQueue.invokeLater(() -> {
					eguneratuTabla(oraingkoBilerak);
					egoerakoEtiketa.setText("✓ Bilerak zuzen kargatu dira");
					egoerakoEtiketa.setForeground(new Color(0, 128, 0));
					guztiraEtiketa.setText("Guztira: " + oraingkoBilerak.size() + " bilera");
					btnFreskatu.setEnabled(true);

					if (oraingkoBilerak.isEmpty()) {
						JOptionPane.showMessageDialog(this, "Ez da bilerarik aurkitu sisteman.", "Daturik gabe",
								JOptionPane.INFORMATION_MESSAGE);
					}
				});

			} catch (Exception e) {
				EventQueue.invokeLater(() -> {
					egoerakoEtiketa.setText("❌ Kargatzeko errorea");
					egoerakoEtiketa.setForeground(Color.RED);
					btnFreskatu.setEnabled(true);

					JOptionPane.showMessageDialog(this, "Zerbitzariarekin konektatzeko errorea:\n" + e.getMessage(),
							"Konexio Errorea", JOptionPane.ERROR_MESSAGE);
				});
			}
		}).start();
	}

	private void iragaziIrakasleaArabera() {
		String sarrera = JOptionPane.showInputDialog(this, "Sartu irakaslearen ID:", "Iragazi Irakasleaz",
				JOptionPane.QUESTION_MESSAGE);

		if (sarrera != null && !sarrera.trim().isEmpty()) {
			try {
				long irakasleaId = Long.parseLong(sarrera.trim());
				kargatuBilerakIrakasleaArabera(irakasleaId);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "ID-ak zenbaki baliozkoa izan behar du", "Errorea",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void kargatuBilerakIrakasleaArabera(long irakasleaId) {
		egoerakoEtiketa.setText("Irakasle " + irakasleaId + "aren bilerak kargatzen...");
		egoerakoEtiketa.setForeground(Color.BLUE);

		new Thread(() -> {
			try {
				oraingkoBilerak = bileraBezeroa.lortuBilerakIrakasleaArabera(irakasleaId);

				EventQueue.invokeLater(() -> {
					eguneratuTabla(oraingkoBilerak);
					egoerakoEtiketa.setText("✓ Irakasle " + irakasleaId + "aren bilerak");
					egoerakoEtiketa.setForeground(new Color(0, 128, 0));
					guztiraEtiketa.setText("Guztira: " + oraingkoBilerak.size() + " bilera");

					if (oraingkoBilerak.isEmpty()) {
						JOptionPane.showMessageDialog(this, "Ez da bilerarik aurkitu ID honekin: " + irakasleaId,
								"Emaitzarik gabe", JOptionPane.INFORMATION_MESSAGE);
					}
				});

			} catch (Exception e) {
				EventQueue.invokeLater(() -> {
					egoerakoEtiketa.setText("❌ Kargatzeko errorea");
					egoerakoEtiketa.setForeground(Color.RED);
					JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage(), "Errorea",
							JOptionPane.ERROR_MESSAGE);
				});
			}
		}).start();
	}

	private void iragaziIkasleaArabera() {
		String sarrera = JOptionPane.showInputDialog(this, "Sartu ikaslearen ID:", "Iragazi Ikasleaz",
				JOptionPane.QUESTION_MESSAGE);

		if (sarrera != null && !sarrera.trim().isEmpty()) {
			try {
				long ikasleaId = Long.parseLong(sarrera.trim());
				kargatuBilerakIkasleaArabera(ikasleaId);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "ID-ak zenbaki baliozkoa izan behar du", "Errorea",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void kargatuBilerakIkasleaArabera(long ikasleaId) {
		egoerakoEtiketa.setText("Ikasle " + ikasleaId + "aren bilerak kargatzen...");
		egoerakoEtiketa.setForeground(Color.BLUE);

		new Thread(() -> {
			try {
				oraingkoBilerak = bileraBezeroa.lortuBilerakIkasleaArabera(ikasleaId);

				EventQueue.invokeLater(() -> {
					eguneratuTabla(oraingkoBilerak);
					egoerakoEtiketa.setText("✓ Ikasle " + ikasleaId + "aren bilerak");
					egoerakoEtiketa.setForeground(new Color(0, 128, 0));
					guztiraEtiketa.setText("Guztira: " + oraingkoBilerak.size() + " bilera");

					if (oraingkoBilerak.isEmpty()) {
						JOptionPane.showMessageDialog(this, "Ez da bilerarik aurkitu ID honekin: " + ikasleaId,
								"Emaitzarik gabe", JOptionPane.INFORMATION_MESSAGE);
					}
				});

			} catch (Exception e) {
				EventQueue.invokeLater(() -> {
					egoerakoEtiketa.setText("❌ Kargatzeko errorea");
					egoerakoEtiketa.setForeground(Color.RED);
					JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage(), "Errorea",
							JOptionPane.ERROR_MESSAGE);
				});
			}
		}).start();
	}

	private void iragaziBilerak() {
		if (oraingkoBilerak == null)
			return;

		String testuaBilaketa = bilatuTestua.getText().toLowerCase().trim();
		String egoerakoIragazkia = (String) egoerakoIragazkiCombo.getSelectedItem();

		List<Meetings.BileraData> bilerakIragaziak = oraingkoBilerak.stream().filter(b -> {
			boolean batdatorkiTestuarekin = testuaBilaketa.isEmpty()
					|| (b.getIzenburua() != null && b.getIzenburua().toLowerCase().contains(testuaBilaketa))
					|| (b.getGaia() != null && b.getGaia().toLowerCase().contains(testuaBilaketa))
					|| (b.getGela() != null && b.getGela().toLowerCase().contains(testuaBilaketa));

			boolean batdatorkiEgoerarekin = egoerakoIragazkia.equals("Guztiak")
					|| b.getEgoeraEuskera().equals(egoerakoIragazkia);

			return batdatorkiTestuarekin && batdatorkiEgoerarekin;
		}).toList();

		eguneratuTabla(bilerakIragaziak);
		guztiraEtiketa.setText("Erakusten: " + bilerakIragaziak.size() + " / " + oraingkoBilerak.size() + " bilera");
	}

	private void eguneratuTabla(List<Meetings.BileraData> bilerak) {
		taulaModeloa.setRowCount(0);

		for (Meetings.BileraData bilera : bilerak) {
			Object[] errenkada = { bilera.getBileraId(), bilera.getIzenburua(), bilera.getEgoeraEuskera(),
					bilera.getIrakasleaId(), bilera.getIkasleaId(), bilera.getDataSoilik(), bilera.getOrduaSoilik(),
					bilera.getGela(), bilera.getGaia() };
			taulaModeloa.addRow(errenkada);
		}
	}

	private void ikusiXehetasunakBilera() {
		int hautatutakoErrenkada = bilerakTabla.getSelectedRow();
		if (hautatutakoErrenkada == -1)
			return;

		Long bileraId = (Long) taulaModeloa.getValueAt(hautatutakoErrenkada, 0);

		Meetings.BileraData bilera = oraingkoBilerak.stream().filter(b -> b.getBileraId().equals(bileraId)).findFirst()
				.orElse(null);

		if (bilera == null)
			return;

		JPanel xehetasunakPanel = new JPanel(new GridLayout(10, 2, 10, 10));
		xehetasunakPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		xehetasunakPanel.add(new JLabel("Bilera ID:"));
		xehetasunakPanel.add(new JLabel(bilera.getBileraId().toString()));

		xehetasunakPanel.add(new JLabel("Izenburua:"));
		xehetasunakPanel.add(new JLabel(bilera.getIzenburua()));

		xehetasunakPanel.add(new JLabel("Egoera:"));
		xehetasunakPanel.add(new JLabel(bilera.getEgoeraEuskera() + " / " + bilera.getEgoeraeus()));

		xehetasunakPanel.add(new JLabel("Irakasle ID:"));
		xehetasunakPanel.add(new JLabel(bilera.getIrakasleaId() != null ? bilera.getIrakasleaId().toString() : "E/E"));

		xehetasunakPanel.add(new JLabel("Ikasle ID:"));
		xehetasunakPanel.add(new JLabel(bilera.getIkasleaId() != null ? bilera.getIkasleaId().toString() : "E/E"));

		xehetasunakPanel.add(new JLabel("Data eta Ordua:"));
		xehetasunakPanel.add(new JLabel(bilera.getDataFormateatua()));

		xehetasunakPanel.add(new JLabel("Gela:"));
		xehetasunakPanel.add(new JLabel(bilera.getGela()));

		xehetasunakPanel.add(new JLabel("Zentroa:"));
		xehetasunakPanel.add(new JLabel(bilera.getZentroId()));

		xehetasunakPanel.add(new JLabel("Gaia:"));
		JLabel gaiaEtiketa = new JLabel("<html>" + bilera.getGaia() + "</html>");
		xehetasunakPanel.add(gaiaEtiketa);

		JOptionPane.showMessageDialog(this, xehetasunakPanel, "Bileraren Xehetasunak", JOptionPane.INFORMATION_MESSAGE);
	}
}