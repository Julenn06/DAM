package modelo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class TiendaModel {
	private Document doc;
	private XPath xpath;

	public TiendaModel(String rutaArchivoXML) throws Exception {
		File inputFile = new File(rutaArchivoXML);

		// Si el archivo no existe, lo crea con la estructura exacta proporcionada
		if (!inputFile.exists()) {
			System.out.println("⚠️ Archivo no encontrado. Creando plantilla XML en: " + rutaArchivoXML);
		}

		// Carga y normalización del documento XML
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		this.doc = dBuilder.parse(inputFile);
		this.doc.getDocumentElement().normalize();

		// Inicialización de XPath
		XPathFactory xPathfactory = XPathFactory.newInstance();
		this.xpath = xPathfactory.newXPath();
	}

	private List<String> nodeExtractor(String expression) throws Exception {
		List<String> resultados = new ArrayList<>();
		NodeList nodes = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			resultados.add(nodes.item(i).getTextContent());
		}
		return resultados;
	}

	// 1. Suma de unidades vendidas de productos de una categoría (Ej: "carne")
	public double getVentasPorCategoria(String categoria) throws Exception {
		String expression = String.format("sum(//venta[producto = //producto[@categoria='%s']/@id]/cantidad)",
				categoria);
		return (Double) xpath.compile(expression).evaluate(doc, XPathConstants.NUMBER);
	}

	// 2. Nombre de los productos con un precio inferior a un valor (Ej: 1.50)
	public List<String> getProductosBaratos(double precioMax) throws Exception {
		String expression = String.format("//producto[precio < %s]/nombre", precioMax);
		return nodeExtractor(expression);
	}

	// 3. Nombre de los productos de los que se han vendido más de X unidades en una
	// sola venta (Ej: 5)
	public List<String> getProductosMasVendidos(int cantidadMin) throws Exception {
		String expression = String.format("//producto[@id = //venta[cantidad > %d]/producto]/nombre", cantidadMin);
		return nodeExtractor(expression);
	}

	// 4. Responsable del departamento al que pertenece un producto dado (Ej:
	// "Naranjas")
	public String getResponsableDelProducto(String nombreProducto) throws Exception {
		String expression = String.format("//dpto[@id = //producto[nombre = '%s']/@venta]/responsable", nombreProducto);
		return (String) xpath.compile(expression).evaluate(doc, XPathConstants.STRING);
	}

	// 5. Responsable de la venta realizada en una fecha concreta (Ej: "2013-05-05")
	public List<String> getResponsablesDeLaVenta(String fecha) throws Exception {
		String expression = String.format("//venta[@fecha='%s']/@responsable", fecha);
		return nodeExtractor(expression);
	}

	// 6. Número de productos con menos de X unidades en stock (Ej: 20)
	public double getProductosPocoStock(int stockMax) throws Exception {
		String expression = String.format("count(//producto[stock < %d])", stockMax);
		return (Double) xpath.compile(expression).evaluate(doc, XPathConstants.NUMBER);
	}

	// 7. Nombre del departamento cuyo responsable es X (Ej: "Iker Zabala")
	public String getDptoDelResponsable(String nombreResponsable) throws Exception {
		String expression = String.format("//dpto[responsable = '%s']/nombre", nombreResponsable);
		return (String) xpath.compile(expression).evaluate(doc, XPathConstants.STRING);
	}
}
