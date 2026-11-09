package format;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Persona;

public final class TxtPersonas {
	private TxtPersonas() {
	}

	public static void escribirTabla(List<Persona> personas, File ruta) throws IOException {
		try (BufferedWriter out = new BufferedWriter(new FileWriter(ruta))) {
			out.write("ID | NOMBRE | FECHA_NACIMIENTO | EDAD");
			out.newLine();
			for (Persona p : personas) {
				out.write(p.getId() + " | " + p.getNombre() + " | " + p.getFechaNacimiento() + " | " + p.getEdad());
				out.newLine();
			}
		}
	}

	public static List<Persona> leerTabla(File ruta) throws IOException {
		List<Persona> resultado = new ArrayList<>();
		try (BufferedReader in = new BufferedReader(new FileReader(ruta))) {
			String linea;
			in.readLine();
			while ((linea = in.readLine()) != null) {
				String[] campos = linea.split("\\s*\\|\\s*");
				if (campos.length >= 3) {
					resultado.add(new Persona(campos[0], campos[1], LocalDate.parse(campos[2])));
				}
			}
		}
		return resultado;
	}

}