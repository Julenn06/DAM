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

public class GuardarPrecios {

	private final Firestore db;

	public GuardarPrecios() {
		this.db = DBConnection.getFirestore();
	}

	public List<Libros> gordeDatuak() {
		List<Libros> liburuak = new ArrayList<>();
		try {
			ApiFuture<QuerySnapshot> future = db.collection("liburuak").get();
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

	public void datuakIkusi(Libros l) {
		System.out.println("Izena" + l.getTitle());
		System.out.println("Prezioa" + l.getPrice());
	}

	private Libros mapToLiburuak(DocumentSnapshot doc) {
		try {
			Libros liburua = new Libros();
			liburua.setTitle(doc.getString("title"));
			liburua.setPrice(doc.getDouble("price"));

			return liburua;
		} catch (Exception e) {
			System.err.println("✗ Errorea: " + e.getMessage());
			return null;
		}
	}

	public boolean saveToDAT() {
		try {
			List<Libros> libro = gordeDatuak();
			if (libro.isEmpty()) {
				System.out.println("[WARN] Ez daude libururik gordetzeko.");
				return false;
			}

			Path out = Paths.get("Precios.dat");
			try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(out, StandardCharsets.UTF_8,
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {

				for (Libros l : libro) {
					pw.println(l.IzenaPrezioa());
				}
			}

			System.out.println("[INFO] Liburuak Atera dira:" + out.toAbsolutePath());
			return true;

		} catch (IOException e) {
			System.err.println("[ERROR] Errorea Liburuak Ateratzena: " + e.getMessage());
			return false;
		}
	}

}
