package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.google.cloud.firestore.Firestore;

public class NuevoLibro {

	private final Firestore db;

	public NuevoLibro() {
		this.db = DBConnection.getFirestore();
	}

	public void insertTxtToDB(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			Map<String, Object> data = new HashMap<>();
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(":", 2);
				if (parts.length == 2) {
					String key = parts[0].trim();
					String value = parts[1].trim();

					switch (key) {
					case "category":
						data.put(key, value);
						break;
					case "isbn":
						data.put(key, value);
						break;
					case "title":
						data.put(key, value);
						break;
					case "lang":
						data.put(key, value);
						break;
					case "author":
						data.put(key, value);
						break;
					case "year":
						data.put(key, Integer.parseInt(value));
						break;
					case "price":
						data.put(key, Double.parseDouble(value));
						break;
					case "date":
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						data.put(key, sdf.parse(value));
						break;
					default:
						data.put(key, value);
						break;
					}
				}
			}

			db.collection("liburuak").add(data);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
}