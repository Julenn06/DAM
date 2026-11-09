package dao;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import model.Erabiltzailea;

/**
 * Erabiltzaileak memorian mantentzen dituen cachea.
 *
 * Firestore listener baten bidez "erabiltzaileak" kolekziora sinkronizatzen
 * da, beraz saioa hasteko unean ez da sareko requestik egin behar:
 * erabiltzailea
 * memorian bilatzen da eta emaitza berehala (milisegundotan) itzultzen da.
 *
 * Cachea prest dagoen bitartean, sarea erabiltzen da (fallback).
 */
public final class ErabiltzaileCache {

	private static final ErabiltzaileCache INSTANCE = new ErabiltzaileCache();

	/** Erabiltzaileak, email normalizatuaren arabera indexatuta. */
	private volatile Map<String, Erabiltzailea> erabiltzaileak = new HashMap<>();
	private volatile ListenerRegistration listener;
	private volatile boolean prest = false;

	private ErabiltzaileCache() {
	}

	public static ErabiltzaileCache getInstance() {
		return INSTANCE;
	}

	/**
	 * Firestore listenerra abiarazten du.
	 * 
	 * @param db Firestore instantzia
	 */
	public synchronized void start(Firestore db) {
		if (listener != null || db == null) {
			return;
		}

		try {
			listener = db.collection("erabiltzaileak").addSnapshotListener((snapshot, error) -> {
				if (error != null) {
					System.err.println("[WARN] Erabiltzaile cachearen errorea: " + error.getMessage());
					return;
				}
				if (snapshot == null) {
					return;
				}

				Map<String, Erabiltzailea> berriak = new HashMap<>();
				for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {
					Erabiltzailea erab;
					try {
						erab = Erabiltzailea.fromDocHistorikoarekin(doc);
						if (erab.getEmail() != null) {
							berriak.put(normalizatu(erab.getEmail()), erab);
						}
					} catch (InterruptedException | ExecutionException e) {
						System.err.println("[WARN] Ezin izan da erabiltzailea jaso: " + e.getMessage());
					}
				}

				erabiltzaileak = berriak;
				prest = true;
			});
		} catch (RuntimeException e) {
			System.err.println("[WARN] Erabiltzaile cachea ez da abiarazi: " + e.getMessage());
		}
	}

	/**
	 * Erabiltzailea cachean bilatzen du
	 *
	 * @param email Erabiltzailearen emaila
	 * @return Erabiltzailea edo null
	 */
	public Erabiltzailea bilatu(String email) {
		if (email == null || email.trim().isEmpty()) {
			return null;
		}
		return erabiltzaileak.get(normalizatu(email));
	}

	/**
	 * Erabiltzailea cachean gordetzen edo eguneratzen du.
	 */
	public synchronized void eguneratu(Erabiltzailea erab) {
		if (erab == null || erab.getEmail() == null) {
			return;
		}
		Map<String, Erabiltzailea> berriak = new HashMap<>(erabiltzaileak);
		berriak.put(normalizatu(erab.getEmail()), erab);
		erabiltzaileak = berriak;
	}

	/**
	 * Erabiltzailea cachean ezabatzen du.
	 */
	public synchronized void ezabatu(String email) {
		if (email == null) {
			return;
		}
		Map<String, Erabiltzailea> berriak = new HashMap<>(erabiltzaileak);
		berriak.remove(normalizatu(email));
		erabiltzaileak = berriak;
	}

	/**
	 * Cachea lehenengo aldiz bete da den ala ez.
	 */
	public boolean prestDago() {
		return prest;
	}

	/**
	 * Emaila normalizatzen du (inguruko hutsuneak kendu eta letra txikietara
	 * pasatu).
	 */
	public static String normalizatu(String email) {
		return email == null ? "" : email.trim().toLowerCase();
	}
}
