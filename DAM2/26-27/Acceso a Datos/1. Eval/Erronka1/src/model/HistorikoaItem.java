package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.cloud.firestore.QueryDocumentSnapshot;

import utils.TimeFormatter;

public class HistorikoaItem {
	public static final String DATA_FORMATUA = "yyyy-MM-dd'T'HH:mm:ss";

	String emailErabiltzaile;

	String workout;
	int maila;
	Date data;
	int denboraGuztira;
	int aurreikusitakoDenbora;
	int osatua;

	public HistorikoaItem(String workout, int maila, Date data, int denboraGuztira, int aurreikusitakoDenbora,
			int osatua, String emailErabiltzaile) {
		super();
		this.emailErabiltzaile = emailErabiltzaile;
		this.workout = workout;
		this.maila = maila;
		this.data = data;
		this.denboraGuztira = denboraGuztira;
		this.aurreikusitakoDenbora = aurreikusitakoDenbora;
		this.osatua = osatua;
	}

	public HistorikoaItem() {
		super();
	}

	public static HistorikoaItem fromXML(Element dis) {
		if (dis == null) {
			return null;
		}

		String email = dis.getAttribute("id");
		if (email.isEmpty()) {
			return null;
		}

		String workout = lortuTestua(dis, 0);
		int maila = parseInt(lortuTestua(dis, 1));
		Date data = parseData(lortuTestua(dis, 2));
		int denboraGuztira = parseDenbora(lortuTestua(dis, 3));
		int aurreikusitakoDenbora = parseDenbora(lortuTestua(dis, 4));
		int osatua = parseInt(lortuTestua(dis, 5));

		return new HistorikoaItem(workout, maila, data, denboraGuztira, aurreikusitakoDenbora, osatua, email);
	}

	public static HistorikoaItem fromXML(Element dis, XPath xpath) {
		if (dis == null) {
			return null;
		}

		String email = dis.getAttribute("id");
		if (email.isEmpty()) {
			return null;
		}

		try {
			String workout = lortuTestuaXPath(xpath, "workout", dis);
			int maila = parseInt(lortuTestuaXPath(xpath, "maila", dis));
			Date data = parseData(lortuTestuaXPath(xpath, "data", dis));
			int denboraGuztira = parseDenbora(lortuTestuaXPath(xpath, "denboraGuztira", dis));
			int aurreikusitakoDenbora = parseDenbora(lortuTestuaXPath(xpath, "aurreikusitakoDenbora", dis));
			int osatua = parseInt(lortuTestuaXPath(xpath, "osatua", dis));

			return new HistorikoaItem(workout, maila, data, denboraGuztira, aurreikusitakoDenbora, osatua, email);
		} catch (XPathExpressionException e) {
			return null;
		}
	}

	public static HistorikoaItem fromDoc(QueryDocumentSnapshot doc, String email) {
		String workout = doc.getString("workout");
		int maila = intBalioa(doc.get("maila"));
		Date data = doc.getDate("data");
		int denboraGuztira = denboraBalioa(doc.get("denboraGuztira"));
		int aurreikusitakoDenbora = denboraBalioa(doc.get("aurreikusitakoDenbora"));
		int osatua = intBalioa(doc.get("osatua"));

		return new HistorikoaItem(workout, maila, data, denboraGuztira, aurreikusitakoDenbora, osatua, email);
	}

	private static String formatDenbora(int segundu) {
		return TimeFormatter.segunduak(segundu);
	}

	private static String formatData(Date data) {
		if (data == null) {
			return "";
		}
		return new SimpleDateFormat(DATA_FORMATUA).format(data);
	}

	private static void addChild(Element parent, String izena, String balioa) {
		Element child = parent.getOwnerDocument().createElement(izena);
		child.setTextContent(balioa);
		parent.appendChild(child);
	}

	private static String lortuTestua(Element parent, int posizioa) {
		int kontadorea = 0;
		NodeList semeak = parent.getChildNodes();
		for (int i = 0; i < semeak.getLength(); i++) {
			Node seme = semeak.item(i);
			if (seme.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			if (kontadorea == posizioa) {
				String testua = seme.getTextContent();
				return testua == null ? "" : testua.trim();
			}
			kontadorea++;
		}
		return "";
	}

	private static String lortuTestuaXPath(XPath xpath, String tagName, Element context)
			throws XPathExpressionException {
		String testua = (String) xpath.evaluate(tagName, context, XPathConstants.STRING);
		return testua == null ? "" : testua.trim();
	}

	private static int parseInt(String testua) {
		if (testua == null || testua.isEmpty()) {
			return 0;
		}
		try {
			return Integer.parseInt(testua);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private static int parseDenbora(String testua) {
		if (testua == null || testua.isEmpty()) {
			return 0;
		}
		int posizioa = testua.indexOf(':');
		if (posizioa < 0) {
			return parseInt(testua);
		}
		int minutuak = parseInt(testua.substring(0, posizioa));
		int segunduak = parseInt(testua.substring(posizioa + 1));
		return minutuak * 60 + segunduak;
	}

	private static Date parseData(String testua) {
		if (testua == null || testua.isEmpty()) {
			return null;
		}
		try {
			return new SimpleDateFormat(DATA_FORMATUA).parse(testua);
		} catch (ParseException e) {
			return null;
		}
	}

	private static int intBalioa(Object balioa) {
		if (balioa instanceof Number) {
			return ((Number) balioa).intValue();
		}
		return parseInt(balioa == null ? "" : balioa.toString().trim());
	}

	private static int denboraBalioa(Object balioa) {
		if (balioa instanceof Number) {
			return ((Number) balioa).intValue();
		}
		return parseDenbora(balioa == null ? "" : balioa.toString().trim());
	}

	public void toXML(Element parent) {
		Element entrenamendua = parent.getOwnerDocument().createElement("entrenamendua");

		entrenamendua.setAttribute("id", emailErabiltzaile);

		parent.appendChild(entrenamendua);

		addChild(entrenamendua, "workout", getWorkout());
		addChild(entrenamendua, "maila", String.valueOf(getMaila()));
		addChild(entrenamendua, "data", formatData(getData()));
		addChild(entrenamendua, "denboraGuztira", formatDenbora(getDenboraGuztira()));
		addChild(entrenamendua, "aurreikusitakoDenbora", formatDenbora(getAurreikusitakoDenbora()));
		addChild(entrenamendua, "osatua", String.valueOf(getOsatua()));
	}

	public Map<String, Object> toDoc() {
		Map<String, Object> datuak = new HashMap<>();
		datuak.put("workout", getWorkout());
		datuak.put("maila", getMaila());
		datuak.put("data", getData());
		datuak.put("denboraGuztira", formatDenbora(getDenboraGuztira()));
		datuak.put("aurreikusitakoDenbora", formatDenbora(getAurreikusitakoDenbora()));
		datuak.put("osatua", getOsatua());
		return datuak;
	}

	public String getWorkout() {
		return workout;
	}

	public void setWorkout(String workout) {
		this.workout = workout;
	}

	public int getMaila() {
		return maila;
	}

	public void setMaila(int maila) {
		this.maila = maila;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getDenboraGuztira() {
		return denboraGuztira;
	}

	public void setDenboraGuztira(int denboraGuztira) {
		this.denboraGuztira = denboraGuztira;
	}

	public int getAurreikusitakoDenbora() {
		return aurreikusitakoDenbora;
	}

	public void setAurreikusitakoDenbora(int aurreikusitakoDenbora) {
		this.aurreikusitakoDenbora = aurreikusitakoDenbora;
	}

	public int getOsatua() {
		return osatua;
	}

	public void setOsatua(int osatua) {
		this.osatua = osatua;
	}

	public String getEmailErabiltzaile() {
		return emailErabiltzaile;
	}

	public void setEmailErabiltzaile(String emailErabiltzaile) {
		this.emailErabiltzaile = emailErabiltzaile;
	}
}
