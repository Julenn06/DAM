package modelo;

public class Cerradura {
	private String codigo;
	private String mensaje;

	// Constructor vacío requerido por Gson
	public Cerradura() {
	}

	public Cerradura(String codigo, String mensaje) {
		this.codigo = codigo;
		this.mensaje = mensaje;
	}

	// Getters y Setters
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
		return "Cerradura [codigo=" + codigo + ", mensaje=" + mensaje + "]";
	}
}
