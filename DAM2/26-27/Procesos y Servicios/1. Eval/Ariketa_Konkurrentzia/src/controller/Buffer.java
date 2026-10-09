package controller;

public class Buffer {
	private char edukia;
	private boolean bufferBeteta = false;

	public synchronized void jarri(char c) {
		while (bufferBeteta) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		this.edukia = c;
		this.bufferBeteta = true;
		System.out.println("Ekoizleak jarri du: " + c);

		notifyAll();
	}

	public synchronized char recoger() {
		while (!bufferBeteta) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		this.bufferBeteta = false;
		char lagunartekoa = edukia;

		notifyAll();

		return lagunartekoa;
	}
}
