package ariketa01;

public class Erabiltzaile extends Thread {
	private String izena;

	public Erabiltzaile(String izena) {
		this.izena = izena;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < 3; i++) {
				System.out.println(izena + ": operazio " + i);
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			System.out.println(izena + " eten egin da.");
		}
	}
}
