package model;

import java.util.ArrayList;
import java.util.List;

public class AriketaInfo {
	private String izena;
	private String deskribapena;
	private int iraupena;
	private int atsedena;
	private List<SerieInfo> serieak;

	public AriketaInfo(String izena, String deskribapena, int iraupena) {
		this.izena = izena;
		this.deskribapena = deskribapena;
		this.iraupena = iraupena;
		this.atsedena = 20;
		this.serieak = new ArrayList<>();
	}

	public String getIzena() {
		return izena;
	}

	public String getDeskribapena() {
		return deskribapena;
	}

	public int getIraupena() {
		return iraupena;
	}

	public int getAtsedena() {
		return atsedena;
	}

	public void setAtsedena(int atsedena) {
		this.atsedena = atsedena;
	}

	public List<SerieInfo> getSerieak() {
		return serieak;
	}

	public void setSerieak(List<SerieInfo> serieak) {
		this.serieak = serieak;
	}

	public static AriketaInfo fromAriketa(Ariketa a) {
		if (a == null) {
			return null;
		}

		List<SerieInfo> serieakLista = new ArrayList<>();
		if (a.getSerieak() != null) {
			for (Seriea seriea : a.getSerieak()) {
				String sIzena = seriea.getIzena();
				String sArgazkiaUrl = seriea.getArgazkia();
				int sIraupenaInt = seriea.getIraupena();

				if (sIzena != null) {
					serieakLista.add(new SerieInfo(sIzena, sArgazkiaUrl, sIraupenaInt));
				}
			}
			serieakLista.sort((s1, s2) -> s1.getIzena().compareToIgnoreCase(s2.getIzena()));
		}

		AriketaInfo info = new AriketaInfo(a.getIzena(), a.getDeskribapena(), a.getIraupena());
		info.setAtsedena(a.getAtsedena());
		info.setSerieak(serieakLista);

		return info;
	}
}
