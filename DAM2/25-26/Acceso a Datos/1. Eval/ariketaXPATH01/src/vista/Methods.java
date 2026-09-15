package vista;

import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Methods {

	public static int menu(Scanner sc) {
		int op = 0;
		while (true) {
			System.out.println("Elige una opción:");
			System.out.println("1. Cantidad de ventas de la carnicería");
			System.out.println("2. Precio de productos de carnicería");
			System.out.println("3. Nombre del producto del que se han vendido 3 unidades");
			System.out.println("4. Responsable del producto con nombre de Naranjas");
			System.out.println("5. Responsable de la venta realizada el 10/03/2013");
			System.out.println("6. Salir");
			System.out.print("Opción: ");
			try {
				op = Integer.parseInt(sc.nextLine().trim());
				if (op >= 1 && op <= 6)
					break;
				System.out.println("\n Opción no válida. Inténtalo de nuevo.\n");
			} catch (NumberFormatException e) {
				System.out.println("\n Entrada no válida. Inténtalo de nuevo.\n");
			}
		}
		return op;
	}

	public static void caso1(XPath xPath, Document doc) {
		try {
			String dptoId = (String) xPath.evaluate("/tienda/dptos/dpto[nombre='Carnicería']/@id", doc,
					XPathConstants.STRING);
			NodeList productosCarniceria = (NodeList) xPath
					.evaluate("/tienda/productos/producto[@venta='" + dptoId + "']/@id", doc, XPathConstants.NODESET);
			int total = 0;
			for (int i = 0; i < productosCarniceria.getLength(); i++) {
				String prodId = productosCarniceria.item(i).getNodeValue();
				NodeList ventas = (NodeList) xPath.evaluate("/tienda/ventas/venta[producto='" + prodId + "']/cantidad",
						doc, XPathConstants.NODESET);
				for (int j = 0; j < ventas.getLength(); j++) {
					total += Integer.parseInt(ventas.item(j).getTextContent());
				}
			}
			System.out.println("\n Cantidad total de ventas de la carnicería: " + total + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void caso2(XPath xPath, Document doc) {
		try {
			String dptoId2 = (String) xPath.evaluate("/tienda/dptos/dpto[nombre='Carnicería']/@id", doc,
					XPathConstants.STRING);
			NodeList productosCarniceria2 = (NodeList) xPath
					.evaluate("/tienda/productos/producto[@venta='" + dptoId2 + "']", doc, XPathConstants.NODESET);
			System.out.println("\n Precios de productos de carnicería:");
			for (int i = 0; i < productosCarniceria2.getLength(); i++) {
				Element prod = (Element) productosCarniceria2.item(i);
				String nombre = prod.getElementsByTagName("nombre").item(0).getTextContent();
				String precio = prod.getElementsByTagName("precio").item(0).getTextContent();
				System.out.println("  " + nombre + ": " + precio + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void caso3(XPath xPath, Document doc) {
		try {
			NodeList ventas3 = (NodeList) xPath.evaluate("/tienda/ventas/venta[cantidad=3]", doc,
					XPathConstants.NODESET);
			for (int i = 0; i < ventas3.getLength(); i++) {
				Element venta = (Element) ventas3.item(i);
				String prodId = venta.getElementsByTagName("producto").item(0).getTextContent();
				String nombre = (String) xPath.evaluate("/tienda/productos/producto[@id='" + prodId + "']/nombre", doc,
						XPathConstants.STRING);
				System.out.println("\n Producto vendido en 3 unidades: " + nombre + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void caso4(XPath xPath, Document doc) {
		try {
			String prodDepto = (String) xPath.evaluate("/tienda/productos/producto[nombre='Naranjas']/@venta", doc,
					XPathConstants.STRING);
			String responsable = (String) xPath.evaluate("/tienda/dptos/dpto[@id='" + prodDepto + "']/responsable", doc,
					XPathConstants.STRING);
			System.out.println("\n Responsable del producto Naranjas: " + responsable + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void caso5(XPath xPath, Document doc) {
		try {
			String prodIdVenta = (String) xPath.evaluate("/tienda/ventas/venta[data='2013/3/10']/producto", doc,
					XPathConstants.STRING);
			String dptoVenta = (String) xPath.evaluate("/tienda/productos/producto[@id='" + prodIdVenta + "']/@venta",
					doc, XPathConstants.STRING);
			String responsableVenta = (String) xPath.evaluate("/tienda/dptos/dpto[@id='" + dptoVenta + "']/responsable",
					doc, XPathConstants.STRING);
			System.out.println("\n Responsable de la venta realizada el 10/03/2013: " + responsableVenta + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static XPath llamadaXPath() {
		try {
			return XPathFactory.newInstance().newXPath();
		} catch (Exception e) {
			return null;
		}
	}

	public static Document llamadaDOC() {
		try {
			File inputFile = new File("ventas.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			return doc;
		} catch (Exception e) {
			return null;
		}
	}
}