package ariketa02;

public class Nagusia2 {
	public static void main(String[] args) {
		Idazle hariZenbakiak = new Idazle(true);
		Idazle hariLetrak = new Idazle(false);

		System.out.println("Exekuzioa hasten da (irteera nahasia):");
		hariZenbakiak.start();
		hariLetrak.start();
	}
}
