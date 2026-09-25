package ariketa03;

class HiloThread extends Thread {
	private String izena;
	private int noraArte;

	public HiloThread(String izena, int noraArte) {
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

public class Ariketa03 {
	public static void main(String[] args) {
		HiloThread haria1 = new HiloThread("Haria-A", 5);
		HiloThread haria2 = new HiloThread("Haria-B", 8);

		haria1.start();
		haria2.start();

		System.out.println("[Main] Hariak abiarazi dira (Thread heredatuz). Jarraipena hasten...");

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
