package dao.firebase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import controller.DBConnection;
import dao.interfazeak.IWorkoutDAO;
import model.Ariketa;
import model.DAOException;
import model.Workout;
import model.Seriea;

public class FirebaseWorkoutDAO implements IWorkoutDAO {

	private volatile Firestore db;

	public FirebaseWorkoutDAO() {
	}

	/**
	 * Firestore instantzia lortzen du. Ez da konstruktorean egiten, horrela
	 * interfazea (EDT) blokeatzen ez dutelako.
	 */
	private Firestore db() {
		Firestore instantzia = db;
		if (instantzia == null) {
			instantzia = DBConnection.getFirestore();
			db = instantzia;
		}
		return instantzia;
	}

	@Override
	public List<Workout> denakJaso() throws DAOException {
		return jasoWorkoutakQuery(db().collection("workoutak").orderBy("maila"));
	}

	@Override
	/**
	 * Firebase datu basean ez dira erregistroak gainidatziko.
	 * 
	 * @see dao.interfazeak.IDAO#denakIdatzi(java.util.List)
	 */
	public void denakIdatzi(List<Workout> datuak) throws DAOException {
		return;
	}

	@Override
	public List<Workout> irakurriWorkoutMailarekin(int maila) throws DAOException {
		return jasoWorkoutakQuery(db().collection("workoutak").whereEqualTo("maila", maila));
	}

	/**
	 * Firestore Query batetik jaso Workoutak eta haren azpiklaseak
	 *
	 */
	private List<Workout> jasoWorkoutakQuery(Query query) throws DAOException {
		List<Workout> saioak = new ArrayList<>();
		try {
			for (QueryDocumentSnapshot w : query.get().get().getDocuments()) {
				saioak.add(jasoWorkoutQuery(w));
			}
			return saioak;
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Errorea workout-ak irakurtzean", e);
		}
	}

	private Workout jasoWorkoutQuery(DocumentSnapshot query) throws DAOException {
		Workout saioa = Workout.fromDocument(query);

		List<Ariketa> ariketak = new ArrayList<>();
		try {
			for (QueryDocumentSnapshot a : query.getReference().collection("ariketak").get().get().getDocuments()) {
				Ariketa ariketa = Ariketa.fromDocument(a);

				List<Seriea> serieak = new ArrayList<>();
				for (QueryDocumentSnapshot s : a.getReference().collection("serieak").orderBy("izena").get().get()
						.getDocuments()) {
					serieak.add(Seriea.fromDocument(s));
				}
				ariketa.setSerieak(serieak);
				ariketak.add(ariketa);
			}
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Errorea workout-ak irakurtzean", e);
		}
		saioa.setAriketak(ariketak);
		return saioa;
	}

	public DocumentSnapshot getUserDocumentByEmail(String email) throws InterruptedException, ExecutionException {
		if (email == null || email.trim().isEmpty()) {
			return null;
		}

		QuerySnapshot querySnapshot = db().collection("erabiltzaileak").whereEqualTo("email", email).get().get();

		if (querySnapshot.isEmpty()) {
			return null;
		}

		return querySnapshot.getDocuments().get(0);
	}

	@Override
	public String jasoWorkoutID(Workout saioa) throws DAOException {
		try {
			QuerySnapshot querySnapshot = db().collection("workoutak").whereEqualTo("izena", saioa.getIzena())
					.whereEqualTo("deskribapena", saioa.getDeskribapena()).get().get();

			if (!querySnapshot.isEmpty()) {

				String workoutID = querySnapshot.getDocuments().get(0).getId();

				return workoutID;
			}

			return null;
		} catch (InterruptedException | ExecutionException e) {
			throw new DAOException("Errorea Workout ID-a bilatzerakoan: " + e.getMessage(), e);
		}
	}

	public String lortuWorkoutIzena(String workoutId) {
		if (workoutId == null || workoutId.isBlank()) {
			return "";
		}
		try {
			DocumentReference docRef = DBConnection.getFirestore().collection("workoutak").document(workoutId);

			ApiFuture<DocumentSnapshot> future = docRef.get();
			DocumentSnapshot document = future.get();

			if (document.exists()) {
				String izena = document.getString("izena");
				return izena != null ? izena : workoutId;
			}
		} catch (Exception e) {
			System.err.println("Errorea workout-aren izena bilatzean: " + e.getMessage());
		}
		return workoutId;
	}

	@Override
	public int zenbatWorkoutMaila(int maila) throws DAOException {
		int totala = 0;
		try {
			ApiFuture<QuerySnapshot> workoutFuture = db().collection("workoutak")
					.whereEqualTo("maila", maila).get();

			QuerySnapshot workoutSnapshot = workoutFuture.get();

			totala = workoutSnapshot.size();

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Errorea: Konexioa eten da datuak kargatzean.", e);
		} catch (ExecutionException e) {
			throw new DAOException("Errorea Firebase-rekin karga komunikatzean: " + e.getCause().getMessage(), e);
		}
		return totala;

	}

	@Override
	public Workout lortuWorkoutOsoa(String workoutId) throws DAOException {
		try {
			return jasoWorkoutQuery(db().collection("workoutak").document(workoutId).get().get());
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Errorea: Konexioa eten da datuak kargatzean.", e);
		}
	}
}
