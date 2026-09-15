package main;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FireBaseTest {

	public static void main(String[] args) throws Exception {
		FireBaseConfig.initialize();

		Firestore db = FirestoreClient.getFirestore();

		Scanner sc = new Scanner(System.in);

		int aukera = 0;
		do {
			System.out.println("1. Añadir usuario");
			System.out.println("2. Leer usuario");
			System.out.println("3. Actualizar usuario");
			System.out.println("4. Borrar usuario");
			System.out.println("5. Salir");
			aukera = sc.nextInt();

			switch (aukera) {

			case 1:
				Map<String, Object> usuario = new HashMap<>();
				usuario.put("nombre", "Julen");
				usuario.put("edad", 25);

				ApiFuture<WriteResult> resultado = db.collection("usuarios").document("chumacho").set(usuario);
				System.out.println("Documento guardado en: " + resultado.get().getUpdateTime());
				break;

			case 2:
				ApiFuture<QuerySnapshot> query = db.collection("usuarios").get();
				QuerySnapshot querySnapshot = query.get();
				for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
					System.out.println("Documento ID: " + doc.getId() + " => Datos: " + doc.getData());
				}
				break;

			case 3:
				Map<String, Object> actualizacion = new HashMap<>();
				actualizacion.put("edad", 26);

				ApiFuture<WriteResult> writeResult = db.collection("usuarios").document("chumacho")
						.update(actualizacion);
				System.out.println("Documento actualizado en: " + writeResult.get().getUpdateTime());
				break;

			case 4:
				ApiFuture<WriteResult> deleteResult = db.collection("usuarios").document("chumacho").delete();
				System.out.println("Documento borrado en: " + deleteResult.get().getUpdateTime());
				break;

			case 5:
				System.out.println("Saliendo...");
				break;

			}

		} while (aukera != 5);

		sc.close();
	}
}
