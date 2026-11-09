package model;

import java.io.Serializable;
import java.util.List;

import com.google.cloud.firestore.DocumentSnapshot;

public class Workout implements Serializable {
	private static final long serialVersionUID = 1L;
	private int maila;
	private String izena;
	private String deskribapena;
	private String gidaBideoa;
	private List<Ariketa> ariketak;

	public Workout() {
		super();
	}

	public Workout(int maila, String izena, String deskribapena, String gidaBideoa, List<Ariketa> ariketak) {
		super();
		this.maila = maila;
		this.izena = izena;
		this.deskribapena = deskribapena;
		this.gidaBideoa = gidaBideoa;
		this.ariketak = ariketak;
	}

	public static Workout fromDocument(DocumentSnapshot doc) {
		long maila = doc.getLong("maila");
		String izena = doc.getString("izena");
		String deskribapena = doc.getString("deskribapena");
		String bideoa = doc.getString("bideoa");
		return new Workout((int) maila, izena, deskribapena, bideoa, null);
	}

	public int getMaila() {
		return maila;
	}

	public String getIzena() {
		return izena;
	}

	public String getDeskribapena() {
		return deskribapena;
	}

	public String getGidaBideoa() {
		return gidaBideoa;
	}

	public List<Ariketa> getAriketak() {
		return ariketak;
	}

	public void setMaila(int maila) {
		this.maila = maila;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public void setDeskribapena(String deskribapena) {
		this.deskribapena = deskribapena;
	}

	public void setGidaBideoa(String gidaBideoa) {
		this.gidaBideoa = gidaBideoa;
	}

	public void setAriketak(List<Ariketa> ariketak) {
		this.ariketak = ariketak;
	}

	public int getIraupena() {
		int iraupena = 0;
		if (this.getAriketak() != null) {
			for (Ariketa ariketa : this.getAriketak()) {
				iraupena += ariketa.getIraupena();
			}
		}
		return iraupena;
	}

	@Override
	public String toString() {
		return "Workout [maila=" + maila + ", izena=" + izena + ", deskribapena=" + deskribapena + ", gidaBideoa="
				+ gidaBideoa + ", ariketak=" + ariketak + "]";
	}

}
