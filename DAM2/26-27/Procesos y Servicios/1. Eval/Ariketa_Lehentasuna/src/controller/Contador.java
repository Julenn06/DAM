package controller;

public class Contador extends Thread {

	private String izena = "";
	private int noraArte = 0;
	private volatile boolean terminar = false;

	public Contador(String izena, int noraArte) {
		this.izena = izena;
		this.noraArte = noraArte;
	}

	public void amaitu() {
		this.terminar = true;
		this.interrupt();
	}

	@Override
	public void run() {
		System.out.println(izena + " hasi da.");

		for (int i = 1; i <= noraArte && !terminar; i++) {
			System.out.println(izena + ": " + i + "   (lehentasuna " + izenaPrioritateaLortu() + ")");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(izena + " haria eten egin da.");
			}
		}

		System.out.println(izena + " amaitu da.");
	}

	private String izenaPrioritateaLortu() {
		int p = this.getPriority();
		if (p == Thread.MAX_PRIORITY)
			return "MAX";
		if (p == Thread.MIN_PRIORITY)
			return "MIN";
		if (p == Thread.NORM_PRIORITY)
			return "NORM";
		return String.valueOf(p);
	}
}