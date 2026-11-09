package model;

import java.time.LocalDate;
import java.time.Period;

public class Persona {
	private String id;
	private String nombre;
	private LocalDate fechaNacimiento;

	public Persona() {
	}

	public Persona(String id, String nombre, LocalDate fechaNacimiento) {
		this.id = id;
		this.nombre = nombre;
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public int getEdad() {
		return fechaNacimiento == null ? 0 : Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

	@Override
	public String toString() {
		return String.format("%-6s | %-16s | %s | %2d años", id, nombre, fechaNacimiento, getEdad());
	}
}