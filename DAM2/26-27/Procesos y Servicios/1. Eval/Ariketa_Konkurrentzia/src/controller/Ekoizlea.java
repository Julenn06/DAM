package controller;

import java.util.Random;

public class Ekoizlea extends Thread {
	private Buffer buffer;

	public Ekoizlea(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		Random random = new Random();
		try {
			for (int i = 0; i < 10; i++) {
				char letra = (char) (random.nextInt(26) + 'A');
				buffer.jarri(letra); // mezua Buffer-ak inprimatzen du (synchronized barruan, ordena zuzena)
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			System.out.println("Ekoizlea eten egin da.");
		}
	}
}
