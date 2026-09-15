package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Bezeroa - Zerbitzarira objektuak bidaltzen dituen bezeroa
 */
public class Bezeroa {
	private static final String ZERBITZARIA_HELBIDEA = "localhost";
	private static final int PORTUA = 5000;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("=== BEZEROA ===");
		System.out.println("Zerbitzaria: " + ZERBITZARIA_HELBIDEA + ":" + PORTUA);
		System.out.println();

		try {
			// Zerbitzariarekin konektatu
			Socket socket = new Socket(ZERBITZARIA_HELBIDEA, PORTUA);
			System.out.println("Zerbitzariarekin konektatuta!\n");

			// Sarrera eta irteera objektuak sortu
			ObjectOutputStream irteera = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream sarrera = new ObjectInputStream(socket.getInputStream());

			// Erabiltzaileak datuak sartu
			System.out.println("--- Pertsona baten datuak sartu ---");
			System.out.print("Izena: ");
			String izena = scanner.nextLine();

			System.out.print("Adina: ");
			int adina = scanner.nextInt();
			scanner.nextLine(); // Lerro-jauzia kontsumitu

			System.out.print("Herria: ");
			String herria = scanner.nextLine();

			System.out.print("Jaiotze-data (dd/MM/yyyy): ");
			String dataTestua = scanner.nextLine();
			Date jaiotzeData = null;
			try {
				SimpleDateFormat formatua = new SimpleDateFormat("dd/MM/yyyy");
				jaiotzeData = formatua.parse(dataTestua);
			} catch (ParseException e) {
				System.out.println("Errorea data formatuan. Data hutsa erabiliko da.");
			}

			System.out.print("Generoa (Gizona/Emakumea/Bestea): ");
			String generoa = scanner.nextLine();

			// Pertsona objektua sortu
			Pertsona pertsona = new Pertsona(izena, adina, herria, jaiotzeData, generoa);

			// Objektua bidali
			System.out.println("\nObjektua bidaltzen...");
			irteera.writeObject(pertsona);
			irteera.flush();
			System.out.println("Objektua bidali da!");

			// Erantzuna jaso
			String erantzuna = (String) sarrera.readObject();
			System.out.println("\n>>> Zerbitzariaren erantzuna: " + erantzuna);

			// Konexioa itxi
			irteera.close();
			sarrera.close();
			socket.close();
			scanner.close();

			System.out.println("\nKonexioa itxi da. Agur!");

		} catch (IOException e) {
			System.err.println("Errorea zerbitzariarekin konektatzen: " + e.getMessage());
			System.err.println("Ziurtatu zerbitzaria martxan dagoela.");
		} catch (ClassNotFoundException e) {
			System.err.println("Errorea objektua jasotzean: " + e.getMessage());
		} finally {
			scanner.close();
		}
	}
}
