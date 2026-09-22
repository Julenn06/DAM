package ariketa06;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ProcesoA {
	public static void main(String[] args) {
		try {
			ProcessBuilder pb = new ProcessBuilder("java", "-cp", System.getProperty("java.class.path"),
					"ariketa06.ProcesoB");
			Process p = pb.start();

			try (PrintWriter out = new PrintWriter(p.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {

				out.println("Nola deitzen zara?");
				String response1 = in.readLine();
				System.out.println(response1);

				out.println("Zenbat urte dauzkazu?");
				String response2 = in.readLine();
				System.out.println(response2);

				String errorLine;
				while (err.ready() && (errorLine = err.readLine()) != null) {
					System.err.println(errorLine);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
