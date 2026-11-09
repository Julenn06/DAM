package model;

public class SerieInfo {
	private String izena;
	private String irudiaUrl;
	private int iraupena;

	public SerieInfo(String izena, String irudiaUrl, int iraupena) {
		this.izena = izena;
		this.irudiaUrl = irudiaUrl;
		this.iraupena = iraupena;
	}

	public String getIzena() {
		return izena;
	}

	public String getIrudiaUrl() {
		return irudiaUrl;
	}

	public int getIraupena() {
		return iraupena;
	}

	@Override
	public String toString() {
		return izena + " (" + iraupena + " s)";
	}
}
