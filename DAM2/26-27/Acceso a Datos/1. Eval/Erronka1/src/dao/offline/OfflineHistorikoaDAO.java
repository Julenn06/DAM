package dao.offline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dao.interfazeak.IHistorikoaDAO;
import model.DAOException;
import model.HistorikoaItem;

public class OfflineHistorikoaDAO implements IHistorikoaDAO {

	private File fitxategia = new File("historikoa.xml");

	public OfflineHistorikoaDAO() {
		super();
	}

	@Override
	public void denakIdatzi(List<HistorikoaItem> datuak) throws DAOException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document docXml = builder.newDocument();

			Element erroa = docXml.createElement("historikoak");
			docXml.appendChild(erroa);

			for (HistorikoaItem item : datuak) {
				item.toXML(erroa);
			}

			try (FileOutputStream fos = new FileOutputStream(fitxategia, false)) {
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();

				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://apache.org}indent-amount", "4");

				DOMSource source = new DOMSource(docXml);
				StreamResult result = new StreamResult(fos);

				transformer.transform(source, result);
			}

		} catch (IOException e) {
			throw new DAOException("Ezin izan da " + fitxategia + " idatzi", e);
		} catch (Exception e) {
			throw new DAOException("Errorea XML dokumentua sortzean: " + e.getMessage(), e);
		}
	}

	@Override
	public List<HistorikoaItem> denakJaso() throws DAOException {
		List<HistorikoaItem> historikoak = new ArrayList<>();

		if (!fitxategia.exists()) {
			return historikoak;
		}

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(fitxategia);
			doc.getDocumentElement().normalize();

			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xpath = xPathFactory.newXPath();

			NodeList nList = (NodeList) xpath.evaluate("//entrenamendua", doc, XPathConstants.NODESET);

			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					HistorikoaItem item = HistorikoaItem.fromXML((Element) node, xpath);
					if (item != null) {
						historikoak.add(item);
					}
				}
			}
		} catch (Exception e) {
			throw new DAOException("Ezin izan da " + fitxategia + " XML fitxategia irakurri", e);
		}
		return historikoak;
	}

	@Override
	public void gordeHistorikoa(HistorikoaItem item) throws DAOException {
		List<HistorikoaItem> guztiak = denakJaso();
		guztiak.add(item);
		denakIdatzi(guztiak);
	}

	@Override
	public int zenbatWorkoutAmaituta(String email, int maila) throws DAOException {
		if (email == null) {
			return 0;
		}

		List<HistorikoaItem> historikoak = denakJaso();
		int kontagailua = 0;

		for (HistorikoaItem item : historikoak) {
			String itemEmail = item.getEmailErabiltzaile();
			if (email.equalsIgnoreCase(itemEmail)) {
				if (item.getWorkout() != null && item.getMaila() == maila) {
					kontagailua++;
				}
			}
		}

		return kontagailua;
	}

	@Override
	public List<HistorikoaItem> lortuErabiltzailearenHistorikoak(String email) throws DAOException {
		return denakJaso().stream()
				.filter(item -> item.getWorkout() != null && email.equalsIgnoreCase(item.getEmailErabiltzaile()))
				.collect(Collectors.toCollection(ArrayList::new));
	}
}
