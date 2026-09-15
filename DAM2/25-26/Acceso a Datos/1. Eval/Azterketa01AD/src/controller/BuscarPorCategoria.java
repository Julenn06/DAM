package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import model.Libros;

public class BuscarPorCategoria {

	private final Firestore db;

	public BuscarPorCategoria() {
		this.db = DBConnection.getFirestore();
	}

	public List<Libros> gordeDatuak() {
		List<Libros> liburuak = new ArrayList<>();
		try {
			ApiFuture<QuerySnapshot> future = db.collection("liburuak").whereEqualTo("category", "web").get();
			QuerySnapshot querySnapshot = future.get();

			for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {
				Libros liburua = mapToLiburuak(doc);
				if (liburuak != null) {
					liburuak.add(liburua);
				}
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

		return liburuak;
	}

	private Libros mapToLiburuak(DocumentSnapshot doc) {
		try {
			Libros liburua = new Libros();
			liburua.setIsbn(doc.getString("isbn"));
			liburua.setTitle(doc.getString("title"));

			if (doc.getDouble("price") > 29.95) {
				return liburua;
			} else {
				return null;
			}

		} catch (Exception e) {
			System.err.println("✗ Errorea: " + e.getMessage());
			return null;
		}
	}

	public boolean gordeDatuak(List<Libros> liburuak2) {
		try {
			if (liburuak2.isEmpty()) {
				System.out.println("[WARN] Ez daude libururik gordetzeko.");
				return false;
			}

			Path out = Paths.get("Web.txt");
			try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(out, StandardCharsets.UTF_8,
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
				pw.println("ISBN		IZENA		YEAR");
				pw.println("-------------------------------");
				for (Libros l : liburuak2) {
					pw.println(l.isbnIzena());
				}
				pw.println("-------------------------------");
				pw.println("Total liburuak: " + liburuak2.size());
			}

			System.out.println("[INFO] Liburuak Atera dira:" + out.toAbsolutePath());
			return true;

		} catch (IOException e) {
			System.err.println("[ERROR] Errorea Liburuak Ateratzena: " + e.getMessage());
			return false;
		}
	}

}
