package format;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Persona;

public final class DatPersonas {
	private DatPersonas() {
	}

	// .dat estructurado: cantidad, id, nombre y fecha de nacimiento.
	public static void escribirDat(List<Persona> personas, File ruta) throws IOException {
		try (DataOutputStream out = new DataOutputStream(new FileOutputStream(ruta))) {
			out.writeInt(personas.size());
			for (Persona p : personas) {
				out.writeUTF(p.getId());
				out.writeUTF(p.getNombre());
				out.writeUTF(p.getFechaNacimiento().toString());
			}
		}
	}

	public static List<Persona> leerDat(File ruta) throws IOException {
		List<Persona> resultado = new ArrayList<>();
		try (DataInputStream in = new DataInputStream(new FileInputStream(ruta))) {
			int cantidad = in.readInt();
			for (int i = 0; i < cantidad; i++) {
				resultado.add(new Persona(in.readUTF(), in.readUTF(), LocalDate.parse(in.readUTF())));
			}
		}
		return resultado;
	}
}