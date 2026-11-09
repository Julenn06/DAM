package model;

import java.util.Random;

public class Piloto extends Thread {
	private String nombre;
	private String equipo;
	private double progreso;

	public Piloto(String nombre, String equipo) {
		this.nombre = nombre;
		this.equipo = equipo;
		this.progreso = 0.0;
	}

	public String getNombre() {
		return nombre;
	}

	public String getEquipo() {
		return equipo;
	}

	public double getProgreso() {
		return progreso;
	}

	@Override
	public void run() {
		Random random = new Random();

		for (int i = 1; i <= 24; i++) {
			try {
				int tiempoVuelta = random.nextInt(301) + 100;
				Thread.sleep(tiempoVuelta);

				double incremento = 1.0 + (4.0 * random.nextDouble());
				this.progreso += incremento;

				System.out.println(i + ". itzulia - " + nombre + " (" + equipo + ")");

			} catch (InterruptedException e) {
				// Eten badute, lasterketa utzi. interrupt() berriz jarri eta begiztan
				// jarraituz gero, hurrengo sleep()-ek berehala jaurtiko luke berriz.
				System.out.println(nombre + " arazo bat izan du bueltan eta lasterketa utzi du.");
				Thread.currentThread().interrupt();
				return;
			}
		}

		System.out.println(" ¡" + nombre + " (" + equipo + ") HELMUGA GURUTZATU DU!");
	}
}
