package kronometroa;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private HariakKronometroa hariak = new HariakKronometroa(null, null, null, null, null, null);

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnHasi = new JButton("Hasi");
		btnHasi.setBounds(10, 201, 89, 23);
		contentPane.add(btnHasi);

		JButton btnPausatu = new JButton("Pausatu");
		btnPausatu.setBounds(109, 201, 89, 23);
		btnPausatu.setEnabled(false);
		contentPane.add(btnPausatu);

		JButton btnJarraitu = new JButton("Jarraitu");
		btnJarraitu.setBounds(208, 201, 89, 23);
		btnJarraitu.setEnabled(false);
		contentPane.add(btnJarraitu);

		JButton btnIrten = new JButton("Irten");
		btnIrten.setBounds(322, 201, 89, 23);
		btnIrten.setEnabled(false);
		contentPane.add(btnIrten);

		JLabel lblKronometroa = new JLabel("00:00:00");
		lblKronometroa.setBounds(171, 75, 110, 23);
		lblKronometroa.setFont(new Font("Tahoma", Font.BOLD, 18));
		contentPane.add(lblKronometroa);

		JLabel labelInfo = new JLabel();
		labelInfo.setBounds(109, 144, 267, 14);
		contentPane.add(labelInfo);

		JLabel lblOrduaOrain = new JLabel("");
		lblOrduaOrain.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOrduaOrain.setHorizontalAlignment(SwingConstants.CENTER);
		lblOrduaOrain.setBounds(337, 144, 64, 27);
		contentPane.add(lblOrduaOrain);
		OrduaOrain ordua = new OrduaOrain("OrduaOrain", lblOrduaOrain);

		JLabel labelInfoOrduaOrain = new JLabel();
		labelInfoOrduaOrain.setBounds(109, 169, 267, 14);
		contentPane.add(labelInfoOrduaOrain);

		btnHasi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnHasi.setEnabled(false);
				btnPausatu.setEnabled(true);
				btnJarraitu.setEnabled(false);
				btnIrten.setEnabled(true);
				hariak = new HariakKronometroa("HARIAKORDUA", lblKronometroa, labelInfo, btnHasi, btnPausatu,
						btnJarraitu);
				Thread kronometroa = new Thread(hariak);
				kronometroa.start();

			}
		});

		btnPausatu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPausatu.setEnabled(false);
				btnJarraitu.setEnabled(true);

				hariak.pausa();
			}
		});

		btnJarraitu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnJarraitu.setEnabled(false);
				btnPausatu.setEnabled(true);
				hariak.jarraitu();
			}
		});

		btnIrten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnHasi.setEnabled(false);
				btnPausatu.setEnabled(false);
				btnJarraitu.setEnabled(false);
				btnIrten.setEnabled(false);
				hariak.amaitu();
				ordua.amaitu(labelInfoOrduaOrain);
			}
		});

		Thread orduaOrain = new Thread(ordua);
		orduaOrain.start();
	}
}
