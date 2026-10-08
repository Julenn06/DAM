package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import model.Heroes;
import model.ListarEditorial;

public class HeroesController {

	private Firestore db;
	private SimpleDateFormat dateFormat;

	public HeroesController() {
		this.db = DBConnection.getFirestore();
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	public void cargarDatos() throws ParseException, InterruptedException, ExecutionException {

		List<Heroes> comicsBrisa = new ArrayList<>();
		comicsBrisa.add(new Heroes("Brisa", 1, "12/04/1987", 1.50, "PROTAGONISTA"));
		comicsBrisa.add(new Heroes("La Liga del Norte", 3, "10/02/1990", 1.75, "SECUNDARIO"));
		comicsBrisa.add(new Heroes("Brisa", 50, "01/06/2001", 2.95, "PROTAGONISTA"));
		insertarHeroeConComics("Brisa", "Ane Etxeberria", "Ediciones Ilargi", "Bilbao",
				Arrays.asList("vuelo", "control del viento"), true, "12/04/1987", comicsBrisa);

		List<Heroes> comicsHormigon = new ArrayList<>();
		comicsHormigon.add(new Heroes("Hormigón", 1, "03/09/1979", 0.90, "PROTAGONISTA"));
		comicsHormigon.add(new Heroes("Hormigón contra Doctor Voltio", 1, "01/10/1985", 1.25, "PROTAGONISTA"));
		comicsHormigon.add(new Heroes("La Liga del Norte", 3, "10/02/1990", 1.75, "PROTAGONISTA"));
		insertarHeroeConComics("Hormigón", "Iker Zubiaurre", "Ediciones Ilargi", "Donostia",
				Arrays.asList("superfuerza", "invulnerabilidad"), true, "03/09/1979", comicsHormigon);

		List<Heroes> comicsMarea = new ArrayList<>();
		comicsMarea.add(new Heroes("Marea", 1, "05/11/2003", 3.50, "PROTAGONISTA"));
		comicsMarea.add(new Heroes("La Liga del Norte", 100, "01/03/2010", 3.95, "SECUNDARIO"));
		insertarHeroeConComics("Marea", "Nerea Olaizola", "Ediciones Ilargi", "Bilbao",
				Arrays.asList("respiración acuática", "control del agua"), true, "05/11/2003", comicsMarea);

		List<Heroes> comicsSombra = new ArrayList<>();
		comicsSombra.add(new Heroes("Sombra Lunar", 1, "20/01/1995", 2.25, "PROTAGONISTA"));
		comicsSombra.add(new Heroes("Noches de Valencia", 7, "15/08/1998", 2.50, "SECUNDARIO"));
		insertarHeroeConComics("Sombra Lunar", "Lucía Ferrer", "Norte Cómics", "Valencia",
				Arrays.asList("invisibilidad", "teletransporte"), true, "20/01/1995", comicsSombra);

		List<Heroes> comicsVoltio = new ArrayList<>();
		comicsVoltio.add(new Heroes("Doctor Voltio", 1, "15/06/1968", 0.50, "PROTAGONISTA"));
		comicsVoltio.add(new Heroes("Hormigón contra Doctor Voltio", 1, "01/10/1985", 1.25, "SECUNDARIO"));
		insertarHeroeConComics("Doctor Voltio", "Marcos Ibarra", "Norte Cómics", "Madrid",
				Arrays.asList("control de la electricidad", "vuelo"), false, "15/06/1968", comicsVoltio);
	}

	private void insertarHeroeConComics(String nombre, String identidad, String editorial, String ciudad,
			List<String> poderes, boolean activo, String fechaStr, List<Heroes> listaHeroes)
			throws ParseException, InterruptedException, ExecutionException {

		Date primeraAparicion = dateFormat.parse(fechaStr);

		Map<String, Object> heroeData = new HashMap<>();
		heroeData.put("nombre", nombre);
		heroeData.put("identidad", identidad);
		heroeData.put("editorial", editorial);
		heroeData.put("ciudad", ciudad);
		heroeData.put("poderes", poderes);
		heroeData.put("activo", activo);
		heroeData.put("primera_aparicion", primeraAparicion);

		ApiFuture<DocumentReference> futureHeroe = db.collection("Heroes").add(heroeData);
		DocumentReference heroeDocRef = futureHeroe.get();
		System.out.println("\n[Héroe] " + nombre + " guardado con ID: " + heroeDocRef.getId());

		for (Heroes c : listaHeroes) {
			Date fechaComic = dateFormat.parse(c.getFecha());

			Map<String, Object> comicMap = new HashMap<>();
			comicMap.put("titulo", c.getTitulo());
			comicMap.put("numero", c.getNumero());
			comicMap.put("fecha", fechaComic);
			comicMap.put("precio", c.getPrecio());
			comicMap.put("papel", c.getPapel());

			ApiFuture<DocumentReference> futureComic = heroeDocRef.collection("Comics").add(comicMap);
			DocumentReference comicDocRef = futureComic.get();
			System.out.println("· [Cómic] " + c.getTitulo() + " (Nº" + c.getNumero() + ") ID subdocumento: "
					+ comicDocRef.getId());
		}
	}

	public void listarPorEditorial(String editorialNombre) throws InterruptedException, ExecutionException {
		List<ListarEditorial> editoriales = new ArrayList<>();

		// 1. Buscamos usando el campo "editorial" en minúsculas.
		// Evitamos usar .orderBy() aquí para que NO salte el error del índice compuesto
		// de Firebase.
		Query query = db.collection("Heroes").whereEqualTo("editorial", editorialNombre);
		QuerySnapshot querySnapshot = query.get().get();

		for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {
			// 2. Extraemos los campos usando los nombres exactos en minúsculas de tus
			// capturas
			String nombre = doc.getString("nombre");
			String ciudad = doc.getString("ciudad");

			ListarEditorial heroeEditorial = new ListarEditorial(nombre, ciudad);
			editoriales.add(heroeEditorial);
		}

		// 3. Ordenamos alfabéticamente por el nombre del héroe directamente en Java
		Collections.sort(editoriales, new Comparator<ListarEditorial>() {
			@Override
			public int compare(ListarEditorial h1, ListarEditorial h2) {
				if (h1.getNombre() == null || h2.getNombre() == null)
					return 0;
				return h1.getNombre().compareToIgnoreCase(h2.getNombre());
			}
		});

		// 4. Mostramos el resultado limpio por consola
		System.out.println("\nHéroes de la editorial [" + editorialNombre + "]:");
		if (editoriales.isEmpty()) {
			System.out.println("No se encontraron héroes para esta editorial.");
		} else {
			for (ListarEditorial heroe : editoriales) {
				System.out.println("- " + heroe.getNombre() + " (" + heroe.getCiudad() + ")");
			}
		}
	}
}
