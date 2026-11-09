package ariketa06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ProcesoA {
	public static void main(String[] args) {
		try {
			ProcessBuilder pb = new ProcessBuilder("java", "-cp", System.getProperty("java.class.path"),
					"ariketa06.ProcesoB");
			Process p = pb.start();

			try (PrintWriter out = new PrintWriter(p.getOutputStream(), true); // true = autoflush
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {

				galdetu(out, in, "Nola deitzen zara?");
				galdetu(out, in, "Zenbat urte dauzkazu?");

				// Sarrera itxi -> ProcesoB-k EOF jasotzen du, readLine()-ek null itzultzen du eta amaitzen da
				out.close();

				// Orain bai: B amaituta dagoenez, bere errore guztiak irakur daitezke (ready() erabiliz galdu egin zitezkeen)
				String errorLine;
				while ((errorLine = err.readLine()) != null) {
					System.err.println("ProcesoB ERROREA: " + errorLine);
				}

				int kodea = p.waitFor();
				System.out.println("ProcesoB amaitu da. Irteera kodea: " + kodea);
			}
		} catch (IOException e) {
			System.err.println("Errorea ProcesoB-rekin komunikatzean: " + e.getMessage());
		} catch (InterruptedException e) {
			System.err.println("ProcesoA eten egin da ProcesoB-ren zain zegoela.");
			Thread.currentThread().interrupt();
		}
	}

	private static void galdetu(PrintWriter out, BufferedReader in, String galdera) throws IOException {
		out.println(galdera);
		String erantzuna = in.readLine();
		if (erantzuna == null) {
			System.err.println("ProcesoB-k ez du erantzun (amaitu egin da?).");
		} else {
			System.out.println(galdera + " -> " + erantzuna);
		}
	}
}
