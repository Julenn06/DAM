package ariketa01;

import java.util.Scanner;

public class HiloContador {

	public static void main(String[] args) {

		Contador haria = new Contador("Hilo 1", 42);
		Contador haria2 = new Contador("Hilo 2", 38);
		Contador haria3 = new Contador("Hilo 3", 51);

		haria.setPriority(Thread.NORM_PRIORITY);
		haria2.setPriority(Thread.NORM_PRIORITY);
		haria3.setPriority(Thread.NORM_PRIORITY);

		haria.start();
		haria2.start();
		haria3.start();

		Scanner scanner = new Scanner(System.in);
		boolean atera = false;

		while (!atera) {
			mostrarMenu();
			System.out.print("> ");
			String comando = scanner.nextLine().trim().toLowerCase();

			switch (comando) {
			case "f":
				haria.amaitu();
				haria2.amaitu();
				haria3.amaitu();
				System.out.println("Hari guztiak amaitzeko eskaera bidali da.");
				break;
			case "q":
				haria.amaitu();
				haria2.amaitu();
				haria3.amaitu();
				atera = true;
				System.out.println("Programatik irteten...");
				break;
			default:
				procesarComandoEspecifico(comando, haria, haria2, haria3);
				break;
			}
		}
		scanner.close();
	}

	private static void mostrarMenu() {
		System.out.println("\nKomandoak: [1-3]+ lehentasuna igo · [1-3]- lehentasuna jaitsi");
		System.out.println("           [1-3]f hari hori amaitu · f guztiak amaitu · q irten");
	}

	private static void procesarComandoEspecifico(String comando, Contador h1, Contador h2, Contador h3) {
		if (comando.length() != 2) {
			System.out.println("Errorea: Komando ezezaguna.");
			return;
		}

		char numeroHilo = comando.charAt(0);
		char accion = comando.charAt(1);

		Contador helmuga = null;
		if (numeroHilo == '1')
			helmuga = h1;
		else if (numeroHilo == '2')
			helmuga = h2;
		else if (numeroHilo == '3')
			helmuga = h3;

		if (helmuga == null) {
			System.out.println("Errorea: Hari hori ez da existitzen (1 eta 3 artean bakarrik).");
			return;
		}

		switch (accion) {
		case '+':
			prioritateaAldatu(helmuga, 1);
			break;
		case '-':
			prioritateaAldatu(helmuga, -1);
			break;
		case 'f':
			helmuga.amaitu();
			System.out.println(helmuga.getName() + " amaitzeko eskaera bidali da.");
			break;
		default:
			System.out.println("Errorea: Ekintza ezezaguna (+, - edo f).");
			break;
		}
	}

	private static void prioritateaAldatu(Contador hilo, int incremento) {
		int nuevaPrioridad = hilo.getPriority() + incremento;

		if (nuevaPrioridad > Thread.MAX_PRIORITY) {
			System.out.println("Ezin da lehentasuna gehiago igo (MAX_PRIORITY da dagoeneko).");
		} else if (nuevaPrioridad < Thread.MIN_PRIORITY) {
			System.out.println("Ezin da lehentasuna gehiago jaitsi (MIN_PRIORITY da dagoeneko).");
		} else {
			hilo.setPriority(nuevaPrioridad);
			System.out.println(hilo.getName() + "-(r)en lehentasun berria: " + nuevaPrioridad);
		}
	}
}
