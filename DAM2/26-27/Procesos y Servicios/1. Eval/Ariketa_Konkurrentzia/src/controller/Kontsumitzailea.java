package controller;

public class Kontsumitzailea extends Thread {
	private Buffer buffer;

	public Kontsumitzailea(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			char letra = buffer.recoger();
			System.out.println("Kontsumitzaileak jaso du: '" + letra + "'");

			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
