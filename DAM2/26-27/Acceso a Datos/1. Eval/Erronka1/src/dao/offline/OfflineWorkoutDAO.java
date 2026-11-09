package dao.offline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import com.google.gson.Gson;

import dao.interfazeak.IWorkoutDAO;
import model.DAOException;
import model.Gimnasio;
import model.Workout;
import utils.SegurtasunTresna;

public class OfflineWorkoutDAO implements IWorkoutDAO {
	private File workoutakFile = new File("workoutak.dat");
	private File workoutakFileJSON = new File("gimnasio.json");

	@Override
	public List<Workout> irakurriWorkoutMailarekin(int maila) throws DAOException {
		List<Workout> saioak = denakJaso();
		return saioak.stream().filter(saioa -> saioa.getMaila() == maila).toList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Workout> denakJaso() throws DAOException {
		// FileInputStream CipherInputStream-ekin objektua seguru des-serializatzeko
		try (FileInputStream fis = new FileInputStream(workoutakFile);
				CipherInputStream cis = new CipherInputStream(fis, SegurtasunTresna.lortuDezifratzailea());
				ObjectInputStream ois = new ObjectInputStream(cis)) {
			return (List<Workout>) ois.readObject();
		} catch (Exception e) {
			throw new DAOException("Ezin izan da saioen babes kopia segurua jaso", e);
		}
	}

	@Override
	public void denakIdatzi(List<Workout> datuak) throws DAOException {
		// FileOutputStream CipherOutputStream-ekin objektua seguru zifratzeko
		Gson gson = new Gson();
		try (FileOutputStream fos = new FileOutputStream(workoutakFile);
				CipherOutputStream cos = new CipherOutputStream(fos, SegurtasunTresna.lortuZifratzailea());
				ObjectOutputStream oos = new ObjectOutputStream(cos);
				FileWriter fw = new FileWriter(workoutakFileJSON);) {
			oos.writeObject(datuak);
			Gimnasio gym = new Gimnasio(datuak);
			gson.toJson(gym, fw);
		} catch (Exception e) {
			throw new DAOException("Ezin izan da saioen babes kopia segurua egin", e);
		}
	}

	@Override
	public String jasoWorkoutID(Workout saioa) throws DAOException {
		if (saioa == null) {
			return null;
		}
		return saioa.getIzena();
	}

	@Override
	public int zenbatWorkoutMaila(int maila) throws DAOException {
		List<Workout> saioak = irakurriWorkoutMailarekin(maila);
		return saioak.size();
	}

	@Override
	public Workout lortuWorkoutOsoa(String workoutId) throws DAOException {
		List<Workout> saioak = denakJaso();
		for (Workout saioa : saioak) {
			if (saioa.getIzena() != null && saioa.getIzena().equals(workoutId)) {
				return saioa;
			}
		}
		return null;
	}
}
