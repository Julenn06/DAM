package firebase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import model.Persona;

public class FirebasePersonasRepository {
	private final Firestore db;
	private final String coleccion;

	public FirebasePersonasRepository(Firestore db, String coleccion) {
		this.db = db;
		this.coleccion = coleccion;
	}

	public void guardar(Persona persona) throws ExecutionException, InterruptedException {
		db.collection(coleccion).document(persona.getId()).set(campos(persona)).get();
	}

	public void crear(Persona persona) throws ExecutionException, InterruptedException {
		db.collection(coleccion).document(persona.getId()).create(campos(persona)).get();
	}

	public List<Persona> listar() throws ExecutionException, InterruptedException {
		List<Persona> resultado = new ArrayList<>();
		for (DocumentSnapshot doc : db.collection(coleccion).get().get().getDocuments()) {
			resultado.add(convertir(doc));
		}
		return resultado;
	}

	public Persona buscarPorId(String id) throws ExecutionException, InterruptedException {
		DocumentSnapshot doc = db.collection(coleccion).document(id).get().get();
		return doc.exists() ? convertir(doc) : null;
	}

	public void actualizar(Persona persona) throws ExecutionException, InterruptedException {
		db.collection(coleccion).document(persona.getId()).update(campos(persona)).get();
	}

	public void eliminar(String id) throws ExecutionException, InterruptedException {
		db.collection(coleccion).document(id).delete().get();
	}

	public List<String> leerNombresComoCampos() throws ExecutionException, InterruptedException {
		List<String> nombres = new ArrayList<>();
		for (DocumentSnapshot doc : db.collection(coleccion).get().get().getDocuments()) {
			nombres.add(doc.getId() + " -> " + doc.getString("nombre"));
		}
		return nombres;
	}

	public List<String> leerDocumentosDeColeccion(String subcoleccion, String idPadre)
			throws ExecutionException, InterruptedException {
		List<String> documentos = new ArrayList<>();
		CollectionReference ref = db.collection(coleccion).document(idPadre).collection(subcoleccion);
		for (DocumentSnapshot doc : ref.get().get().getDocuments())
			documentos.add(doc.getId());
		return documentos;
	}

	public void crearSubdocumento(String idPadre, String subcoleccion, String idDocumento,
			Map<String, Object> datos) throws ExecutionException, InterruptedException {
		db.collection(coleccion).document(idPadre).collection(subcoleccion).document(idDocumento).create(datos).get();
	}

	public List<Map<String, Object>> listarSubdocumentos(String idPadre, String subcoleccion)
			throws ExecutionException, InterruptedException {
		List<Map<String, Object>> resultado = new ArrayList<>();
		for (DocumentSnapshot doc : db.collection(coleccion).document(idPadre).collection(subcoleccion).get().get()
				.getDocuments()) {
			Map<String, Object> datos = new LinkedHashMap<>(doc.getData());
			datos.put("id", doc.getId());
			resultado.add(datos);
		}
		return resultado;
	}

	public Map<String, Object> buscarSubdocumento(String idPadre, String subcoleccion, String idDocumento)
			throws ExecutionException, InterruptedException {
		DocumentSnapshot doc = db.collection(coleccion).document(idPadre).collection(subcoleccion)
				.document(idDocumento).get().get();
		if (!doc.exists()) return null;
		Map<String, Object> datos = new LinkedHashMap<>(doc.getData());
		datos.put("id", doc.getId());
		return datos;
	}

	public void actualizarSubdocumento(String idPadre, String subcoleccion, String idDocumento,
			Map<String, Object> datos) throws ExecutionException, InterruptedException {
		db.collection(coleccion).document(idPadre).collection(subcoleccion).document(idDocumento).update(datos).get();
	}

	public void eliminarSubdocumento(String idPadre, String subcoleccion, String idDocumento)
			throws ExecutionException, InterruptedException {
		db.collection(coleccion).document(idPadre).collection(subcoleccion).document(idDocumento).delete().get();
	}

	private Map<String, Object> campos(Persona persona) {
		Map<String, Object> campos = new LinkedHashMap<>();
		campos.put("nombre", persona.getNombre());
		campos.put("fechaNacimiento", persona.getFechaNacimiento().toString());
		campos.put("edad", persona.getEdad());
		return campos;
	}

	private Persona convertir(DocumentSnapshot doc) {
		return new Persona(doc.getId(), doc.getString("nombre"), LocalDate.parse(doc.getString("fechaNacimiento")));
	}
}