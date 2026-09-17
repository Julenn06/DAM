package modelo;

public class Estadisticas {
	private int numeroPartidos;
	private int totalGolesLocal;
	private int totalGolesVisitante;

	public Estadisticas(int numeroPartidos, int totalGolesLocal, int totalGolesVisitante) {
		this.numeroPartidos = numeroPartidos;
		this.totalGolesLocal = totalGolesLocal;
		this.totalGolesVisitante = totalGolesVisitante;
	}

	public int getNumeroPartidos() {
		return numeroPartidos;
	}

	public int getTotalGolesLocal() {
		return totalGolesLocal;
	}

	public int getTotalGolesVisitante() {
		return totalGolesVisitante;
	}
}
