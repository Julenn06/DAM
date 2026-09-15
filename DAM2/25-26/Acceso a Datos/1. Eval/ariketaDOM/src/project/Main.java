package project;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
	public static void main(String[] args) {
		try {
			File inputFile = new File("cd_catalog.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			NodeList cdList = doc.getElementsByTagName("cd");

			for (int i = 0; i < cdList.getLength(); i++) {
				Node cd = cdList.item(i);
				if (cd.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) cd;
					System.out.println("CD: " + (i + 1));
					System.out.println("Titulo: " + eElement.getElementsByTagName("title").item(0).getTextContent());
					System.out.println("Artista: " + eElement.getElementsByTagName("artist").item(0).getTextContent());
					System.out.println("País: " + eElement.getElementsByTagName("country").item(0).getTextContent());
					System.out.println("Sello: " + eElement.getElementsByTagName("company").item(0).getTextContent());
					System.out.println("Precio: " + eElement.getElementsByTagName("price").item(0).getTextContent());
					System.out.println("Año: " + eElement.getElementsByTagName("year").item(0).getTextContent());
					System.out.println();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
