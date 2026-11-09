package model;

import java.io.Serializable;

import com.google.cloud.firestore.QueryDocumentSnapshot;

public class Seriea implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String izena;
	private String argazkia;
	private int errepikapenak;
	private int iraupena;

	public Seriea() {
		super();
	}

	public Seriea(String izena, String argazkia, int errepikapenak, int iraupena) {
		super();
		this.izena = izena;
		this.errepikapenak = errepikapenak;
		this.argazkia = argazkia;
		this.iraupena = iraupena;
	}

	public static Seriea fromDocument(QueryDocumentSnapshot s) {
		String izena = s.getString("izena");
		String argazkia = s.getString("argazkia");
		int errepikapenak = (int) (long) s.getLong("errepikapenak");
		int iraupena = (int) (long) s.getLong("iraupena");
		return new Seriea(izena, argazkia, errepikapenak, iraupena);
	}

	public String getIzena() {
		return izena;
	}

	public String getArgazkia() {
		return argazkia;
	}

	public int getErrepikapenak() {
		return errepikapenak;
	}

	public int getIraupena() {
		return iraupena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public void setArgazkia(String argazkiEsteka) {
		this.argazkia = argazkiEsteka;
	}

	public void setErrepikapenak(int errepikapenak) {
		this.errepikapenak = errepikapenak;
	}

	public void setIraupena(int iraupena) {
		this.iraupena = iraupena;
	}

}
