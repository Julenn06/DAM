package dao;

import java.util.List;

import dao.interfazeak.IErabiltzaileDAO;
import dao.interfazeak.IHistorikoaDAO;
import dao.interfazeak.IWorkoutDAO;
import dao.offline.OfflineErabiltzaileaDAO;
import dao.offline.OfflineHistorikoaDAO;
import dao.offline.OfflineWorkoutDAO;
import model.DAOException;
import model.Erabiltzailea;
import model.HistorikoaItem;
import model.Workout;

public class Backups {
	private IErabiltzaileDAO erabiltzaileDAO = DAOGetter.getInstance().getErabiltzaileaDAO();
	private OfflineErabiltzaileaDAO offlineErabiltzaileDAO = DAOGetter.getInstance().getOfflineErabiltzaileaDAO();
	private IWorkoutDAO workoutDAO = DAOGetter.getInstance().getWorkoutDAO();
	private OfflineWorkoutDAO offlineWorkoutDAO = DAOGetter.getInstance().getOfflineWorkoutDAO();
	private IHistorikoaDAO historikoaDAO = DAOGetter.getInstance().getHistorikoaDAO();
	private OfflineHistorikoaDAO offlineHistorikoaDAO = DAOGetter.getInstance().getOfflineHistorikoaDAO();

	public Backups() {
	}

	public void backupWorkouts() throws DAOException {
		List<Workout> lista = workoutDAO.denakJaso();
		offlineWorkoutDAO.denakIdatzi(lista);
	}

	public void backupErabiltzaileak() throws DAOException {
		List<Erabiltzailea> lista = erabiltzaileDAO.denakJaso();
		offlineErabiltzaileDAO.denakIdatzi(lista);
	}

	public void backupHistorikoa() throws DAOException {
		List<HistorikoaItem> historikoa = historikoaDAO.denakJaso();
		offlineHistorikoaDAO.denakIdatzi(historikoa);
	}
}