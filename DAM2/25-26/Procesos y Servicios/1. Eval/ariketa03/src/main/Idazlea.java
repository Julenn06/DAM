package main;

public class Idazlea {
	
	private String izena;
	private Boolean zenbakia;
	
	
	public Idazlea(String izena, boolean zenbakia) {
		this.izena = izena;
		this.zenbakia = zenbakia;
		Thread thread = new Thread(() -> {
			if (zenbakia) {
				for (int i = 1; i <= 30; i++) {
					System.out.println(izena + " - Zenbakia: " + i);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			} else {
				for (char c = 'a'; c <= 'z'; c++) {
					System.out.println(izena + " - Letra: " + c);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		});
		thread.start();
	}
}