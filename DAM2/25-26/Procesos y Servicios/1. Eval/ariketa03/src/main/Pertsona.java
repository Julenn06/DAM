package main;

public class Pertsona {
	private String izena;

	public Pertsona(String izena) {
		this.izena = izena;
		Thread thread = new Thread(() -> {
			for (int i = 1; i <= 3; i++) {
				System.out.println(izena + " - Eragiketa: " + i);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		thread.start();
	}
	
}