package view;

import java.util.concurrent.ExecutionException;

import controller.DBConnection;
import controller.Usuarios;

public class Main {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		// 1. Forzar a Java a usar el almacén de certificados nativo de Windows
		System.setProperty("javax.net.ssl.trustStore", "NONE");
		System.setProperty("javax.net.ssl.trustStoreType", "WINDOWS-ROOT");

		// 2. Inicializar la conexión con Firebase de manera segura
		DBConnection.initialize();

		// 3. Instanciar el controlador y listar los alumnos de Firestore
		Usuarios usuario = new Usuarios();
		usuario.viewUsuarios();
	}

}
