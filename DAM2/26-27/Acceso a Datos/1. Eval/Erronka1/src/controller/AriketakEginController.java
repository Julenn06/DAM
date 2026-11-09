package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;

import dao.DAOGetter;
import dao.interfazeak.IErabiltzaileDAO;
import dao.interfazeak.IHistorikoaDAO;
import dao.interfazeak.IWorkoutDAO;
import dao.offline.OfflineWorkoutDAO;
import model.AriketaInfo;
import model.DAOException;
import model.HistorikoaItem;
import model.Workout;
import model.SerieInfo;
import utils.Timer;
import view.AriketaEginPanel;
import view.Lehioa;

public class AriketakEginController {

	private AriketaEginPanel panel;

	private List<AriketaInfo> ariketaLista = new ArrayList<>();

	private int unekoAriketaIndizea = 0;
	private int unekoSerieIndizea = 0;
	private Timer timer;
	private int workoutDenboraGuztira = 0;
	private int ariketaDenboraMugatua = 0;
	private boolean martxanDa = false;
	private boolean atsedenEstatuan = false;
	private boolean gordeDu = true;

	private JLabel lblWorkoutInfo;
	private JLabel lblWorkoutKrono;
	private JLabel lblAriketaKrono;

	private JLabel lblAriketaIzena;
	private JLabel lblAriketaDesc;

	private JPanel pnlSeriesContenedor;

	private JPanel pnlAbisuenEremua;
	private JLabel lblAbisuTestua;

	private JButton btnHasiPausatu;
	private JButton btnHurrengoa;

	private boolean preparacionEstatuan = false;
	private int cuentaAtrasPrepara = 5;

	private IWorkoutDAO workoutDAO = DAOGetter.getInstance().getWorkoutDAO();
	private IErabiltzaileDAO erabiltzaileDAO = DAOGetter.getInstance().getErabiltzaileaDAO();
	private IHistorikoaDAO historikoaDAO = DAOGetter.getInstance().getHistorikoaDAO();

	public AriketakEginController(AriketaEginPanel panel, Lehioa lehioa) {
		this.panel = panel;

		panel.getBtnItzuli().addActionListener(e -> {
			geldituTimer();
			gordeHistorikoa();
			lehioa.cambiarVista("ARIKETAK");
		});

		panel.getBtnHasiPausatu().addActionListener(e -> alternarHasiPausatu());

		panel.getBtnHurrengoa().addActionListener(e -> jarraituBotoiaPulsatuta());
	}

	private void mailaKonprobatuEguneratu() {
		int unekoMaila = Login.getErabil().getMaila();
		String email = Login.getErabil().getEmail();
		try {
			int workoutKopurua = workoutDAO.zenbatWorkoutMaila(unekoMaila);
			int egindakoWorkoutKopurua = historikoaDAO.zenbatWorkoutAmaituta(email, unekoMaila);

			if (workoutKopurua == egindakoWorkoutKopurua && unekoMaila < 3) {
				erabiltzaileDAO.mailaEguneratu(Login.getErabil(), unekoMaila + 1);
			}
		} catch (DAOException e1) {
			JOptionPane.showMessageDialog(panel, "Ezin izan da maila konprobatu eta eguneratu", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void ariketakKargatu() {
		unekoAriketaIndizea = 0;
		unekoSerieIndizea = 0;
		workoutDenboraGuztira = 0;
		martxanDa = false;
		atsedenEstatuan = false;
		preparacionEstatuan = true;
		gordeDu = false;
		cuentaAtrasPrepara = 5;

		ariketaLista = new ArrayList<>();
		panel.getBtnHasiPausatu().setText("Hasi");

		final String workoutID = WorkoutGorde.getWorkoutID();

		new SwingWorker<Workout, Void>() {
			@Override
			protected Workout doInBackground() throws Exception {
				try {
					return workoutDAO.lortuWorkoutOsoa(workoutID);
				} catch (Exception e) {
					System.out.println("[WARN] Firebase karga okerra. Erreskate modua aktibatzen (workoutak.dat)...");

					OfflineWorkoutDAO offlineDAO = DAOGetter.getInstance().getOfflineWorkoutDAO();
					List<Workout> saioakLokalak = offlineDAO.denakJaso();
					Workout hautatutakoWorkout = null;

					for (Workout s : saioakLokalak) {
						if (s.getIzena().equalsIgnoreCase(workoutID)
								|| (s.getDeskribapena() != null && s.getDeskribapena().equalsIgnoreCase(workoutID))) {
							hautatutakoWorkout = s;
							break;
						}
					}

					if (hautatutakoWorkout != null) {
						return hautatutakoWorkout;
					} else {
						throw new Exception("Ez da entrenamendurik aurkitu datu basean ezta lokalean ere.");
					}
				}
			}

			@Override
			protected void done() {
				try {
					Workout saioa = get();
					kargatuUIrekin(saioa);
				} catch (InterruptedException | ExecutionException e) {
					Throwable cause = e.getCause() != null ? e.getCause() : e;
					JOptionPane.showMessageDialog(panel, "Errorea datuak kargatzean: " + cause.getMessage(), "Errorea",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}.execute();
	}

	private void kargatuUIrekin(Workout saioa) {
		ariketaLista = saioa.getAriketak().stream().map(a -> AriketaInfo.fromAriketa(a)).toList();
		erakutsiWorkoutPuntua(saioa.getIzena());
		eguneratuPantailaEstatua();
		erakutsiPreparacionAvisoa(cuentaAtrasPrepara);
	}

	private void alternarHasiPausatu() {
		if (martxanDa) {
			timer.gelditu();
			martxanDa = false;
			panel.getBtnHasiPausatu().setText("Jarraitu");
		} else {
			if (timer.isHasita()) {
				timer.jarraitu();
			} else {
				timer.hasi();
			}

			martxanDa = true;
			panel.getBtnHasiPausatu().setText("Pausatu");
		}
	}

	private void segunduBatPasatu() {
		workoutDenboraGuztira++;
		eguneratuWorkoutKronometroa(formatDenbora(workoutDenboraGuztira));

		if (preparacionEstatuan) {
			cuentaAtrasPrepara--;
			eguneratuAriketaKronometroa("00:0" + cuentaAtrasPrepara);
			if (cuentaAtrasPrepara <= 0) {
				preparacionEstatuan = false;
				panel.getBtnHurrengoa().setEnabled(false);
				hurrengoaAriketaEgitera();
			}
		} else if (atsedenEstatuan) {
			if (ariketaDenboraMugatua > 0) {
				ariketaDenboraMugatua--;
				eguneratuAriketaKronometroa(formatDenbora(ariketaDenboraMugatua));
				panel.getBtnHurrengoa().setEnabled(false);
			} else {
				eguneratuAriketaKronometroa("00:00");
				panel.getLblAbisuTestua().setText("¡Atsedena osatua! Sakatu 'Siguiente' aurrera egiteko.");
				panel.getBtnHurrengoa().setEnabled(true);
			}
		} else {
			if (ariketaDenboraMugatua > 0) {
				ariketaDenboraMugatua--;
				eguneratuAriketaKronometroa(formatDenbora(ariketaDenboraMugatua));
			} else {
				atsedenEstatuan = true;
				panel.getBtnHurrengoa().setEnabled(false);

				int descansoDinamico = 20;
				if (ariketaLista != null && unekoAriketaIndizea < ariketaLista.size()) {
					descansoDinamico = ariketaLista.get(unekoAriketaIndizea).getAtsedena();
				}
				ariketaDenboraMugatua = descansoDinamico;
				erakutsiAtsedenaEremua(ariketaDenboraMugatua);
			}
		}
	}

	public void jarraituBotoiaPulsatuta() {
		if (atsedenEstatuan && ariketaDenboraMugatua <= 0) {
			atsedenEstatuan = false;
			preparacionEstatuan = true;
			cuentaAtrasPrepara = 5;

			panel.getBtnHurrengoa().setEnabled(false);
			erakutsiPreparacionAvisoa(cuentaAtrasPrepara);
		}
	}

	private void hurrengoaAriketaEgitera() {
		if (ariketaLista != null && unekoAriketaIndizea < ariketaLista.size()) {
			AriketaInfo unekoAriketa = ariketaLista.get(unekoAriketaIndizea);
			unekoSerieIndizea++;

			if (unekoAriketa.getSerieak() == null || unekoSerieIndizea >= unekoAriketa.getSerieak().size()) {
				unekoAriketaIndizea++;
				unekoSerieIndizea = 0;
			}
		}
		eguneratuPantailaEstatua();
	}

	private void eguneratuPantailaEstatua() {
		if (ariketaLista != null && unekoAriketaIndizea < ariketaLista.size()) {
			AriketaInfo unekoAriketa = ariketaLista.get(unekoAriketaIndizea);
			List<SerieInfo> series = unekoAriketa.getSerieak();

			if (series != null && !series.isEmpty() && unekoSerieIndizea < series.size()) {
				SerieInfo unekoSerie = series.get(unekoSerieIndizea);

				ariketaDenboraMugatua = unekoSerie.getIraupena();

				String tituloPantalla = unekoAriketa.getIzena() + " (" + unekoSerie.getIzena() + ")";

				erakutsiAriketa(unekoAriketa, formatDenbora(ariketaDenboraMugatua));
				panel.getLblAriketaIzena().setText(tituloPantalla);
				panel.getBtnHurrengoa().setEnabled(false);
			} else {
				unekoAriketaIndizea++;
				unekoSerieIndizea = 0;
				eguneratuPantailaEstatua();
				return;
			}

			boolean esUltimoEjercicio = (unekoAriketaIndizea + 1) == ariketaLista.size();
			boolean esUltimaSerie = (unekoSerieIndizea + 1) == series.size();
			panel.getBtnHurrengoa().setEnabled(esUltimoEjercicio && esUltimaSerie);

		} else {
			geldituTimer();
			erakutsiAmaiera();
		}
	}

	public void geldituTimer() {
		if (timer != null) {
			timer.gelditu();
		}
		martxanDa = false;
	}

	private String formatDenbora(int segundu) {
		int minutuak = segundu / 60;
		int erreserbaSegunduak = segundu % 60;
		return String.format("%02d:%02d", minutuak, erreserbaSegunduak);
	}

	public void erakutsiWorkoutPuntua(String workoutIzena) {
		lblWorkoutInfo = panel.getLblWorkoutInfo();
		lblWorkoutInfo.setText(workoutIzena);
	}

	public void eguneratuWorkoutKronometroa(String denbora) {
		lblWorkoutKrono = panel.getLblWorkoutKrono();
		lblWorkoutKrono.setText(denbora);
	}

	public void eguneratuAriketaKronometroa(String denbora) {
		lblAriketaKrono = panel.getLblAriketaKrono();
		lblAriketaKrono.setText(AriketaEginPanel.ATSEDEN_IKONOA + " " + denbora);
	}

	public void erakutsiAtsedenaEremua(int denboraAtsedena) {
		lblAriketaIzena = panel.getLblAriketaIzena();
		lblAriketaDesc = panel.getLblAriketaDesc();
		lblAriketaKrono = panel.getLblAriketaKrono();
		lblAbisuTestua = panel.getLblAbisuTestua();
		pnlAbisuenEremua = panel.getPnlAbisuenEremua();
		lblAriketaIzena.setText("¡Atsedena / Descanso!");
		lblAriketaDesc.setText("Recupera la respiración antes de continuar.");
		lblAriketaKrono.setText(String.format("%s 00:%02d", AriketaEginPanel.ATSEDEN_IKONOA, denboraAtsedena));
		lblAbisuTestua.setText(AriketaEginPanel.DENBORA_IKONOA + " " + "Abisua: Atsedena martxan");
		pnlAbisuenEremua.setBackground(new Color(255, 240, 240));
	}

	public void erakutsiAmaiera() {
		lblAriketaIzena = panel.getLblAriketaIzena();
		lblAriketaDesc = panel.getLblAriketaDesc();
		lblAriketaKrono = panel.getLblAriketaKrono();
		lblAbisuTestua = panel.getLblAbisuTestua();
		btnHurrengoa = panel.getBtnHurrengoa();
		btnHasiPausatu = panel.getBtnHasiPausatu();
		lblAriketaIzena.setText("Workout Amaitua!");
		lblAriketaDesc.setText("Has terminado todas las series de la sesión.");
		lblAriketaKrono.setText("00:00");
		lblAbisuTestua.setText("Workout osatuta.");
		btnHurrengoa.setEnabled(false);
		btnHasiPausatu.setEnabled(false);
		amaituta();
		this.mailaKonprobatuEguneratu();
	}

	public void amaituta() {
		gordeHistorikoa();
	}

	public void gordeHistorikoa() {
		if (gordeDu) {
			return;
		}

		gordeDu = true;
		if (this.workoutDenboraGuztira == 0) {
			System.out.println("Workout ez da hasi. Ez da historikorik gordeko.");
			return;
		}
		HistorikoaItem historikoa = new HistorikoaItem();
		historikoa.setEmailErabiltzaile(Login.getErabil().getEmail());
		historikoa.setData(new Date());
		historikoa.setWorkout(WorkoutGorde.getWorkoutID());
		historikoa.setDenboraGuztira(this.workoutDenboraGuztira);
		int tiempoPrevistoTotal = 0;
		if (ariketaLista != null) {
			for (AriketaInfo ariketa : ariketaLista) {
				tiempoPrevistoTotal += ariketa.getIraupena();
			}
		}
		historikoa.setAurreikusitakoDenbora(tiempoPrevistoTotal);
		int porcentajeCompletado = 0;
		if (ariketaLista != null && !ariketaLista.isEmpty()) {
			if (preparacionEstatuan || this.workoutDenboraGuztira <= 5) {
				porcentajeCompletado = 0;
			} else if (unekoAriketaIndizea >= ariketaLista.size()) {
				porcentajeCompletado = 100;
			} else {
				int tiempoTotalWorkout = 0;
				int tiempoDeSeriesCompletadas = 0;
				for (AriketaInfo apk : ariketaLista) {
					if (apk.getSerieak() != null) {
						for (SerieInfo serie : apk.getSerieak()) {
							tiempoTotalWorkout += serie.getIraupena();
						}
					}
				}
				for (int i = 0; i < unekoAriketaIndizea; i++) {
					AriketaInfo apk = ariketaLista.get(i);
					if (apk.getSerieak() != null) {
						for (SerieInfo serie : apk.getSerieak()) {
							tiempoDeSeriesCompletadas += serie.getIraupena();
						}
					}
				}
				AriketaInfo ariketaActual = ariketaLista.get(unekoAriketaIndizea);
				if (ariketaActual.getSerieak() != null) {
					for (int j = 0; j < unekoSerieIndizea; j++) {
						if (j < ariketaActual.getSerieak().size()) {
							tiempoDeSeriesCompletadas += ariketaActual.getSerieak().get(j).getIraupena();
						}
					}
				}
				if (tiempoTotalWorkout > 0) {
					porcentajeCompletado = (tiempoDeSeriesCompletadas * 100) / tiempoTotalWorkout;
				}
			}
		}
		if (porcentajeCompletado < 0)
			porcentajeCompletado = 0;
		if (porcentajeCompletado > 100)
			porcentajeCompletado = 100;
		historikoa.setOsatua(porcentajeCompletado);
		historikoa.setMaila(MailaGorde.getMaila());
		new Thread(() -> {
			try {
				IHistorikoaDAO historikoaDAO = DAOGetter.getInstance().getHistorikoaDAO();
				historikoaDAO.gordeHistorikoa(historikoa);
				System.out.println("[INFO] Entrenamendua arrakastaz erregistratu da.");
			} catch (DAOException e) {
				SwingUtilities.invokeLater(() -> {
					gordeDu = false;
					JOptionPane.showMessageDialog(panel, e.getMessage(), "Errorea Gordetzean",
							JOptionPane.ERROR_MESSAGE);
				});
			}
		}).start();
	}

	public void hasieratuEtaKargatu() {
		timer = new Timer(1000, () -> segunduBatPasatu());
		lblWorkoutKrono = panel.getLblWorkoutKrono();
		lblAriketaKrono = panel.getLblAriketaKrono();
		btnHurrengoa = panel.getBtnHurrengoa();
		btnHasiPausatu = panel.getBtnHasiPausatu();
		lblWorkoutKrono.setText("00:00");
		lblAriketaKrono.setText("00:05");
		btnHurrengoa.setEnabled(true);
		btnHasiPausatu.setEnabled(true);
		ariketakKargatu();
	}

	public void erakutsiPreparacionAvisoa(int segundosRestantes) {
		lblAriketaIzena = panel.getLblAriketaIzena();
		lblAriketaDesc = panel.getLblAriketaDesc();
		lblAriketaKrono = panel.getLblAriketaKrono();
		lblAbisuTestua = panel.getLblAbisuTestua();
		pnlAbisuenEremua = panel.getPnlAbisuenEremua();
		lblAriketaIzena.setText("¡Prest egon!");
		lblAriketaDesc.setText("Hurrengo saioa hastear dago. Egokitu zure posizioa.");
		lblAriketaKrono.setText("00:0" + segundosRestantes);
		lblAriketaKrono.setForeground(new Color(230, 120, 0));
		lblAbisuTestua.setText("\u26A0\uFE0F Adi: Ariketa berria 5 segundotan hasiko da...");
		pnlAbisuenEremua.setBackground(new Color(255, 248, 220));
	}

	public void erakutsiAriketa(AriketaInfo unekoAriketa, String iraupenaFormatua) {
		lblAriketaIzena = panel.getLblAriketaIzena();
		lblAriketaDesc = panel.getLblAriketaDesc();
		lblAriketaKrono = panel.getLblAriketaKrono();
		lblAbisuTestua = panel.getLblAbisuTestua();
		pnlAbisuenEremua = panel.getPnlAbisuenEremua();
		pnlSeriesContenedor = panel.getPnlSeriesContenedor();
		lblAriketaIzena.setText(unekoAriketa.getIzena() + " \u2465");
		lblAriketaDesc.setText(unekoAriketa.getDeskribapena());
		lblAriketaKrono.setText(AriketaEginPanel.ATSEDEN_IKONOA + " " + iraupenaFormatua);
		lblAriketaKrono.setForeground(new Color(205, 30, 30));
		lblAbisuTestua.setText("Ariketa martxan...");
		pnlAbisuenEremua.setBackground(new Color(235, 247, 235));
		pnlSeriesContenedor.removeAll();
		if (unekoAriketa.getSerieak() != null) {
			for (SerieInfo serie : unekoAriketa.getSerieak()) {
				String duracionFormato = String.format("%02d:%02d", serie.getIraupena() / 60, serie.getIraupena() % 60);
				pnlSeriesContenedor.add(crearTarjetaSerieReal(serie.getIzena(), duracionFormato, serie.getIrudiaUrl()));
			}
		}
		pnlSeriesContenedor.revalidate();
		pnlSeriesContenedor.repaint();
	}

	private JPanel crearTarjetaSerieReal(String titulo, String duracion, String urlImagen) {
		JPanel tarjeta = new JPanel(new GridBagLayout());
		tarjeta.setBackground(Color.WHITE);
		tarjeta.setBorder(new LineBorder(new Color(210, 210, 210), 1));
		tarjeta.setPreferredSize(new Dimension(180, 160));
		JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
		GridBagConstraints gbc_t = new GridBagConstraints();
		gbc_t.gridx = 0;
		gbc_t.gridy = 0;
		gbc_t.fill = GridBagConstraints.HORIZONTAL;
		gbc_t.insets = new Insets(8, 5, 4, 5);
		tarjeta.add(lblTitulo, gbc_t);
		JLabel lblImagenHolder = new JLabel(AriketaEginPanel.DENBORA_IKONOA, SwingConstants.CENTER);
		lblImagenHolder.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_i = new GridBagConstraints();
		gbc_i.gridx = 0;
		gbc_i.gridy = 1;
		gbc_i.fill = GridBagConstraints.BOTH;
		gbc_i.weightx = 1.0;
		gbc_i.weighty = 1.0;
		gbc_i.insets = new Insets(2, 8, 2, 8);
		tarjeta.add(lblImagenHolder, gbc_i);
		if (urlImagen != null && !urlImagen.isBlank()) {
			new Thread(() -> {
				try {
					URI uri = URI.create(urlImagen);
					URL url = uri.toURL();
					ImageIcon originalIcon = new ImageIcon(url);
					Image imgEscalada = originalIcon.getImage().getScaledInstance(160, 100, Image.SCALE_SMOOTH);
					SwingUtilities.invokeLater(() -> {
						lblImagenHolder.setText("");
						lblImagenHolder.setIcon(new ImageIcon(imgEscalada));
					});
				} catch (Exception e) {
					SwingUtilities.invokeLater(() -> {
						lblImagenHolder.setText(AriketaEginPanel.ATSEDEN_IKONOA);
					});
				}
			}).start();
		} else {
			lblImagenHolder.setText(AriketaEginPanel.ATSEDEN_IKONOA);
		}
		JLabel lblDuracion = new JLabel(duracion, SwingConstants.CENTER);
		lblDuracion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblDuracion.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_d = new GridBagConstraints();
		gbc_d.gridx = 0;
		gbc_d.gridy = 2;
		gbc_d.insets = new Insets(4, 5, 8, 5);
		tarjeta.add(lblDuracion, gbc_d);
		return tarjeta;
	}
}