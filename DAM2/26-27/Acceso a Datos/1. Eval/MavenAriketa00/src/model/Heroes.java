package model;

public class Heroes {
	private String titulo;
	private int numero;
	private String fecha;
	private double precio;
	private String papel;

	public Heroes(String titulo, int numero, String fecha, double precio, String papel) {
		this.titulo = titulo;
		this.numero = numero;
		this.fecha = fecha;
		this.precio = precio;
		this.papel = papel;
	}

	public String getTitulo() {
		return titulo;
	}

	public int getNumero() {
		return numero;
	}

	public String getFecha() {
		return fecha;
	}

	public double getPrecio() {
		return precio;
	}

	public String getPapel() {
		return papel;
	}
}
