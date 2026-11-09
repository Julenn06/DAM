package model;

import java.io.Serializable;
import java.util.List;

import com.google.cloud.firestore.QueryDocumentSnapshot;

public class Ariketa implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MAILA_MAX = 4;

	private String izena;
	private String deskribapena;
	private int atsedena;
	private List<Seriea> serieak;

	public Ariketa() {
		super();
	}

	public Ariketa(String izena, String deskribapena, int atsedena, List<Seriea> serieak) {
		super();
		this.izena = izena;
		this.deskribapena = deskribapena;
		this.serieak = serieak;
		this.atsedena = atsedena;
	}

	public static Ariketa fromDocument(QueryDocumentSnapshot doc) {
		String izena = doc.getString("izena");
		String deskribapena = doc.getString("deskribapena");
		long atsedena = doc.getLong("atsedena");
		return new Ariketa(izena, deskribapena, (int) atsedena, null);
	}

	public String getIzena() {
		return izena;
	}

	public String getDeskribapena() {
		return deskribapena;
	}

	public int getAtsedena() {
		return atsedena;
	}

	public List<Seriea> getSerieak() {
		return serieak;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public void setDeskribapena(String deskribapena) {
		this.deskribapena = deskribapena;
	}

	public void setAtsedena(int atsedena) {
		this.atsedena = atsedena;
	}

	public void setSerieak(List<Seriea> serieak) {
		this.serieak = serieak;
	}

	public int getIraupena() {
		int iraupenaGuztira = 0;
		if (this.getSerieak() != null) {
			for (Seriea seriea : this.getSerieak()) {
				iraupenaGuztira += seriea.getIraupena() + this.getAtsedena();
			}
		}
		return iraupenaGuztira;
	}

	@Override
	public String toString() {
		return "Ariketa [izena=" + izena + ", deskribapena=" + deskribapena + ", atsedena=" + atsedena + ", serieak="
				+ serieak + "]";
	}

}
