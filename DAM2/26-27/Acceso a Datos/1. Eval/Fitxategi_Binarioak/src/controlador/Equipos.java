package controlador;

import modelo.Equipo;

public class Equipos {

	public boolean validarDatos(Equipo equipo) {
		if (equipo == null) {
			return false;
		}
		if (equipo.getEquipoLocal() == null || equipo.getEquipoLocal().trim().isEmpty()) {
			return false;
		}
		if (equipo.getEquipoVisitante() == null || equipo.getEquipoVisitante().trim().isEmpty()) {
			return false;
		}
		if (equipo.getLugar() == null || equipo.getLugar().trim().isEmpty()) {
			return false;
		}
		if (equipo.getGolesLocal() < 0 || equipo.getGolesVisitante() < 0) {
			return false;
		}
		if (equipo.getFecha() == null || equipo.getFecha().trim().isEmpty()) {
			return false;
		}
		String regexFecha = "^\\d{2}[/-]\\d{2}[/-]\\d{4}$";
		if (!equipo.getFecha().matches(regexFecha)) {
			return false;
		}
		return true;
	}
}
