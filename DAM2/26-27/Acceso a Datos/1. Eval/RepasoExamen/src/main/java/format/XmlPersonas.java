package format;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import model.Persona;

public final class XmlPersonas {
	private XmlPersonas() {
	}

	public static void escribir(List<Persona> personas, File ruta) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element root = doc.createElement("personas");
		doc.appendChild(root);
		for (Persona p : personas) {
			Element persona = doc.createElement("persona");
			persona.setAttribute("id", p.getId());
			root.appendChild(persona);
			campo(doc, persona, "nombre", p.getNombre());
			campo(doc, persona, "fechaNacimiento", p.getFechaNacimiento().toString());
		}
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(doc), new StreamResult(ruta));
	}

	public static List<Persona> leer(File ruta) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ruta);
		NodeList nodos = (NodeList) XPathFactory.newInstance().newXPath().compile("/personas/persona").evaluate(doc,
				XPathConstants.NODESET);
		List<Persona> personas = new ArrayList<>();
		for (int i = 0; i < nodos.getLength(); i++) {
			Element e = (Element) nodos.item(i);
			personas.add(new Persona(e.getAttribute("id"), texto(e, "nombre"),
					LocalDate.parse(texto(e, "fechaNacimiento"))));
		}
		return personas;
	}

	public static String nombrePorId(File ruta, String id) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ruta);
		String expression = "/personas/persona[@id='" + id + "']/nombre";
		return (String) XPathFactory.newInstance().newXPath().compile(expression).evaluate(doc, XPathConstants.STRING);
	}

	public static void escribirAtributos(List<Persona> personas, File ruta) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element root = doc.createElement("personas");
		doc.appendChild(root);
		for (Persona p : personas) {
			Element persona = doc.createElement("persona");
			persona.setAttribute("id", p.getId());
			persona.setAttribute("nombre", p.getNombre());
			persona.setAttribute("fechaNacimiento", p.getFechaNacimiento().toString());
			root.appendChild(persona);
		}
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(doc), new StreamResult(ruta));
	}

	public static List<Persona> leerAtributos(File ruta) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ruta);
		NodeList nodos = (NodeList) XPathFactory.newInstance().newXPath().compile("/personas/persona")
				.evaluate(doc, XPathConstants.NODESET);
		List<Persona> personas = new ArrayList<>();
		for (int i = 0; i < nodos.getLength(); i++) {
			Element persona = (Element) nodos.item(i);
			personas.add(new Persona(persona.getAttribute("id"), persona.getAttribute("nombre"),
					LocalDate.parse(persona.getAttribute("fechaNacimiento"))));
		}
		return personas;
	}

	private static void campo(Document doc, Element padre, String nombre, String valor) {
		Element campo = doc.createElement(nombre);
		campo.setTextContent(valor);
		padre.appendChild(campo);
	}

	private static String texto(Element padre, String nombre) {
		return padre.getElementsByTagName(nombre).item(0).getTextContent();
	}
}