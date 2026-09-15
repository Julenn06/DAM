package controlador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {

	public void EscribirFichero(String user, int age, double salary) {
		try {
			BufferedWriter fichero = new BufferedWriter(new FileWriter("Users.txt"));

			for (int i = 0; i < 11; i++) {
				fichero.write("File Numero " + i);
				fichero.newLine();
			}
			fichero.close();
		} catch (IOException e) {
			System.out.println("Error al escribir el fichero: " + e.getMessage());
		}
	}
}