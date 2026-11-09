package controller;

public class Kontsumitzailea extends Thread {
	private Buffer buffer;

	public Kontsumitzailea(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < 10; i++) {
				buffer.recoger(); // mezua Buffer-ak inprimatzen du
				Thread.sleep(250);
			}
		} catch (InterruptedException e) {
			System.out.println("Kontsumitzailea eten egin da.");
		}
	}
}
