package dao.firebase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import controller.DBConnection;
import controller.Login;
import dao.interfazeak.IHistorikoaDAO;
import model.DAOException;
import model.HistorikoaItem;

public class FirebaseHistorikoaDAO implements IHistorikoaDAO {

	private volatile Firestore db;

	private Firestore db() {
		Firestore instantzia = db;
		if (instantzia == null) {
			instantzia = DBConnection.getFirestore();
			db = instantzia;
		}
		return instantzia;
	}

	public void gordeHistorikoa(HistorikoaItem item) throws DAOException {

		String emailUsuario = Login.getErabil().getEmail();

		if (emailUsuario == null || emailUsuario.isBlank()) {
			throw new DAOException("Errorea: Erabiltzailearen emaila ez da aurkitu.");
		}
		if (item == null) {
			throw new DAOException("Errorea: Ez dago gordetzeko daturik.");
		}

		try {
			Query query = db().collection("erabiltzaileak").whereEqualTo("email", emailUsuario);
			ApiFuture<QuerySnapshot> queryFuture = query.get();

			QuerySnapshot querySnapshot = queryFuture.get();

			if (querySnapshot.isEmpty()) {
				throw new DAOException(
						"Errorea: Ez da aurkitu " + emailUsuario + " posta elektronikoa duen erabiltzailerik.");
			}

			QueryDocumentSnapshot userDoc = querySnapshot.getDocuments().get(0);
			String idRealFirebase = userDoc.getId();

			Map<String, Object> datuak = item.toDoc();
			CollectionReference historikoaRef = db().collection("erabiltzaileak").document(idRealFirebase)
					.collection("historikoa");

			ApiFuture<DocumentReference> insertFuture = historikoaRef.add(datuak);

			insertFuture.get();

			System.out.println("Datuak ondo gorde dira Firebase-n");

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Errorea: Konexioa eten da datuak gordetzean.", e);
		} catch (ExecutionException e) {
			throw new DAOException("Errorea Firebase-rekin komunikatzean: " + e.getCause().getMessage(), e);
		} catch (Exception e) {
			throw new DAOException("Saluespena datuak prozesatzean: " + e.getMessage(), e);
		}
	}

	@Override
	public List<HistorikoaItem> denakJaso() throws DAOException {
		List<HistorikoaItem> guztiak = new ArrayList<>();
		try {
			ApiFuture<QuerySnapshot> erabiltzaileakFuture = db().collection("erabiltzaileak").get();
			QuerySnapshot erabiltzaileakSnapshot = erabiltzaileakFuture.get();

			FirebaseWorkoutDAO workoutDAO = new FirebaseWorkoutDAO();

			for (QueryDocumentSnapshot userDoc : erabiltzaileakSnapshot.getDocuments()) {
				String userId = userDoc.getId();
				String email = userDoc.getString("email");
				CollectionReference historikoaRef = db().collection("erabiltzaileak").document(userId)
						.collection("historikoa");
				ApiFuture<QuerySnapshot> historikoaFuture = historikoaRef.get();
				QuerySnapshot historikoaSnapshot = historikoaFuture.get();

				for (QueryDocumentSnapshot doc : historikoaSnapshot.getDocuments()) {
					HistorikoaItem item = HistorikoaItem.fromDoc(doc, email);

					String workoutId = item.getWorkout();
					String izenaReala = workoutDAO.lortuWorkoutIzena(workoutId);
					item.setWorkout(izenaReala);

					guztiak.add(item);
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Errorea: Konexioa eten da datuak kargatzean.", e);
		} catch (ExecutionException e) {
			throw new DAOException("Errorea Firebase-rekin karga komunikatzean: " + e.getCause().getMessage(), e);
		} catch (Exception e) {
			throw new DAOException("Saluespena datuak kargatzean: " + e.getMessage(), e);
		}
		return guztiak;
	}

	@Override
	public int zenbatWorkoutAmaituta(String email, int maila) throws DAOException {
		Set<String> workoutIdBakarrak = new HashSet<>();

		try {
			ApiFuture<QuerySnapshot> erabiltzaileakFuture = db().collection("erabiltzaileak")
					.whereEqualTo("email", email).get();
			QuerySnapshot erabiltzaileakSnapshot = erabiltzaileakFuture.get();

			if (!erabiltzaileakSnapshot.isEmpty()) {
				String userId = erabiltzaileakSnapshot.getDocuments().get(0).getId();

				QuerySnapshot historikoaSnapshot = db().collection("erabiltzaileak").document(userId)
						.collection("historikoa").whereEqualTo("osatua", 100)
						.whereEqualTo("maila", maila).get().get();

				for (QueryDocumentSnapshot doc : historikoaSnapshot.getDocuments()) {
					String workoutId = doc.getString("workout");

					if (workoutId != null) {
						workoutIdBakarrak.add(workoutId);
					}
				}
			}

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Errorea: Konexioa eten da datuak kargatzean.", e);
		} catch (ExecutionException e) {
			throw new DAOException("Errorea Firebase-rekin karga komunikatzean: " + e.getCause().getMessage(), e);
		} catch (Exception e) {
			throw new DAOException("Saluespena datuak kargatzean: " + e.getMessage(), e);
		}

		return workoutIdBakarrak.size();
	}

	@Override
	/**
	 * Firebase datu basean ez dira erregistroak gainidatziko.
	 * 
	 * @see dao.interfazeak.IDAO#denakIdatzi(java.util.List)
	 */
	public void denakIdatzi(List<HistorikoaItem> datuak) throws DAOException {
		return;
	}

	@Override
	public List<HistorikoaItem> lortuErabiltzailearenHistorikoak(String email) throws DAOException {
		List<HistorikoaItem> historikoak = new ArrayList<>();

		if (email == null || email.isBlank()) {
			throw new DAOException("Errorea: Erabiltzailearen emaila ez da aurkitu.");
		}

		try {
			Query query = db().collection("erabiltzaileak").whereEqualTo("email", email);
			ApiFuture<QuerySnapshot> queryFuture = query.get();
			QuerySnapshot querySnapshot = queryFuture.get();

			if (querySnapshot.isEmpty()) {
				throw new DAOException(
						"Errorea: Ez da aurkitu " + email + " posta elektronikoa duen erabiltzailerik.");
			}

			QueryDocumentSnapshot userDoc = querySnapshot.getDocuments().get(0);
			String idRealFirebase = userDoc.getId();

			CollectionReference historikoaRef = db().collection("erabiltzaileak").document(idRealFirebase)
					.collection("historikoa");
			ApiFuture<QuerySnapshot> historikoaFuture = historikoaRef.get();
			QuerySnapshot historikoaSnapshot = historikoaFuture.get();

			FirebaseWorkoutDAO workoutDAO = new FirebaseWorkoutDAO();

			for (QueryDocumentSnapshot doc : historikoaSnapshot.getDocuments()) {
				HistorikoaItem item = HistorikoaItem.fromDoc(doc, email);

				String workoutId = item.getWorkout();

				String izenaReala = workoutDAO.lortuWorkoutIzena(workoutId);

				item.setWorkout(izenaReala);

				historikoak.add(item);

			}

		} catch (

		InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Errorea: Konexioa eten da datuak kargatzean.", e);
		} catch (ExecutionException e) {
			throw new DAOException("Errorea Firebase-rekin karga komunikatzean: " + e.getCause().getMessage(), e);
		} catch (Exception e) {
			throw new DAOException("Saluespena datuak kargatzean: " + e.getMessage(), e);
		}

		return historikoak;
	}
}
