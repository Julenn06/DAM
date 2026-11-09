package dao.offline;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import dao.interfazeak.IErabiltzaileDAO;
import model.DAOException;
import model.Erabiltzailea;
import utils.Hash;
import utils.SegurtasunTresna;

public class OfflineErabiltzaileaDAO implements IErabiltzaileDAO {
	private File fitxategia = new File("erabiltzaileak.dat");

	public OfflineErabiltzaileaDAO() {
		super();
	}

	@Override
	public List<Erabiltzailea> denakJaso() throws DAOException {
		List<Erabiltzailea> erabiltzaileak = new ArrayList<>();

		if (!fitxategia.exists()) {
			return erabiltzaileak;
		}

		try (FileInputStream fis = new FileInputStream(fitxategia);
				CipherInputStream cis = new CipherInputStream(fis, SegurtasunTresna.lortuDezifratzailea());
				DataInputStream dis = new DataInputStream(cis)) {

			while (true) {
				try {
					Erabiltzailea erab = Erabiltzailea.fromDis(dis);
					dis.readChar(); // Consumir el '\n'
					erabiltzaileak.add(erab);
				} catch (EOFException eof) {
					break;
				}
			}

		} catch (Exception e) {
			throw new DAOException("Ezin izan da " + fitxategia + " modu seguruan irakurri", e);
		}

		return erabiltzaileak;
	}

	@Override
	public void denakIdatzi(List<Erabiltzailea> datuak) throws DAOException {
		try (FileOutputStream fos = new FileOutputStream(fitxategia, false);
				CipherOutputStream cos = new CipherOutputStream(fos, SegurtasunTresna.lortuZifratzailea());
				DataOutputStream dos = new DataOutputStream(cos)) {

			for (Erabiltzailea erabiltzailea : datuak) {
				erabiltzailea.toDos(dos);
				dos.writeChar('\n');
			}
			dos.flush();
		} catch (Exception e) {
			throw new DAOException("Ezin izan da " + fitxategia + " modu seguruan idatzi", e);
		}
	}

	@Override
	public boolean registratu(Erabiltzailea erab) throws DAOException {
		List<Erabiltzailea> guztiak = denakJaso();
		String hashedPassword = Hash.hasheatu(erab.getPasahitza().toCharArray());
		erab.setPasahitza(hashedPassword);
		guztiak.add(erab);
		denakIdatzi(guztiak);
		return true;
	}

	@Override
	public Erabiltzailea saioaHasi(String email, String pasahitza) throws DAOException {
		if (email == null || pasahitza == null) {
			return null;
		}

		String emailLodia = email.trim().toLowerCase();
		List<Erabiltzailea> listaLocal = denakJaso();

		for (Erabiltzailea erab : listaLocal) {
			String emailGuardado = erab.getEmail() != null ? erab.getEmail().trim().toLowerCase() : "";

			if (emailGuardado.equals(emailLodia)) {
				boolean zuzena = Hash.konprobatu(pasahitza.toCharArray(), erab.getPasahitza());

				if (zuzena) {
					return erab;
				}
			}
		}

		return null;
	}

	@Override
	public void profilaEguneratu(Erabiltzailea erab, String pasahitzaZaharra) throws DAOException {
		if (erab == null || erab.getEmail() == null) {
			throw new DAOException("Ezin da profila eguneratu: Erabiltzaile datu okerrak.");
		}

		List<Erabiltzailea> guztiak = denakJaso();
		boolean aurkitua = false;

		for (int i = 0; i < guztiak.size(); i++) {
			Erabiltzailea u = guztiak.get(i);
			if (u.getEmail().equalsIgnoreCase(erab.getEmail().trim())) {
				boolean zuzena = Hash.konprobatu(pasahitzaZaharra.toCharArray(), u.getPasahitza());
				if (!zuzena) {
					throw new DAOException("Pasahitz okerra");
				}
				String nirePasahitzaBerriaHash = Hash.hasheatu(erab.getPasahitza().toCharArray());

				erab.setPasahitza(nirePasahitzaBerriaHash);
				guztiak.set(i, erab);
				aurkitua = true;
				break;
			}
		}

		if (!aurkitua) {
			throw new DAOException("Ez da aurkitu erabiltzailerik email horrekin");
		}

		denakIdatzi(guztiak);
	}

	@Override
	public boolean mailaEguneratu(Erabiltzailea erab, int mailaBerria) throws DAOException {
		if (erab == null || erab.getEmail() == null) {
			return false;
		}

		List<Erabiltzailea> guztiak = denakJaso();
		boolean aurkitua = false;

		for (int i = 0; i < guztiak.size(); i++) {
			Erabiltzailea u = guztiak.get(i);
			if (u.getEmail().equalsIgnoreCase(erab.getEmail().trim())) {
				u.setMaila(mailaBerria);
				guztiak.set(i, u);
				aurkitua = true;
				break;
			}
		}

		if (aurkitua) {
			denakIdatzi(guztiak);
			return true;
		}

		return false;
	}

	@Override
	public void izenaEguneratu(Erabiltzailea erab) throws DAOException {
		if (erab == null || erab.getEmail() == null) {
			return;
		}

		List<Erabiltzailea> guztiak = denakJaso();

		for (int i = 0; i < guztiak.size(); i++) {
			Erabiltzailea u = guztiak.get(i);
			if (u.getEmail().equalsIgnoreCase(erab.getEmail().trim())) {
				u.setIzena(erab.getIzena());
				u.setAbizena1(erab.getAbizena1());
				u.setAbizena2(erab.getAbizena2());
				guztiak.set(i, u);
				break;
			}
		}

		denakIdatzi(guztiak);
	}
}
