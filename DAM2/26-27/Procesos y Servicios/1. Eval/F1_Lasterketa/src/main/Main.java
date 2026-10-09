package main;

import java.util.ArrayList;
import java.util.List;

import model.Piloto;

public class Main {
	public static void main(String[] args) {
		List<Piloto> pilotos = new ArrayList<>();
		pilotos.add(new Piloto("George Russell", "Mercedes"));
		pilotos.add(new Piloto("Charles Leclerc", "Ferrari"));
		pilotos.add(new Piloto("Lando Norris", "McLaren"));
		pilotos.add(new Piloto("Kimi Antonelli", "Mercedes"));
		pilotos.add(new Piloto("Oscar Piastri", "McLaren"));
		pilotos.add(new Piloto("Max Verstappen", "Red Bull"));

		pilotos.get(5).setPriority(Thread.MAX_PRIORITY);
		pilotos.get(2).setPriority(8);
		pilotos.get(1).setPriority(Thread.NORM_PRIORITY);
		pilotos.get(4).setPriority(5);
		pilotos.get(0).setPriority(4);
		pilotos.get(3).setPriority(Thread.MIN_PRIORITY);

		System.out.println("Semaforoak Amatatu Dira!\n");

		for (Piloto p : pilotos) {
			p.start();
		}

		for (Piloto p : pilotos) {
			try {
				p.join();
			} catch (InterruptedException e) {
				System.out.println("Haria amaitu da.");
			}
		}

		System.out.println("\n LASTERKETA AMAITU DA!");

		pilotos.sort((p1, p2) -> Double.compare(p2.getProgreso(), p1.getProgreso()));

		System.out.println("\n === SAILKAPEN FINALA ===");
		for (int i = 0; i < pilotos.size(); i++) {
			Piloto p = pilotos.get(i);
			int posicion = i + 1;

			System.out.println("   " + posicion + "º posición: " + p.getNombre() + " [" + p.getEquipo()
					+ "] - Progreso: " + String.format("%.2f", p.getProgreso()));
		}
	}
}
