package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import dao.ErabiltzaileCache;

public class DBConnection {

	private static volatile Firestore firestoreInstance = null;
	private static volatile boolean warmUpHasita = false;

	/**
	 * Firestore instantzia sortu eta gorde Singleton bezala
	 * 
	 * @return Firestore instantzia edo null
	 */
	public static synchronized Firestore initialize() {
		if (firestoreInstance != null) {
			return firestoreInstance;
		}

		// Firestore zerbitzarira iritsi daitekeen ala ez egiaztatu
		if (!isHostReachable("firestore.googleapis.com", 443, 3000)) {
			System.err.println("[ERROR] Ezin da firestore.googleapis.com zerbitzarira iritsi (konexio arazoa).");
			return null;
		}

		try {
			if (System.getProperty("os.name").toLowerCase().contains("win")) {
				System.setProperty("javax.net.ssl.trustStore", "NONE");
				System.setProperty("javax.net.ssl.trustStoreType", "WINDOWS-ROOT");
			}

			if (FirebaseApp.getApps().isEmpty()) {
				FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");

				FirebaseOptions options = FirebaseOptions.builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

				FirebaseApp.initializeApp(options);
				System.out.println("[INFO] Firebase-rekin arrakastaz konektatuta");
			}

			firestoreInstance = FirestoreClient.getFirestore();
			return firestoreInstance;

		} catch (IOException e) {
			System.err.println("[ERROR] Errorea Firebase-rekin konektatzean: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Ostalari bat eskuragarri dagoen ala ez egiaztatzeko metodo laguntzailea.
	 */
	private static boolean isHostReachable(String host, int port, int timeoutMs) {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, port), timeoutMs);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Firestore instantzia jaso
	 * 
	 * @return Firestore instantzia edo null
	 */
	public static Firestore getFirestore() {
		Firestore instantzia = firestoreInstance;
		if (instantzia == null) {
			instantzia = initialize();
		}
		return instantzia;
	}

	/**
	 * Konexioa atzematzen du background hari batean: OAuth tokenua eskuratzen du,
	 * gRPC kanala irekitzen du eta erabiltzaileen cachea abiarazten du.
	 *
	 * Honela erabiltzaileak saioa hasteko unean ez du saretik request-ik egingo eta
	 * loginak berehala erantzuten dio.
	 *
	 * Blokeatzen ez du: hari esperientziala da.
	 */
	public static void warmUpAsync() {

		if (warmUpHasita) {
			return;
		}
		warmUpHasita = true;

		Thread hari = new Thread(() -> {
			for (int saiakera = 1; saiakera <= 2; saiakera++) {
				try {
					Firestore db = getFirestore();
					if (db == null) {
						return;
					}

					// Tokenua eskuratzen du, kanala irekitzen du eta index plana prestatzen du.
					db.collection("erabiltzaileak").limit(1).get().get();

					// Erabiltzaileak memorian mantentzen dituen cachea abiarazten du.
					ErabiltzaileCache.getInstance().start(db);

					System.out.println("[INFO] Firebase konexioa prest");
					return;
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				} catch (ExecutionException | RuntimeException e) {
					System.err.println("[WARN] Firebase konexioa prestatzean: " + e.getMessage());
					try {
						Thread.sleep(3000);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						return;
					}
				}
			}
		}, "firebase-warmup");

		hari.setDaemon(true);
		hari.start();
	}
}
