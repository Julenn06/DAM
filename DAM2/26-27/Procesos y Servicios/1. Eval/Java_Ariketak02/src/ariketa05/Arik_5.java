package ariketa05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Arik_5 {
	public static void main(String[] args) {
		try {
			Process p = new ProcessBuilder("tasklist").start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			boolean running = false;
			// Ez egin break: irteera osoa irakurri behar da. Erdian utziz gero, tasklist
			// blokea daiteke bere irteera (pipe-a) beteta dagoelako, eta waitFor() inoiz ez litzateke amaituko.
			while ((line = r.readLine()) != null) {
				if (line.toLowerCase().contains("notepad.exe")) {
					running = true;
				}
			}
			p.waitFor();

			if (running) {
				Process kill = new ProcessBuilder("taskkill", "/F", "/IM", "notepad.exe").start();
				int kodea = kill.waitFor();
				System.out.println(kodea == 0 ? "Ohar-bloka itxi da." : "Ezin izan da ohar-bloka itxi.");
			} else {
				System.out.println("Ohar-bloka ez dago martxan.");
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
