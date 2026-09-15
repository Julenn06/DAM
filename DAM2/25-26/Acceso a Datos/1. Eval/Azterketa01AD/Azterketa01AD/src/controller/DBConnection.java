package controller;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class DBConnection {

	private static Firestore db;

	public static void initialize() {
		try {
			if (FirebaseApp.getApps().isEmpty()) {
				FileInputStream serviceAccount = new FileInputStream("libros-ex-security-2526.json");

				FirebaseOptions options = FirebaseOptions.builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

				FirebaseApp.initializeApp(options);
				System.out.println("Firebase ondo hasita");
			}
			db = FirestoreClient.getFirestore();
		} catch (IOException e) {
			System.err.println("Errorea firebase hastean: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static Firestore getFirestore() {
		if (db == null) {
			initialize();
		}
		return db;
	}
}
