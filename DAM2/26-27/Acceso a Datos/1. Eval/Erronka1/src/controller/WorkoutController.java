package controller;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import dao.DAOGetter;
import dao.interfazeak.IWorkoutDAO;
import model.Ariketa;
import model.DAOException;
import model.Erabiltzailea;
import model.Workout;
import view.Lehioa;
import view.TaulaWorkoutak;
import view.WorkoutPanel;

public class WorkoutController {

	private WorkoutPanel panel;
	private Lehioa lehioa;
	private IWorkoutDAO dao = DAOGetter.getInstance().getWorkoutDAO();
	private JComboBox<String> comboMaila;
	private List<Workout> unekoWorkoutak = new ArrayList<>();
	private boolean eguneratzen = false;
	private URL imgURL;
	private JLabel lblLoginLogo;
	private JButton btnHistorikoa;
	private DefaultTableModel modeloTaula;
	private TaulaWorkoutak taula;

	public WorkoutController(WorkoutPanel panel, Lehioa lehioa) {
		this.panel = panel;
		this.lehioa = lehioa;
	}

	public void listenersHasieratu() {
		taula = panel.getPanelTabla();
		JTable tablaVisual = panel.getPanelTabla().getTaula();
		comboMaila = panel.getComboMaila();
		imgURL = panel.getImgURL();
		lblLoginLogo = panel.getLblLoginLogo();
		btnHistorikoa = panel.getBtnHistorikoa();

		comboMaila.addActionListener(e -> {
			if (eguneratzen) {
				return;
			}
			kargatuWorkoutak(comboMaila.getSelectedIndex());
		});

		tablaVisual.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					int filaSeleccionada = tablaVisual.getSelectedRow();

					if (filaSeleccionada != -1) {
						Workout hautatutakoWorkout = getWorkoutAt(filaSeleccionada);

						if (hautatutakoWorkout != null) {
							String workoutID = "";
							try {
								workoutID = dao.jasoWorkoutID(hautatutakoWorkout);
							} catch (DAOException ex) {
								System.out.println(
										"[WARN] Ezin izan da Workout ID-a hodeitik jaso (Modu Offlinea). Izena erabiltzen ID gisa.");
								workoutID = hautatutakoWorkout.getIzena();
							}
							WorkoutGorde.setWorkoutID(workoutID);
							MailaGorde.setMaila(hautatutakoWorkout.getMaila());

							lehioa.erakutsiXehetasunakPantaila(hautatutakoWorkout);
						}
					}
				}
			}
		});

		if (imgURL != null) {
			ImageIcon originalIcon = new ImageIcon(imgURL);
			Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			lblLoginLogo.setIcon(new ImageIcon(scaledImage));
		} else {
			System.err.println("Error: No se pudo encontrar el archivo /img/user.png");
			lblLoginLogo.setText("[User Img]");
		}

		lblLoginLogo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lehioa.cambiarVista("PROFILA");
			}
		});

		btnHistorikoa.addActionListener(e -> {
			lehioa.cambiarVista("HISTORIKOA");
		});

		JButton btnWorkoutItxi = panel.getBtnWorkoutItxi();
		btnWorkoutItxi.addActionListener(e -> {
			Login.setErabil(null);
			lehioa.cambiarVista("LOGIN");
		});
	}

	private void kargatuWorkoutak(int maila) {
		new SwingWorker<List<Workout>, Void>() {
			@Override
			protected List<Workout> doInBackground() throws Exception {
				try {
					return dao.irakurriWorkoutMailarekin(maila);
				} catch (Exception e) {
					System.out.println("[WARN] Ezin izan dira saioak hodeitik jaso. Workoutak.dat kargatzen...");
					dao = DAOGetter.getInstance().getOfflineWorkoutDAO();
					return dao.irakurriWorkoutMailarekin(maila);
				}
			}

			@Override
			protected void done() {
				try {
					eguneratuTaula(get());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} catch (ExecutionException e) {
					JOptionPane.showMessageDialog(panel, "Ezin izan dira saioak kargatu ezta modu lokalean ere.",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}.execute();
	}

	public void datuakKargatuMailarekin(int maila) {
		kargatuWorkoutak(maila);
	}

	public void datuakKargatu() {
		int level = erabiltzaileMaila();

		eguneratzen = true;
		try {
			comboMaila.setModel(new DefaultComboBoxModel<>(comboBoxMailakSortu()));
			if (level >= 0 && level < comboMaila.getItemCount()) {
				comboMaila.setSelectedIndex(level);
			}
		} finally {
			eguneratzen = false;
		}

		kargatuWorkoutak(level);
	}

	private int erabiltzaileMaila() {
		Erabiltzailea erabiltzailea = Login.getErabil();
		return erabiltzailea != null ? erabiltzailea.getMaila() : Ariketa.MAILA_MAX;
	}

	public String[] comboBoxMailakSortu() {
		int level = erabiltzaileMaila();

		String[] levelsArray = new String[level + 1];
		for (int i = 0; i <= level; i++) {
			levelsArray[i] = (i) + ". Maila";
		}

		return levelsArray;
	}

	public void eguneratuTaula(List<Workout> saioak) {
		taulaEguneratu(saioak);
	}

	public Workout getWorkoutAt(int index) {
		unekoWorkoutak = taula.getUnekoWorkoutak();
		if (index >= 0 && index < unekoWorkoutak.size()) {
			return unekoWorkoutak.get(index);
		}
		return null;
	}

	public void taulaEguneratu(List<Workout> saioak) {
		modeloTaula = taula.getModeloTaula();

		taula.getUnekoWorkoutak().clear();
		taula.getUnekoWorkoutak().addAll(saioak);

		modeloTaula.setRowCount(0);
		for (Workout saioa : saioak) {
			modeloTaula.addRow(new Object[] { saioa.getMaila(), saioa.getIzena(), saioa.getDeskribapena(),
					saioa.getGidaBideoa() });
		}
	}
}
