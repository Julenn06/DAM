package view;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import controller.DBConnection;
import controller.HeroesController;

public class Main {

	public static void main(String[] args)
			throws InterruptedException, ExecutionException, IOException, ParseException {

		HeroesController heroes = new HeroesController();
		Scanner sc = new Scanner(System.in);
		int opcion = 0;
		String nombreEditorial = "";

		DBConnection.getFirestore();

		do {
			System.out.println("Que quieres hacer?");
			opcion = sc.nextInt();
		} while (Double.isNaN(opcion) && opcion < 1 || opcion > 6);

		switch (opcion) {

		case 1:
			System.out.println("Insertar Datos");
			heroes.cargarDatos();
			break;
		case 2:
			System.out.println("Buscar por Editorial");
			System.out.println("Escribe el Nombre de la Editorial");
			sc.nextLine();
			nombreEditorial = sc.nextLine();
			heroes.listarPorEditorial(nombreEditorial);
		}

		sc.close();
	}

}
