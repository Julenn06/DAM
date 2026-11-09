package dao.interfazeak;

import java.util.List;

import model.DAOException;
import model.Workout;

public interface IWorkoutDAO extends IDAO<Workout> {
	/**
	 * Maila zehatz bateko workout guztiak jaso.
	 * 
	 * @param maila Iragazteko maila
	 * @return Workouten zerrenda
	 * @throws DAOException Datu-base errorean
	 */
	List<Workout> irakurriWorkoutMailarekin(int maila) throws DAOException;

	/**
	 * Workout zehatz baten identifikatzailea jaso.
	 * 
	 * Firebase-en, hango ID-a jasoko du.
	 * 
	 * Lineaz kanpo, workout-aren izena jasoko du.
	 * 
	 * @param saioa Workout instantzia
	 * @return Identifikatzailea
	 * @throws DAOException Datu-base errorean
	 */
	String jasoWorkoutID(Workout saioa) throws DAOException;

	/**
	 * Maila zehatz bat duten workouten kopurua jaso.
	 * 
	 * @param maila Iragazteko maila
	 * @return Workout kopurua
	 * @throws DAOException Datu-base errorean
	 */
	int zenbatWorkoutMaila(int maila) throws DAOException;

	/**
	 * Identifikatzaile bidez Workout oso bat jaso.
	 * 
	 * Workout-ak bere ariketak eta serieak ditu.
	 * 
	 * @param workoutId Workout identifikatzailea
	 * @return Workout instantzia
	 * @throws DAOException Datu-base errorean
	 */
	public Workout lortuWorkoutOsoa(String workoutId) throws DAOException;
}
