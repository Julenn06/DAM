package modelo;

public class Mensajes {

	private String userEnviar = "";
	private String userRecibir = "";
	private String fecha;
	private String hora;
	private String asunto = "";
	private String contenido = "";

	public String getUserEnviar() {
		return userEnviar;
	}

	public void setUserEnviar(String userEnviar) {
		this.userEnviar = userEnviar;
	}

	public String getUserRecibir() {
		return userRecibir;
	}

	public void setUserRecibir(String userRecibir) {
		this.userRecibir = userRecibir;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fechaFormateada) {
		this.fecha = fechaFormateada;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String horaFormateada) {
		this.hora = horaFormateada;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	@Override
	public String toString() {
		return "Users [userEnviar=" + userEnviar + ", userRecibir=" + userRecibir + ", fecha=" + fecha + ", hora="
				+ hora + ", asunto=" + asunto + ", contenido=" + contenido + "]";
	}
}
