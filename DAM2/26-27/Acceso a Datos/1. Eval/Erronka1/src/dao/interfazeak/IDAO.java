package dao.interfazeak;

import java.util.List;

import model.DAOException;

/**
 * Objektu generikoen sarbide-geruzako (DAO) interfaze orokorra. Fitxategi edo
 * datu base batean gordetzen diren erregistroen irakurketa eta idazketa
 * eragiketak definitzen ditu.
 *
 * <p>
 * <strong>Erabilera adibidea:</strong>
 * </p>
 * 
 * <pre>{@code
 * IErabiltzaileDAO dao = new OfflineErabiltzaileaDAO();
 *
 * // Datu guztiak irakurri
 * List<Erabiltzailea> erabiltzaileak = dao.denakJaso();
 *
 * // Datuak fitxategian gorde
 * dao.denakIdatzi(erabiltzaileak);
 * }</pre>
 *
 * @param <T> erabiltzen den modeloko mota (adibidez, {@code Erabiltzailea})
 */
public interface IDAO<T> {
	/**
	 * Gordetako erregistro guztiak itzultzen ditu.
	 *
	 * @return erregistroen zerrenda; hutsik badago, zerrenda hutsa
	 * @throws DAOException irakurketa edo formatu akats bat gertatzen bada
	 */
	public List<T> denakJaso() throws DAOException;

	/**
	 * Emandako erregistro guztiak fitxategian idazten ditu (aurreko edukia
	 * ordezkatuz).
	 *
	 * @param datuak idazko diren erregistroen zerrenda
	 * @throws DAOException idazketa akats bat gertatzen bada
	 */
	public void denakIdatzi(List<T> datuak) throws DAOException;
}
