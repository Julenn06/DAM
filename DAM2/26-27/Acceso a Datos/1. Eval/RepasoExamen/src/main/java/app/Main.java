package app;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import controller.DBConnection;
import firebase.FirebasePersonasRepository;
import format.DatPersonas;
import format.JsonPersonas;
import format.TxtPersonas;
import format.XmlPersonas;
import model.Persona;
import view.PersonasView;
import view.PersonasWindow;

public class Main {
	public static void main(String[] args) throws Exception {
		FirebasePersonasRepository repository = new FirebasePersonasRepository(DBConnection.getFirestore(), "personas");
		PersonasView vista = new PersonasView();
		if (args.length > 0 && args[0].equalsIgnoreCase("swing")) {
			javax.swing.SwingUtilities.invokeLater(() -> {
				try {
					new PersonasWindow(repository).setVisible(true);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			return;
		}

		menu(repository, vista);
	}

	private static void menu(FirebasePersonasRepository repository, PersonasView vista) throws Exception {
		Scanner scanner = new Scanner(System.in);
		int opcion;
		do {
			System.out.println("\n===== PERSONAS EN FIREBASE =====");
			System.out.println("1. Listar personas");
			System.out.println("2. Buscar por ID");
			System.out.println("3. Crear persona");
			System.out.println("4. Actualizar persona");
			System.out.println("5. Eliminar persona");
			System.out.println("6. Firebase -> TXT");
			System.out.println("7. Firebase -> DAT");
			System.out.println("8. Firebase -> JSON array");
			System.out.println("9. Firebase -> JSON envolvente");
			System.out.println("10. Firebase -> JSON objeto");
			System.out.println("11. Firebase -> XML elementos");
			System.out.println("12. Firebase -> XML atributos");
			System.out.println("13. TXT -> Firebase");
			System.out.println("14. DAT -> Firebase");
			System.out.println("15. JSON array -> Firebase");
			System.out.println("16. JSON envolvente -> Firebase");
			System.out.println("17. JSON objeto -> Firebase");
			System.out.println("18. XML elementos -> Firebase");
			System.out.println("19. XML atributos -> Firebase");
			System.out.println("20. Crear subdocumento");
			System.out.println("21. Listar subdocumentos con campos");
			System.out.println("22. Buscar subdocumento");
			System.out.println("23. Actualizar subdocumento");
			System.out.println("24. Eliminar subdocumento");
			System.out.println("25. Leer campos de documentos");
			System.out.println("26. Leer IDs de subcoleccion");
			System.out.println("27. Abrir tabla WindowBuilder/Swing");
			System.out.println("0. Salir");
			System.out.print("Opcion: ");
			opcion = Integer.parseInt(scanner.nextLine());

			switch (opcion) {
			case 1 -> vista.mostrar(repository.listar());
			case 2 -> {
				Persona persona = repository.buscarPorId(pedir(scanner, "ID: "));
				if (persona == null) vista.mensaje("No existe esa persona.");
				else vista.mostrar(List.of(persona));
			}
			case 3 -> repository.crear(new Persona(pedir(scanner, "ID: "), pedir(scanner, "Nombre: "),
					LocalDate.parse(pedir(scanner, "Fecha (AAAA-MM-DD): "))));
			case 4 -> repository.actualizar(new Persona(pedir(scanner, "ID: "), pedir(scanner, "Nombre nuevo: "),
					LocalDate.parse(pedir(scanner, "Fecha nueva (AAAA-MM-DD): "))));
			case 5 -> repository.eliminar(pedir(scanner, "ID: "));
			case 6 -> {
				Path carpeta = Path.of("datos-firebase");
				Files.createDirectories(carpeta);
				TxtPersonas.escribirTabla(repository.listar(), carpeta.resolve("personas.txt").toFile());
			}
			case 7 -> {
				Path carpeta = Path.of("datos-firebase");
				Files.createDirectories(carpeta);
				DatPersonas.escribirDat(repository.listar(), carpeta.resolve("personas.dat").toFile());
			}
			case 8 -> {
				Path carpeta = Path.of("datos-firebase");
				Files.createDirectories(carpeta);
				JsonPersonas.escribirArray(repository.listar(), carpeta.resolve("personas-array.json").toFile());
			}
			case 9 -> {
				Path carpeta = Path.of("datos-firebase");
				Files.createDirectories(carpeta);
				JsonPersonas.escribirEnvolvente(repository.listar(), carpeta.resolve("personas-envelope.json").toFile());
			}
			case 10 -> {
				Persona persona = repository.buscarPorId(pedir(scanner, "ID de persona: "));
				if (persona == null) {
					vista.mensaje("No existe esa persona.");
				} else {
					Path carpeta = Path.of("datos-firebase");
					Files.createDirectories(carpeta);
					JsonPersonas.escribirObjeto(persona, carpeta.resolve("persona.json").toFile());
				}
			}
			case 11 -> {
				Path carpeta = Path.of("datos-firebase");
				Files.createDirectories(carpeta);
				XmlPersonas.escribir(repository.listar(), carpeta.resolve("personas.xml").toFile());
			}
			case 12 -> {
				Path carpeta = Path.of("datos-firebase");
				Files.createDirectories(carpeta);
				XmlPersonas.escribirAtributos(repository.listar(), carpeta.resolve("personas-atributos.xml").toFile());
			}
			case 13 -> {
				for (Persona persona : TxtPersonas.leerTabla(new File("datos-firebase", "personas.txt")))
					repository.guardar(persona);
			}
			case 14 -> {
				for (Persona persona : DatPersonas.leerDat(new File("datos-firebase", "personas.dat")))
					repository.guardar(persona);
			}
			case 15 -> {
				for (Persona persona : JsonPersonas.leerArray(new File("datos-firebase", "personas-array.json")))
					repository.guardar(persona);
			}
			case 16 -> {
				for (Persona persona : JsonPersonas.leerEnvolvente(new File("datos-firebase", "personas-envelope.json")))
					repository.guardar(persona);
			}
			case 17 -> repository.guardar(JsonPersonas.leerObjeto(new File("datos-firebase", "persona.json")));
			case 18 -> {
				for (Persona persona : XmlPersonas.leer(new File("datos-firebase", "personas.xml")))
					repository.guardar(persona);
			}
			case 19 -> {
				for (Persona persona : XmlPersonas.leerAtributos(new File("datos-firebase", "personas-atributos.xml")))
					repository.guardar(persona);
			}
			case 20 -> {
				String padre = pedir(scanner, "ID documento padre: ");
				String subcoleccion = pedir(scanner, "Subcoleccion: ");
				String idDocumento = pedir(scanner, "ID subdocumento: ");
				String campo = pedir(scanner, "Nombre del campo: ");
				String valor = pedir(scanner, "Valor del campo: ");
				repository.crearSubdocumento(padre, subcoleccion, idDocumento, Map.of(campo, valor));
			}
			case 21 -> repository.listarSubdocumentos(pedir(scanner, "ID documento padre: "),
					pedir(scanner, "Subcoleccion: ")).forEach(datos -> vista.mensaje(datos.toString()));
			case 22 -> {
				Map<String, Object> datos = repository.buscarSubdocumento(pedir(scanner, "ID documento padre: "),
						pedir(scanner, "Subcoleccion: "), pedir(scanner, "ID subdocumento: "));
				vista.mensaje(datos == null ? "No existe ese subdocumento." : datos.toString());
			}
			case 23 -> {
				String padre = pedir(scanner, "ID documento padre: ");
				String subcoleccion = pedir(scanner, "Subcoleccion: ");
				String idDocumento = pedir(scanner, "ID subdocumento: ");
				String campo = pedir(scanner, "Nombre del campo: ");
				String valor = pedir(scanner, "Valor nuevo: ");
				repository.actualizarSubdocumento(padre, subcoleccion, idDocumento, Map.of(campo, valor));
			}
			case 24 -> repository.eliminarSubdocumento(pedir(scanner, "ID documento padre: "),
					pedir(scanner, "Subcoleccion: "), pedir(scanner, "ID subdocumento: "));
			case 25 -> repository.leerNombresComoCampos().forEach(vista::mensaje);
			case 26 -> repository.leerDocumentosDeColeccion(pedir(scanner, "Subcoleccion: "),
					pedir(scanner, "ID del documento padre: ")).forEach(vista::mensaje);
			case 27 -> abrirTabla(repository);
			case 0 -> System.out.println("Programa terminado.");
			default -> System.out.println("Opcion no valida.");
			}
		} while (opcion != 0);
	}

	private static void abrirTabla(FirebasePersonasRepository repository) throws Exception {
		javax.swing.SwingUtilities.invokeLater(() -> {
			try {
				new PersonasWindow(repository).setVisible(true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	private static String pedir(Scanner scanner, String mensaje) {
		System.out.print(mensaje);
		return scanner.nextLine();
	}

}