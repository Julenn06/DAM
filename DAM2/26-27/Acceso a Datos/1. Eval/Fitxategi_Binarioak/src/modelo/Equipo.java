package modelo;

import java.io.Serializable;

public class Equipo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String equipoLocal;
	private String equipoVisitante;
	private int golesLocal;
	private int golesVisitante;
	private String lugar;
	private String fecha;

	public String getEquipoLocal() {
		return equipoLocal;
	}

	public void setEquipoLocal(String equipoLocal) {
		this.equipoLocal = equipoLocal;
	}

	public String getEquipoVisitante() {
		return equipoVisitante;
	}

	public void setEquipoVisitante(String equipoVisitante) {
		this.equipoVisitante = equipoVisitante;
	}

	public int getGolesLocal() {
		return golesLocal;
	}

	public void setGolesLocal(int golesLocal) {
		this.golesLocal = golesLocal;
	}

	public int getGolesVisitante() {
		return golesVisitante;
	}

	public void setGolesVisitante(int golesVisitante) {
		this.golesVisitante = golesVisitante;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return "Equipo [equipoLocal=" + equipoLocal + ", equipoVisitante=" + equipoVisitante + ", golesLocal="
				+ golesLocal + ", golesVisitante=" + golesVisitante + ", lugar=" + lugar + ", fecha=" + fecha + "]";
	}

}
