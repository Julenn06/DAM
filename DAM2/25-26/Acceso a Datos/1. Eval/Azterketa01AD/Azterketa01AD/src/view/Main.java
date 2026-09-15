package view;

import java.text.ParseException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import controller.BuscarPorCategoria;
import controller.GenerarLibrosXML;
import controller.GuardarPrecios;
import controller.NuevoLibro;
import model.Libros;

public class Main {

	public static void main(String[] args) throws InterruptedException, ExecutionException,
			ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, ParseException {

		Scanner sc = new Scanner(System.in);
		GuardarPrecios gordePrezioak = new GuardarPrecios();
		GenerarLibrosXML xmlGorde = new GenerarLibrosXML();
		NuevoLibro liburuBerria = new NuevoLibro();
		BuscarPorCategoria bilatu = new BuscarPorCategoria();
		int aukera = 0;
		do {

			System.out.println("1. Liburuen izenburua eta prezioa gorde Precios.dat");
			System.out.println("2. Liburuen datu guztiak xml GORDE");
			System.out.println("3. TXT datuk BD eta XML gorde");
			System.out.println("4. Gorde web kategoriako liburuak eta prezioa 29.95 baino handiagoak direnak");
			System.out.println("5. Atera");
			aukera = sc.nextInt();

			switch (aukera) {

			case 1:
				gordePrezioak.gordeDatuak();
				gordePrezioak.saveToDAT();
				break;
			case 2:
				xmlGorde.saveToXML();
				break;
			case 3:
				liburuBerria.insertTxtToDB("LiburuBerria-2526.txt");
				xmlGorde.saveToXML();
				break;
			case 4:
				List<Libros> liburuak2 = bilatu.gordeDatuak();
				for (Libros l : liburuak2) {
					System.out.println(l.getIsbn() + l.getTitle());
				}
				if (liburuak2 != null) {
					bilatu.gordeDatuak(liburuak2);
				}
				break;
			case 5:
				System.out.println("Agur Roman!");
				break;
			}
		} while (aukera != 5);
		sc.close();

	}

}
