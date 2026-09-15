package controlador;

import java.io.IOException;

public class ReadFile {

	public void MostrarFichero() {
		String rutaArchivo = "users.txt";

		try {
			ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start", "notepad", rutaArchivo);
			pb.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
	