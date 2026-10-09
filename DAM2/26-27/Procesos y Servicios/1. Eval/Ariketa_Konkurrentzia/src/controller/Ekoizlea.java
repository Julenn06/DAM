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
		for (int i = 0; i < 10; i++) {
			char letra = (char) (random.nextInt(26) + 'A');
			buffer.jarri(letra);
			System.out.println("Ekoizleak jarri du: " + letra);

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
