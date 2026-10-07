package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import model.Usuario;

public class Usuarios {

	private Firestore db;

	public Usuarios() {
		this.db = DBConnection.getFirestore();
	}

	public void viewUsuarios() throws InterruptedException, ExecutionException {
		List<Usuario> usuarios = new ArrayList<>();

		QuerySnapshot querySnapshot = db.collection("usuarios").get().get();

		for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {

			String nombre = doc.getString("nombre");
			String contraseña = doc.getString("contraseña");

			Usuario usuario = new Usuario(nombre, contraseña);
			usuarios.add(usuario);
		}

		for (Usuario usuario : usuarios) {
			System.out.println(usuario.getNombre() + " " + usuario.getContraseña());
		}

	}

}
