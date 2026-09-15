package kronometroa;

import javax.swing.JButton;
import javax.swing.JLabel;

public class HariakKronometroa implements Runnable {

	JLabel lblKronometroa;
	JLabel lblInfo;
	String hariaIzena;
	boolean geldituta = false;
	boolean amaituta = false;
	private JButton btnHasi, btnPausatu, btnJarraitu;

	public HariakKronometroa(String hariaIzena, JLabel lblKronometroa, JLabel lblInfo, JButton btnHasi,
			JButton btnPausatu, JButton btnJarraitu) {
		this.hariaIzena = hariaIzena;
		this.lblKronometroa = lblKronometroa;
		this.lblInfo = lblInfo;
		this.btnHasi = btnHasi;
		this.btnPausatu = btnPausatu;
		this.btnJarraitu = btnJarraitu;
	}

	@Override
	public void run() {
		lblInfo.setText(hariaIzena + " haria martxan dago");
		int totala = 5400;
		long hasiera = System.currentTimeMillis();
		long pausak = 0L;
		long hasieraPausa = 0L;

		while (!amaituta) {
			if (geldituta) {
				if (hasieraPausa == 0L) {
					hasieraPausa = System.currentTimeMillis();
				}

			} else {
				if (hasieraPausa != 0L) {
					pausak += System.currentTimeMillis() - hasieraPausa;
					hasieraPausa = 0L;
				}

				long orain = System.currentTimeMillis();
				long trans = (orain - hasiera - pausak);
				int geltizenDa = (int) Math.max(0, totala - (trans / 1000));
				lblKronometroa.setText(formateatu(geltizenDa));

				if (geltizenDa <= 0) {
					lblInfo.setText(hariaIzena + " haria bukatu da");
					btnHasi.setEnabled(false);
					btnPausatu.setEnabled(false);
					btnJarraitu.setEnabled(false);
					amaituta = true;
				}
			}

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	private String formateatu(int segundoak) {
		int hh = (segundoak / 3600);
		int mm = ((segundoak - hh * 3600) / 60);
		int ss = segundoak - (hh * 3600 + mm * 60);
		return String.format("%02d:%02d:%02d", hh, mm, ss);
	}

	public void pausa() {
		lblInfo.setText(hariaIzena + " haria pausatu da");
		geldituta = true;

	}

	public void jarraitu() {
		lblInfo.setText(hariaIzena + " haria berrabiarazi da");
		geldituta = false;

	}

	public void amaitu() {
		lblInfo.setText(hariaIzena + " haria bukatu da");
		amaituta = true;
	}

}
