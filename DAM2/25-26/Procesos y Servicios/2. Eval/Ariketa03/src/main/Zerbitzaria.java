package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Zerbitzaria - Bezeroengandik objektuak jasotzen dituen zerbitzaria
 */
public class Zerbitzaria {
	private static final int PORTUA = 5000;

	public static void main(String[] args) {
		System.out.println("=== ZERBITZARIA HASITA ===");
		System.out.println("Portua: " + PORTUA);
		System.out.println("Bezeroen zain...\n");

		try (ServerSocket zerbitzariSocket = new ServerSocket(PORTUA)) {

			while (true) {
				// Bezero baten zain
				Socket bezeroSocket = zerbitzariSocket.accept();
				System.out.println("Bezeroa konektatu da: " + bezeroSocket.getInetAddress().getHostAddress());

				// Bezero bakoitza harialdi berri batean kudeatu
				Thread haria = new Thread(new BezeroaKudeatzailea(bezeroSocket));
				haria.start();
			}

		} catch (IOException e) {
			System.err.println("Errorea zerbitzaria abiaraztean: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

/**
 * BezeroaKudeatzailea - Bezero bakoitza harialdi bereizietan kudeatzeko
 */
class BezeroaKudeatzailea implements Runnable {
	private Socket bezeroSocket;

	public BezeroaKudeatzailea(Socket socket) {
		this.bezeroSocket = socket;
	}

	@Override
	public void run() {
		try (ObjectInputStream sarrera = new ObjectInputStream(bezeroSocket.getInputStream());
				ObjectOutputStream irteera = new ObjectOutputStream(bezeroSocket.getOutputStream())) {
			// Objektua jaso
			Object jasotakoObjektua = sarrera.readObject();

			if (jasotakoObjektua instanceof Pertsona) {
				Pertsona pertsona = (Pertsona) jasotakoObjektua;
				System.out.println("\n>>> Pertsona bat jaso da:");
				System.out.println("    " + pertsona);

				// Erantzuna bidali
				String erantzuna = "Ongi jaso da: " + pertsona.getIzena();
				irteera.writeObject(erantzuna);
				irteera.flush();

				System.out.println("    Erantzuna bidali da bezeroari\n");
			} else {
				System.out.println("Objektu mota ezezaguna jaso da");
			}

		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Errorea bezeroaren datuak prozesatzean: " + e.getMessage());
		} finally {
			try {
				bezeroSocket.close();
				System.out.println("Bezeroa deskonektatu da\n");
			} catch (IOException e) {
				System.err.println("Errorea socketa ixtean: " + e.getMessage());
			}
		}
	}
}
