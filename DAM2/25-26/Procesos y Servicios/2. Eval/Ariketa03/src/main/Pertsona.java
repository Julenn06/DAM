package main;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Pertsona klasea - Objektuak sarean bidali ahal izateko Serializable
 * interfazea inplementatzen du
 */
public class Pertsona implements Serializable {
	private static final long serialVersionUID = 1L;

	private String izena;
	private int adina;
	private String herria;
	private Date jaiotzeData;
	private String generoa;

	// Eraikitzailea
	public Pertsona(String izena, int adina, String herria, Date jaiotzeData, String generoa) {
		this.izena = izena;
		this.adina = adina;
		this.herria = herria;
		this.jaiotzeData = jaiotzeData;
		this.generoa = generoa;
	}

	// Getters eta Setters
	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public int getAdina() {
		return adina;
	}

	public void setAdina(int adina) {
		this.adina = adina;
	}

	public String getHerria() {
		return herria;
	}

	public void setHerria(String herria) {
		this.herria = herria;
	}

	public Date getJaiotzeData() {
		return jaiotzeData;
	}

	public void setJaiotzeData(Date jaiotzeData) {
		this.jaiotzeData = jaiotzeData;
	}

	public String getGeneroa() {
		return generoa;
	}

	public void setGeneroa(String generoa) {
		this.generoa = generoa;
	}

	@Override
	public String toString() {
		SimpleDateFormat formatua = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormateatua = (jaiotzeData != null) ? formatua.format(jaiotzeData) : "Ez dago";
		return "Pertsona [Izena=" + izena + ", Adina=" + adina + ", Herria=" + herria + ", Jaiotze-data="
				+ dataFormateatua + ", Generoa=" + generoa + "]";
	}
}
