package ariketa03;

import java.util.ArrayList;
import java.util.List;

public class Nagusia3 {
	public static void main(String[] args) {
		List<AtzerapedunDetonagailua> hariak = new ArrayList<>();

		hariak.add(new AtzerapedunDetonagailua("Detonagailu-A", 4));
		hariak.add(new AtzerapedunDetonagailua("Detonagailu-B", 3));
		hariak.add(new AtzerapedunDetonagailua("Detonagailu-C", 5));
		hariak.add(new AtzerapedunDetonagailua("Detonagailu-D", 2));

		for (AtzerapedunDetonagailua hari : hariak) {
			hari.start();
		}

		for (AtzerapedunDetonagailua hari : hariak) {
			try {
				hari.join();
			} catch (InterruptedException e) {
				System.out.println("Hari nagusia eten da itxoiten ari zela.");
			}
		}

		System.out.println("PROZESU GUZTIAK AMAITU DIRA. PROGRAMAREN AMAIERA.");
	}
}