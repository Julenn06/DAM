package dao;

import com.google.cloud.firestore.Firestore;

import controller.DBConnection;
import dao.firebase.FirebaseErabiltzaileaDAO;
import dao.firebase.FirebaseHistorikoaDAO;
import dao.firebase.FirebaseWorkoutDAO;
import dao.interfazeak.IErabiltzaileDAO;
import dao.interfazeak.IHistorikoaDAO;
import dao.interfazeak.IWorkoutDAO;
import dao.offline.OfflineErabiltzaileaDAO;
import dao.offline.OfflineHistorikoaDAO;
import dao.offline.OfflineWorkoutDAO;

public class DAOGetter {

	private static DAOGetter instance;

	private IWorkoutDAO workoutDAO;
	private IErabiltzaileDAO erabiltzaileaDAO;
	private IHistorikoaDAO historikoaDAO;

	private OfflineWorkoutDAO offlineWorkoutDAO;
	private OfflineErabiltzaileaDAO offlineErabiltzaileaDAO;
	private OfflineHistorikoaDAO offlineHistorikoaDAO;

	// Lineaz kanpo dagoen ala ez adierazteko boolearra
	private boolean lineazKanpo = false;

	private DAOGetter() {
		// Konexioa egiaztatu instantzia sortzen den unean
		try {
			Firestore db = DBConnection.getFirestore();
			if (db == null) {
				this.lineazKanpo = true;
				System.out.println("[WARN] Konexiorik ez. Lineaz kanpoko moduan abiarazten...");
			} else {
				this.lineazKanpo = false;
				System.out.println("[INFO] Konexioa egiaztatu da. Linean.");
			}
		} catch (Exception e) {
			this.lineazKanpo = true;
			System.err.println("[ERROR] Salbuespena konexioa egiaztatzean. Lineaz kanpo pasatzen...");
		}
	}

	public static synchronized DAOGetter getInstance() {
		if (instance == null) {
			instance = new DAOGetter();
		}
		return instance;
	}

	/**
	 * Lineaz kanpo egongo den ala ez itzultzen du.
	 * 
	 * @return true lineaz kanpo badago, false bestela.
	 */
	public boolean isLineazKanpo() {
		return lineazKanpo;
	}

	public synchronized IWorkoutDAO getWorkoutDAO() {
		if (workoutDAO == null) {
			if (lineazKanpo) {
				workoutDAO = getOfflineWorkoutDAO();
			} else {
				workoutDAO = new FirebaseWorkoutDAO();
			}
		}
		return workoutDAO;
	}

	public synchronized IErabiltzaileDAO getErabiltzaileaDAO() {
		if (erabiltzaileaDAO == null) {
			if (lineazKanpo) {
				erabiltzaileaDAO = getOfflineErabiltzaileaDAO();
			} else {
				erabiltzaileaDAO = new FirebaseErabiltzaileaDAO();
			}
		}
		return erabiltzaileaDAO;
	}

	public synchronized IHistorikoaDAO getHistorikoaDAO() {
		if (historikoaDAO == null) {
			if (lineazKanpo) {
				historikoaDAO = getOfflineHistorikoaDAO();
			} else {
				historikoaDAO = new FirebaseHistorikoaDAO();
			}
		}
		return historikoaDAO;
	}

	public OfflineWorkoutDAO getOfflineWorkoutDAO() {
		if (offlineWorkoutDAO == null) {
			offlineWorkoutDAO = new OfflineWorkoutDAO();
		}
		return offlineWorkoutDAO;
	}

	public OfflineErabiltzaileaDAO getOfflineErabiltzaileaDAO() {
		if (offlineErabiltzaileaDAO == null) {
			offlineErabiltzaileaDAO = new OfflineErabiltzaileaDAO();
		}
		return offlineErabiltzaileaDAO;
	}

	public OfflineHistorikoaDAO getOfflineHistorikoaDAO() {
		if (offlineHistorikoaDAO == null) {
			offlineHistorikoaDAO = new OfflineHistorikoaDAO();
		}
		return offlineHistorikoaDAO;
	}
}