package ariketa01;

import java.io.IOException;

public class Arik_1 {
	public static void main(String[] args) {
		try {
			new ProcessBuilder("calc.exe").start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
