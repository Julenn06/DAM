package ariketa06;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ProcesoB {
	public static void main(String[] args) {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				PrintWriter out = new PrintWriter(System.out, true)) {
			String line;
			while ((line = in.readLine()) != null) {
				if (line.equals("Nola deitzen zara?")) {
					out.println("Nire izena Ane da");
				} else if (line.equals("Zenbat urte dauzkazu?")) {
					out.println("20 urte dauzkat");
				} else {
					out.println("Ez dut galdera hori ulertzen");
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
