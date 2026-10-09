package main;

import controller.Buffer;
import controller.Ekoizlea;
import controller.Kontsumitzailea;

public class Main {
	public static void main(String[] args) {
		Buffer buffer = new Buffer();

		Ekoizlea ekoizlea = new Ekoizlea(buffer);
		Kontsumitzailea kontsumitzailea = new Kontsumitzailea(buffer);

		ekoizlea.start();
		kontsumitzailea.start();
	}
}
