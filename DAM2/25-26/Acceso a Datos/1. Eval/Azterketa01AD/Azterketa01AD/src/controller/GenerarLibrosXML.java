package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import model.Libros;

public class GenerarLibrosXML {

	private final Firestore db;
	private File file = new File("BookStore.xml");

	public GenerarLibrosXML() {
		this.db = DBConnection.getFirestore();
	}

	public void saveToXML() throws InterruptedException, ExecutionException, ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerException {

		List<Libros> liburuak = new ArrayList<>();

		QuerySnapshot querySnapshot = db.collection("liburuak").get().get();

		for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {

			String name = doc.getString("title");
			Double price = doc.getDouble("price");
			String isbn = doc.getString("isbn");
			String category = doc.getString("category");
			String lang = doc.getString("lang");
			String author = doc.getString("author");
			
			
			
			

			Libros liburua = new Libros(isbn, category, name, lang, author, price);
			liburuak.add(liburua);
		}

		DOMImplementation impl = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
		Document docXML = impl.createDocument(null, "Liburuak", null);
		Element root = docXML.getDocumentElement();

		for (Libros l : liburuak) {
			Element datuakElement = docXML.createElement("Liburua");

			Element nameElement = docXML.createElement("title");
			nameElement.appendChild(docXML.createTextNode(l.getTitle()));
			datuakElement.appendChild(nameElement);

			Element isbnElement = docXML.createElement("isbn");
			isbnElement.appendChild(docXML.createTextNode(String.valueOf(l.getIsbn())));
			datuakElement.appendChild(isbnElement);

			Element categoryElement = docXML.createElement("category");
			categoryElement.appendChild(docXML.createTextNode(String.valueOf(l.getCategory())));
			datuakElement.appendChild(categoryElement);

			Element langElement = docXML.createElement("lang");
			langElement.appendChild(docXML.createTextNode(String.valueOf(l.getLang())));
			datuakElement.appendChild(langElement);

			Element authorElement = docXML.createElement("author");
			authorElement.appendChild(docXML.createTextNode(String.valueOf(l.getAuthor())));
			datuakElement.appendChild(authorElement);

			Element yearElement = docXML.createElement("year");
			yearElement.appendChild(docXML.createTextNode(String.valueOf(l.getYear())));
			datuakElement.appendChild(yearElement);

			Element priceElement = docXML.createElement("price");
			priceElement.appendChild(docXML.createTextNode(String.valueOf(l.getPrice())));
			datuakElement.appendChild(priceElement);

			Element dateElement = docXML.createElement("date");
			dateElement.appendChild(docXML.createTextNode(String.valueOf(l.getDate())));
			datuakElement.appendChild(dateElement);

			root.appendChild(datuakElement);
		}

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(javax.xml.transform.OutputKeys.STANDALONE, "no");

		DOMSource source = new DOMSource(docXML);
		StreamResult result = new StreamResult(file);
		transformer.transform(source, result);

	}
}