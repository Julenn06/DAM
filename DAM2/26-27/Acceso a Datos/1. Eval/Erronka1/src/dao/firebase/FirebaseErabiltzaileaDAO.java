package dao.firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import controller.DBConnection;
import dao.ErabiltzaileCache;
import dao.interfazeak.IErabiltzaileDAO;
import model.DAOException;
import model.Erabiltzailea;
import utils.Hash;

public class FirebaseErabiltzaileaDAO implements IErabiltzaileDAO {

	private static final String KOLEKZIOA = "erabiltzaileak";

	private volatile Firestore db;

	public FirebaseErabiltzaileaDAO() {
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

	/**
	 * Saioa hasten du.
	 *
	 * Lehenik memoria cachea kontsultatzen da (sareko galderarik gabe,
	 * instantanea). Cachean ez badago, email bakarra bilatzen da Firestore-n: honek
	 * indize automatikoa erabiltzen du (ez da konposatu indizearen beharra) eta
	 * pasahitza aplikazioak konparatzen du.
	 *
	 * @param email     Erabiltzailearen e-maila
	 * @param pasahitza Erabiltzailearen pasahitza
	 * @return Erabiltzailea instantzia aurkituz gero, null bestela
	 * @throws DAOException Datu-baseko errore bat gertatzean
	 */
	@Override
	public Erabiltzailea saioaHasi(String email, String pasahitza) throws DAOException {
		String emailLodia = email == null ? "" : email.trim();

		// Memoria cachetik saiatu jasotzen
		Erabiltzailea jasotakoErabiltzaile = ErabiltzaileCache.getInstance().bilatu(emailLodia);
		if (jasotakoErabiltzaile != null) {
			return pasahitzaEgokia(jasotakoErabiltzaile, pasahitza) ? jasotakoErabiltzaile : null;
		}

		// Bestela firebasetik jaso
		try {
			QueryDocumentSnapshot doc = emailBilatu(emailLodia);
			if (doc == null) {
				return null;
			}

			Erabiltzailea erab = Erabiltzailea.fromDoc(doc);
			ErabiltzaileCache.getInstance().eguneratu(erab);

			return pasahitzaEgokia(erab, pasahitza) ? erab : null;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Hari-interrupzioa gertatu da saioa hastean", e);
		} catch (ExecutionException e) {
			throw new DAOException("Errorea gertatu da datu-basearekin komunikazioan saioa hastean", e);
		}
	}

	/**
	 * Email bidez erabiltzailea bilatzen du
	 */
	private QueryDocumentSnapshot emailBilatu(String email) throws InterruptedException, ExecutionException {
		List<QueryDocumentSnapshot> docs = db().collection(KOLEKZIOA).whereEqualTo("email", email).limit(1).get().get()
				.getDocuments();

		if (docs.isEmpty() && !email.equals(email.toLowerCase())) {
			docs = db().collection(KOLEKZIOA).whereEqualTo("email", email.toLowerCase()).limit(1).get().get()
					.getDocuments();
		}

		return docs.isEmpty() ? null : docs.get(0);
	}

	/**
	 * Pasahitza bat datorren erabiltzailearena den ala ez.
	 */
	private boolean pasahitzaEgokia(Erabiltzailea erab, String pasahitza) {
		if (erab == null || erab.getPasahitza() == null || pasahitza == null) {
			return false;
		}

		return Hash.konprobatu(pasahitza.toCharArray(), erab.getPasahitza());
	}

	@Override
	public boolean registratu(Erabiltzailea erab) throws DAOException {
		try {
			List<QueryDocumentSnapshot> existingUsers = db().collection(KOLEKZIOA)
					.whereEqualTo("email", erab.getEmail()).limit(1).get().get().getDocuments();

			if (!existingUsers.isEmpty()) {
				return false; // E-mail jada existitzen da.
			}

			String hashedPassword = Hash.hasheatu(erab.getPasahitza().toCharArray());
			erab.setPasahitza(hashedPassword);

			Map<String, Object> erabiltzaileData = new HashMap<>();
			erabiltzaileData.put("izena", erab.getIzena());
			erabiltzaileData.put("abizena1", erab.getAbizena1());
			erabiltzaileData.put("abizena2", erab.getAbizena2());
			erabiltzaileData.put("email", erab.getEmail());
			erabiltzaileData.put("pasahitza", hashedPassword);
			erabiltzaileData.put("jaiotzeData", erab.getJaiotzeData());
			erabiltzaileData.put("maila", 0);

			DocumentReference docRef = db().collection(KOLEKZIOA).add(erabiltzaileData).get();

			boolean onartu = docRef != null && docRef.getId() != null;
			if (onartu) {
				ErabiltzaileCache.getInstance().eguneratu(erab);
			}
			return onartu;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Hari-interrupzioa gertatu da erregistratzean", e);
		} catch (ExecutionException e) {
			throw new DAOException("Errorea gertatu da datu-basearekin komunikazioan erregistratzean", e);
		}
	}

	@Override
	public List<Erabiltzailea> denakJaso() throws DAOException {
		List<Erabiltzailea> erabiltzaileak = new ArrayList<>();
		try {
			List<QueryDocumentSnapshot> docs = db().collection(KOLEKZIOA).get().get().getDocuments();
			for (QueryDocumentSnapshot q : docs) {
				erabiltzaileak.add(Erabiltzailea.fromDoc(q));
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new DAOException("Ezin izan dira erabiltzaileak FireBase-etik jaso", e);
		}
		return erabiltzaileak;
	}

	@Override
	public void profilaEguneratu(Erabiltzailea erab, String pasahitzaZaharra) throws DAOException {
		try {
			List<QueryDocumentSnapshot> existingUsers = db().collection(KOLEKZIOA)
					.whereEqualTo("email", erab.getEmail().trim()).limit(1).get().get().getDocuments();

			if (existingUsers.isEmpty()) {
				throw new DAOException("Ez da aurkitu erabiltzailerik email horrekin");
			}

			DocumentSnapshot userDoc = existingUsers.get(0);
			String pasahitzaDDBB = userDoc.getString("pasahitza");

			if (pasahitzaDDBB == null || pasahitzaZaharra == null) {
				throw new DAOException("Pasahitz okerra");
			}

			boolean vZaharrak = Hash.konprobatu(pasahitzaZaharra.toCharArray(), pasahitzaDDBB);

			if (!vZaharrak) {
				throw new DAOException("Pasahitz okerra");
			}

			String nirePasahitzaBerriaHash = Hash.hasheatu(erab.getPasahitza().toCharArray());

			Map<String, Object> eguneraketak = new HashMap<>();
			eguneraketak.put("izena", erab.getIzena().trim());
			eguneraketak.put("abizena1", erab.getAbizena1().trim());
			eguneraketak.put("abizena2", erab.getAbizena2().trim());
			eguneraketak.put("jaiotzeData", erab.getJaiotzeData());
			eguneraketak.put("pasahitza", nirePasahitzaBerriaHash);

			erab.setPasahitza(nirePasahitzaBerriaHash);

			DocumentReference docRef = userDoc.getReference();
			docRef.update(eguneraketak).get();

			ErabiltzaileCache.getInstance().eguneratu(erab);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Hari-interrupzioa gertatu da eguneratzean", e);
		} catch (ExecutionException e) {
			throw new DAOException("Errorea gertatu da datu-basearekin", e);
		}
	}

	@Override
	/**
	 * Firebase datu basean ez dira erregistroak gainidatziko.
	 * 
	 * @see dao.interfazeak.IDAO#denakIdatzi(java.util.List)
	 */
	public void denakIdatzi(List<Erabiltzailea> datuak) throws DAOException {
		return;
	}

	@Override
	public boolean mailaEguneratu(Erabiltzailea erab, int mailaBerria) throws DAOException {
		try {
			List<QueryDocumentSnapshot> existingUsers = db().collection(KOLEKZIOA).whereEqualTo("email", erab).limit(1)
					.get().get().getDocuments();

			if (existingUsers.isEmpty()) {
				return false;
			}

			DocumentReference userDocRef = existingUsers.get(0).getReference();

			// Datu basea eguneratu
			Map<String, Object> erabiltzaileData = new HashMap<>();
			erabiltzaileData.put("maila", mailaBerria);

			userDocRef.update(erabiltzaileData).get();

			// Cache-a eguneratu
			erab.setMaila(mailaBerria);

			ErabiltzaileCache.getInstance().eguneratu(erab);

			return true;

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Hari-interrupzioa gertatu da maila eguneratzean", e);
		} catch (ExecutionException e) {
			throw new DAOException("Errorea gertatu da datu-basearekin komunikazioan maila eguneratzean", e);
		}
	}

	@Override
	public void izenaEguneratu(Erabiltzailea erab) throws DAOException {
		try {
			List<QueryDocumentSnapshot> existingUsers = db().collection(KOLEKZIOA)
					.whereEqualTo("email", erab.getEmail().trim()).limit(1).get().get().getDocuments();

			if (existingUsers.isEmpty()) {
				throw new DAOException("Ez da aurkitu erabiltzailerik email horrekin");
			}

			DocumentSnapshot userDoc = existingUsers.get(0);

			Map<String, Object> eguneraketak = new HashMap<>();
			eguneraketak.put("izena", erab.getIzena().trim());
			eguneraketak.put("abizena1", erab.getAbizena1().trim());
			eguneraketak.put("abizena2", erab.getAbizena2().trim());

			DocumentReference docRef = userDoc.getReference();
			docRef.update(eguneraketak).get();

			ErabiltzaileCache.getInstance().eguneratu(erab);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new DAOException("Hari-interrupzioa gertatu da eguneratzean", e);
		} catch (ExecutionException e) {
			throw new DAOException("Errorea gertatu da datu-basearekin", e);
		}

	}
}
