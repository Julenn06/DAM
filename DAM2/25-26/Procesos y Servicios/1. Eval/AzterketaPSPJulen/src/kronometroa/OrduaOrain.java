package kronometroa;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

public class OrduaOrain implements Runnable {

	private JLabel lblOrduaOrain;
	private boolean hasita = true;
	private String izena;

	public OrduaOrain(String izena, JLabel lblOrduaOrain) {
		this.lblOrduaOrain = lblOrduaOrain;
		this.izena = izena;
	}

	@Override
	public void run() {
		do {
			Date data = new java.util.Date();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String unekoOrdua = sdf.format(data);
			lblOrduaOrain.setText(unekoOrdua);
		} while (hasita == true);
	}

	public void amaitu(JLabel labelInfoOrduaOrain) {
		labelInfoOrduaOrain.setText(izena + " haria gelditu da");
		this.hasita = false;
	}
}