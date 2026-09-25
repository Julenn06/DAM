package ariketa02;

class RunnableHaria implements Runnable {
	private String izena;
	private int noraArte;

	public RunnableHaria(String izena, int noraArte) {
		this.izena = izena;
		this.noraArte = noraArte;
	}

	@Override
	public void run() {
		System.out.println("-> " + izena + " hasi da.");

		for (int i = 1; i <= noraArte; i++) {
			System.out.println(izena + ": " + i);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				System.out.println(izena + " eten egin da.");
			}
		}

		System.out.println("-> " + izena + " AMAITU DA.");
	}
}

public class Ariketa02 {
	public static void main(String[] args) {
		Thread haria1 = new Thread(new RunnableHaria("Haria-A", 5));
		Thread haria2 = new Thread(new RunnableHaria("Haria-B", 8));

		haria1.start();
		haria2.start();

		System.out.println("[Main] Hariak abiarazi dira. Jarraipena hasten...");

		while (haria1.isAlive() || haria2.isAlive()) {
			System.out.println("[Main] Hariak lanean ari dira oraindik...");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("[Main] Bi hariak amaitu dira. Begiztatik irten naiz. AGUR!");
	}
}
