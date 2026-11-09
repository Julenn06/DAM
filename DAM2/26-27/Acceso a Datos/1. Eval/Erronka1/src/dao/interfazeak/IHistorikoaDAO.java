package dao.interfazeak;

import java.util.List;

import model.DAOException;
import model.HistorikoaItem;

public interface IHistorikoaDAO extends IDAO<HistorikoaItem> {

	/**
	 * Historiko item berri bat erregistratzen du.
	 * 
	 * @param item HistorikoaItem instantzia
	 * @throws DAOException Datu-base errorean.
	 */
	public void gordeHistorikoa(HistorikoaItem item) throws DAOException;

	/**
	 * Erabiltzaile baten historikoa jasotzen du.
	 * 
	 * @param email Erabiltzailaren e-maila
	 * @return Historikoa
	 * @throws DAOException Datu-base errorean.
	 */
	public List<HistorikoaItem> lortuErabiltzailearenHistorikoak(String email) throws DAOException;

	/**
	 * Erabiltzaileak maila zehatz bateko gainditu dituen workout kopurua.
	 * 
	 * @param email Erabiltzailaren e-maila
	 * @param maila Iragazteko maila
	 * @return Workouten kopurua
	 * @throws DAOException Datu-base errorean.
	 */
	public int zenbatWorkoutAmaituta(String email, int maila) throws DAOException;
}
