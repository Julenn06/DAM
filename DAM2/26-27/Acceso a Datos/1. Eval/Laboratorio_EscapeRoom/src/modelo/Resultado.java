package modelo;

public class Resultado {
	private String equipo;
	private String codigo;
	private String mensaje;

	public Resultado() {
	}

	public Resultado(String equipo, String codigo, String mensaje) {
		this.equipo = equipo;
		this.codigo = codigo;
		this.mensaje = mensaje;
	}

	public String getEquipo() {
		return equipo;
	}

	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	@Override
	public String toString() {
		return "Resultado [equipo=" + equipo + ", codigo=" + codigo + ", mensaje=" + mensaje + "]";
	}
}
