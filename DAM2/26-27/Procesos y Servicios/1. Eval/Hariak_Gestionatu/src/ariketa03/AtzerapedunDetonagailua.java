package ariketa03;

class AtzerapedunDetonagailua extends Thread {
	private String izena;
	private int kontagailua;

	public AtzerapedunDetonagailua(String izena, int kontagailua) {
		this.izena = izena;
		this.kontagailua = kontagailua;
	}

	@Override
	public void run() {
		try {
			while (kontagailua > 0) {
				System.out.println("[" + izena + "] Kontagailua: " + kontagailua);
				kontagailua--;
				Thread.sleep(50);
			}
			System.out.println("--> " + izena + " AMAITU DU.");
		} catch (InterruptedException e) {
			System.out.println(izena + " eten egin da.");
		}
	}
}
