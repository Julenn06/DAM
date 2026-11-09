package dao.interfazeak;

import java.util.concurrent.ExecutionException;

import model.DAOException;
import model.Erabiltzailea;

public interface IErabiltzaileDAO extends IDAO<Erabiltzailea> {

	/**
	 * Helbide elektroniko eta pasahitzarekin saioa hasten du.
	 * 
	 * @param email     Erabiltzailearen e-mail helbidea
	 * @param pasahitza Erabiltzailearen pasahitza testu lauan
	 * @return Erabiltzailea instantzia zuzena bada, null okerra bada
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	Erabiltzailea saioaHasi(String email, String pasahitza) throws DAOException;

	/**
	 * Erabiltzaile berri bat erregistratzen du.
	 * 
	 * @param erab Erabiltzaile instantzia
	 * @return Erabiltzailea arrakastaz sortu den ala ez.
	 * @throws DAOException Datu-baseko errore bat gertatzean
	 */
	boolean registratu(Erabiltzailea erab) throws DAOException;

	/**
	 * Erabiltzailearen profila eguneratzen du (datu pertsonalak eta pasahitza),
	 * pasahitz zaharra egiaztatuz.
	 * 
	 * @param erab             Datu berriak dituen erabiltzaile instantzia
	 * @param pasahitzaZaharra Erabiltzailearen uneko pasahitza egiaztapenerako
	 * @throws DAOException Erabiltzailea aurkitzen ez bada, pasahitza okerra bada
	 *                      edo datu-baseko/hari-errore bat gertatzen denean
	 */
	void profilaEguneratu(Erabiltzailea erab, String pasahitzaZaharra) throws DAOException;

	/**
	 * Erabiltzailearen izena eta abizenak eguneratzen ditu.
	 * 
	 * @param erab Izen eta abizen berriak dituen erabiltzaile instantzia
	 * @throws DAOException Erabiltzailea aurkitzen ez bada edo
	 *                      datu-baseko/hari-errore bat gertatzen denean
	 */
	void izenaEguneratu(Erabiltzailea erab) throws DAOException;

	/**
	 * Erabiltzaile baten maila eguneratzen du.
	 * 
	 * @param erab        Erabiltzaile instantzia
	 * @param mailaBerria Ezarri beharreko maila berria
	 * @return Maila arrakastaz eguneratu den ala ez
	 * @throws DAOException Datu-baseko/hari-errore bat gertatzen denean
	 */

	boolean mailaEguneratu(Erabiltzailea erab, int mailaBerria) throws DAOException;
}
