package ariketa02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Arik_2 {
	public static void main(String[] args) {
		try {
			Process p = new ProcessBuilder("cmd.exe", "/c", "date /t").start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = r.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
