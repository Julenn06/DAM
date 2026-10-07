package modelo;

import java.io.Serializable;

public class Muestra implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	public void setId(String id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setTemperatura(int temperatura) {
		this.temperatura = temperatura;
	}

	public void setPeligrosa(boolean peligrosa) {
		this.peligrosa = peligrosa;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	private String nombre;
	private String tipo;
	private int temperatura;
	private boolean peligrosa;
	private String nota;

	public Muestra(String id, String nombre, String tipo, int temperatura, boolean peligrosa, String nota) {
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.temperatura = temperatura;
		this.peligrosa = peligrosa;
		this.nota = nota;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public int getTemperatura() {
		return temperatura;
	}

	public boolean isPeligrosa() {
		return peligrosa;
	}

	public String getNota() {
		return nota;
	}

	@Override
	public String toString() {
		return id + " | " + nombre + " | " + tipo + " | " + temperatura + " C | peligrosa: "
				+ (peligrosa ? "SI" : "no");
	}
}
