package view;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.AriketaController;
import controller.AriketakEginController;
import controller.DBConnection;
import model.Workout;

public class Lehioa extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private CardLayout cardLayout;
	private AriketakPanel panelAriketa;
	private LoginPanel panelLogin;
	private RegisterPanel panelRegistro;
	private WorkoutPanel panelSecond;
	private ProfilaPanel profilaPanel;
	private AriketaEginPanel panelAriketaEgin;
	private HistorikoaPanel panelHistorikoa;

	public Lehioa() {
		DBConnection.warmUpAsync();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setTitle("Elorgym");

		cardLayout = new CardLayout();
		contentPane = new JPanel(cardLayout);
		setContentPane(contentPane);

		panelLogin = new LoginPanel(this);
		panelRegistro = new RegisterPanel(this);
		panelSecond = new WorkoutPanel(this);
		panelAriketa = new AriketakPanel(this);
		profilaPanel = new ProfilaPanel(this);
		panelAriketaEgin = new AriketaEginPanel(this);
		panelHistorikoa = new HistorikoaPanel(this);

		contentPane.add(panelLogin, "LOGIN");
		contentPane.add(panelRegistro, "REGISTRO");
		contentPane.add(panelSecond, "SECOND");
		contentPane.add(panelAriketa, "ARIKETAK");
		contentPane.add(profilaPanel, "PROFILA");
		contentPane.add(panelAriketaEgin, "ARIKETAKEGIN");
		contentPane.add(panelHistorikoa, "HISTORIKOA");

		cardLayout.show(contentPane, "LOGIN");
	}

	public void cambiarVista(String nombreVista) {
		if ("PROFILA".equals(nombreVista) && profilaPanel != null) {
			profilaPanel.getController().kargatuDatuakBistan();
		}

		if ("ARIKETAKEGIN".equals(nombreVista) && panelAriketaEgin != null) {
			hasieratuEtaKargatu();
		}

		if ("HISTORIKOA".equals(nombreVista) && panelHistorikoa != null) {
			panelHistorikoa.getController().listenersHasieratu();
		}

		cardLayout.show(contentPane, nombreVista);
	}

	public void erakutsiXehetasunakPantaila(Workout saioa) {
		AriketaController cont = new AriketaController(panelAriketa, this);
		if (panelAriketa != null) {

			cont.kargatuAriketak(saioa);
		}
		cambiarVista("ARIKETAK");
	}

	public AriketakPanel getPanelAriketa() {
		return panelAriketa;
	}

	public LoginPanel getPanelLogin() {
		return panelLogin;
	}

	public RegisterPanel getPanelRegistro() {
		return panelRegistro;
	}

	public WorkoutPanel getPanelSecond() {
		return panelSecond;
	}

	public ProfilaPanel getProfilaPanel() {
		return profilaPanel;
	}

	public AriketaEginPanel getAriketaEginPanel() {
		return panelAriketaEgin;
	}

	public HistorikoaPanel getPanelHistorikoa() {
		return panelHistorikoa;
	}

	public void hasieratuEtaKargatu() {
		if (panelAriketaEgin != null) {
			AriketakEginController cont = panelAriketaEgin.getController();

			if (cont != null) {
				cont.hasieratuEtaKargatu();
			}
		}
	}

}
