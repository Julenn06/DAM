package ariketa04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Arik_4 {
	public static void main(String[] args) {
		try {
			Process p = new ProcessBuilder("tasklist").start();
			// Windows-eko kontsola CP850 kodeketan idazten du: horrela azentuak ondo ateratzen dira
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), "CP850"));
			String line;
			while ((line = r.readLine()) != null) {
				System.out.println(line);
			}
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
