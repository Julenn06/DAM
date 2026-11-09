package view;

import java.util.List;

import model.Persona;

public class PersonasView {
	public void mostrar(List<Persona> personas) {
		System.out.println("ID     | NOMBRE           | FECHA_NACIMIENTO | EDAD");
		System.out.println("-------+------------------+------------------+-----");
		personas.forEach(System.out::println);
	}

	public void mensaje(String texto) {
		System.out.println("[INFO] " + texto);
	}
}