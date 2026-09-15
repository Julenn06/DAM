package vista;

import java.util.Scanner;

import javax.xml.xpath.XPath;

import org.w3c.dom.Document;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		XPath xPath = Methods.llamadaXPath();
		Document doc = Methods.llamadaDOC();
		if (xPath == null || doc == null) {
			System.out.println(
					"Error al cargar el archivo XML o inicializar XPath, comprueba el archivo y vuelve a intentarlo.");
			sc.close();
			return;
		}
		int aukera;
		do {
			aukera = Methods.menu(sc);
			switch (aukera) {
			case 1:
				Methods.caso1(xPath, doc);
				break;
			case 2:
				Methods.caso2(xPath, doc);
				break;
			case 3:
				Methods.caso3(xPath, doc);
				break;
			case 4:
				Methods.caso4(xPath, doc);
				break;
			case 5:
				Methods.caso5(xPath, doc);
				break;
			case 6:
				System.out.println("\n Saliendo... \n");
				break;
			default:
				System.out.println("\n Opción no válida \n");
			}
		} while (aukera != 6);
		sc.close();
	}
}