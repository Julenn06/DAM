package ariketa01;

public class Ariketa01 implements Runnable {
	private String hariarenIzena;

	public Ariketa01(String izena) {
		this.hariarenIzena = izena;
	}

	@Override
	public void run() {
		System.out.println(hariarenIzena + " hasi da lanean.");

		for (int i = 0; i <= 1000; i++) {
			if (i % 100 == 0) {
				System.out.println(hariarenIzena + " -> Kontagailua: " + i);
			}
		}

		System.out.println(hariarenIzena + " AMAITU DA.");
	}

	public static void main(String[] args) {
		Ariketa01 instantzia1 = new Ariketa01("1. Haria");
		Ariketa01 instantzia2 = new Ariketa01("2. Haria");

		Thread haria1 = new Thread(instantzia1);
		Thread haria2 = new Thread(instantzia2);

		haria1.start();
		haria2.start();

		System.out.println("Main metodoak hariak abiarazi ditu.");
	}
}
