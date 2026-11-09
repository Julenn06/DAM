package controller;

public class Buffer {
	private char edukia;
	private boolean bufferBeteta = false;

	// InterruptedException gorantz bidaltzen da: haria eteten badute, wait()-etik
	// irten eta deitzaileak (run()) erabakitzen du zer egin. Catch-ean berriz
	// interrupt() egin eta begiztan jarraitzeak begizta infinitu bat sortzen zuen.
	public synchronized void jarri(char c) throws InterruptedException {
		while (bufferBeteta) {
			wait();
		}

		this.edukia = c;
		this.bufferBeteta = true;
		System.out.println("Ekoizleak jarri du: " + c);

		notifyAll();
	}

	public synchronized char recoger() throws InterruptedException {
		while (!bufferBeteta) {
			wait();
		}

		this.bufferBeteta = false;
		char lagunartekoa = edukia;
		System.out.println("Kontsumitzaileak jaso du: '" + lagunartekoa + "'");

		notifyAll();

		return lagunartekoa;
	}
}
