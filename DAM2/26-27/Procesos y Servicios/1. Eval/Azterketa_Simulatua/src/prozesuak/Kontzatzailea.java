package prozesuak;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Kontzatzailea {

	public static void main(String[] args) throws IOException {

		ProcessBuilder pb = new ProcessBuilder("cmd", "-cp", "bin", "Oihartzuna");
		pb.directory(new File("bin"));

		Process process = pb.start();

		if (!process.isAlive()) {
			System.out.println("Oihartzuna prozesua ez dago martxan.");
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		while (reader.ready()) {
			String line = reader.readLine();
			System.out.println(line);
		}
		reader.close();

	}
}
