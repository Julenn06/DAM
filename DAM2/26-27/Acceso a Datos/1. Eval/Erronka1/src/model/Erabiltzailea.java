package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.QueryDocumentSnapshot;

public class Erabiltzailea implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String izena;
	private String abizena1;
	private String abizena2;
	private String email;
	private Date jaiotzeData;
	private int maila;
	private String pasahitza;
	private List<HistorikoaItem> historikoa;

	public Erabiltzailea() {
		super();
	}

	public Erabiltzailea(String izena, String abizena1, String abizena2, String email, Date jaiotzeData, int maila) {
		super();
		this.izena = izena;
		this.abizena1 = abizena1;
		this.abizena2 = abizena2;
		this.email = email;
		this.jaiotzeData = jaiotzeData;
		this.maila = maila;
	}

	public Erabiltzailea(String izena, String abizena1, String abizena2, String email, Date jaiotzeData, int maila,
			String pasahitza) {
		super();
		this.izena = izena;
		this.abizena1 = abizena1;
		this.abizena2 = abizena2;
		this.email = email;
		this.jaiotzeData = jaiotzeData;
		this.maila = maila;
		this.pasahitza = pasahitza;
	}
	
	public Erabiltzailea(String izena, String abizena1, String abizena2, String email) {
		super();
		this.izena = izena;
		this.abizena1 = abizena1;
		this.abizena2 = abizena2;
		this.email = email;
	}

	public Erabiltzailea(String izena, String abizena1, String abizena2, String email, Date jaiotzeData, int maila,
			List<HistorikoaItem> historikoa) {
		super();
		this.izena = izena;
		this.abizena1 = abizena1;
		this.abizena2 = abizena2;
		this.email = email;
		this.jaiotzeData = jaiotzeData;
		this.maila = maila;
		this.historikoa = historikoa;
	}

	public Erabiltzailea(String izena2, String abizena12, String abizena22, String email2, Date utilDate,
			String pasahitzaBerria) {
		super();
		this.izena = izena2;
		this.abizena1 = abizena12;
		this.abizena2 = abizena22;
		this.email = email2;
		this.jaiotzeData = utilDate;
		this.pasahitza = pasahitzaBerria;
	}

	public static Erabiltzailea fromDoc(QueryDocumentSnapshot doc) {
		String izena = doc.getString("izena");
		String abizena1 = doc.getString("abizena1");
		String abizena2 = doc.getString("abizena2");
		String email = doc.getString("email");
		Date jaiotzeData = doc.getDate("jaiotzeData");
		Long mailaLong = doc.getLong("maila");
		int maila = mailaLong != null ? mailaLong.intValue() : 0;
		String pasahitza = doc.getString("pasahitza");

		return new Erabiltzailea(izena, abizena1, abizena2, email, jaiotzeData, maila, pasahitza);
	}

	public static Erabiltzailea fromDocHistorikoarekin(QueryDocumentSnapshot doc)
			throws InterruptedException, ExecutionException {
		String izena = doc.getString("izena");
		String abizena1 = doc.getString("abizena1");
		String abizena2 = doc.getString("abizena2");
		String email = doc.getString("email");
		Date jaiotzeData = doc.getDate("jaiotzeData");
		Long mailaLong = doc.getLong("maila");
		int maila = mailaLong != null ? mailaLong.intValue() : 0;
		String pasahitza = doc.getString("pasahitza");

		List<HistorikoaItem> historikoa = new ArrayList<>();
		List<QueryDocumentSnapshot> historikoaDocs = doc.getReference().collection("historikoa").get().get()
				.getDocuments();

		for (QueryDocumentSnapshot historikoaDoc : historikoaDocs) {
			historikoa.add(HistorikoaItem.fromDoc(historikoaDoc, email));
		}

		Erabiltzailea erabiltzailea = new Erabiltzailea(izena, abizena1, abizena2, email, jaiotzeData, maila,
				pasahitza);
		erabiltzailea.setHistorikoa(historikoa);
		return erabiltzailea;
	}

	public static Erabiltzailea fromDis(DataInputStream dis) throws IOException {
		String izena = dis.readUTF();
		String abizena1 = dis.readUTF();
		String abizena2 = dis.readUTF();
		String email = dis.readUTF();
		Date jaiotzeData = new Date(dis.readLong());
		int maila = dis.readInt();
		String pasahitza = dis.readUTF();
		return new Erabiltzailea(izena, abizena1, abizena2, email, jaiotzeData, maila, pasahitza);
	}

	public Map<String, Object> toDoc() {
		Map<String, Object> erabiltzaileData = new HashMap<>();
		erabiltzaileData.put("izena", this.getIzena());
		erabiltzaileData.put("abizena1", this.getAbizena1());
		erabiltzaileData.put("abizena2", this.getAbizena2());
		erabiltzaileData.put("email", this.getEmail());
		erabiltzaileData.put("pasahitza", this.getPasahitza());
		erabiltzaileData.put("jaiotzeData", this.getJaiotzeData());
		return erabiltzaileData;
	}

	public void toDos(DataOutputStream dos) throws IOException {
		dos.writeUTF(izena);
		dos.writeUTF(abizena1);
		dos.writeUTF(abizena2);
		dos.writeUTF(email);
		dos.writeLong(jaiotzeData.getTime());
		dos.writeInt(maila);
		dos.writeUTF(pasahitza);
	}

	public String getIzena() {
		return izena;
	}

	public String getAbizena1() {
		return abizena1;
	}

	public String getAbizena2() {
		return abizena2;
	}

	public String getEmail() {
		return email;
	}

	public Date getJaiotzeData() {
		return jaiotzeData;
	}

	public int getMaila() {
		return maila;
	}

	public String getPasahitza() {
		return pasahitza;
	}

	public List<HistorikoaItem> getHistorikoa() {
		return historikoa;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public void setAbizena1(String abizena1) {
		this.abizena1 = abizena1;
	}

	public void setAbizena2(String abizena2) {
		this.abizena2 = abizena2;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setJaiotzeData(Date jaiotzeData) {
		this.jaiotzeData = jaiotzeData;
	}

	public void setMaila(int maila) {
		this.maila = maila;
	}

	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}

	public void setHistorikoa(List<HistorikoaItem> historikoa) {
		this.historikoa = historikoa;
	}

	@Override
	public String toString() {
		return "Erabiltzailea [izena=" + izena + ", abizena1=" + abizena1 + ", abizena2=" + abizena2 + ", email="
				+ email + ", jaiotzeData=" + jaiotzeData + ", maila=" + maila + "]";
	}

}
