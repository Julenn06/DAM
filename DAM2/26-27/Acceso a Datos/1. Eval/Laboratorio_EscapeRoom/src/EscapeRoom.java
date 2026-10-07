import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import modelo.Cerradura;
import modelo.Muestra;
import modelo.Resultado;

/**
 * ESCAPE ROOM: FUGA DEL LABORATORIO
 *
 * Equipo: ______________________________
 *
 * COMO FUNCIONA: - Cada sala es un metodo. Empieza por la sala 1. - Cuando
 * superes una sala, QUITA las // de la siguiente linea del main y vuelve a
 * ejecutar. - Los pasos de cada sala estan en el enunciado y en los
 * comentarios. - Donde veas ??? tienes que poner lo que descubras en la sala
 * anterior.
 */
public class EscapeRoom {

	public static void main(String[] args) throws IOException {
		sala1();
		sala2();
		ArrayList<Muestra> muestras = sala3();
		sala4(muestras);
		String codigo = sala5();
		sala6(codigo);
	}

	// =====================================================================
	// BITACORA: escribe una linea al final del fichero bitacora.txt
	// Llama a este metodo al final de cada sala.
	// =====================================================================
	public static void apuntar(String texto) {

		// Se envuelve en un bloque try-catch para manejar posibles errores de
		// entrada/salida (IOException)

		try {

			// PASO A: crea un BufferedWriter sobre new FileWriter("bitacora.txt", true)

			BufferedWriter bw = new BufferedWriter(new FileWriter("bitacora.txt", true));

			// PASO B: escribe el texto con write(...)

			bw.write(texto);

			// PASO C: salta de linea con newLine()

			bw.newLine();

			// PASO D: cierra con close()

			bw.close();

		} catch (IOException e) {

			System.out.println("Ocurrió un error al escribir en la bitácora: " + e.getMessage());

		}

	}

	// =====================================================================
	// SALA 1: RECEPCION (fichero de texto)
	// =====================================================================
	public static void sala1() {
		System.out.println("===== SALA 1: RECEPCION =====");
		// PASO 1: abre "registro.txt" con un BufferedReader
		// PASO 2: lee linea a linea con readLine() hasta que devuelva null
		// PASO 3: si la linea empieza por ">" -> linea.startsWith(">")
		// muestrala SIN el ">" -> linea.substring(1)
		// PASO 4: cierra el fichero
		// PASO 5: apuntar("Sala 1 superada. La llave esta en ...");
		try (BufferedReader reader = new BufferedReader(new FileReader("registro.txt"))) {
			String linea;

			while ((linea = reader.readLine()) != null) {
				linea = linea.trim();

				if (linea.startsWith(">")) {
					System.out.println(linea.substring(1));
				}
			}

			apuntar("Sala 1 superada. La llave esta en");

		} catch (Exception ex) {
			System.out.println("Error al leer el archivo: " + ex.getMessage());
		}
	}

	// =====================================================================
	// SALA 2: ALMACEN DE CAJAS (fichero binario con DataInputStream)
	// =====================================================================
	public static void sala2() throws IOException {
		System.out.println("===== SALA 2: ALMACEN DE CAJAS =====");
		// PASO 1: abre la caja buena:
		// new DataInputStream(new FileInputStream("???"))
		// PASO 2: lee el primer int -> es el numero de letras
		// crea un array: char[] mensaje = new char[numLetras];
		// PASO 3: dentro de un while (true) lee UN registro cada vuelta,
		// EN EL ORDEN que te dijo la sala 1 (??? , ??? , ???)
		// Si el registro es bueno: mensaje[posicion] = (char) letra;
		// PASO 4: el while (true) termina cuando salta EOFException:
		// ponlo dentro de un try { ... } catch (EOFException eof) { }
		// PASO 5: cierra y muestra el mensaje: new String(mensaje)
		// PASO 6: apuntar("Sala 2 superada. " + ...);
		DataInputStream din = null;
		try {
			din = new DataInputStream(new FileInputStream("caja_4.dat"));

			int numLetras = din.readInt();
			char[] mensaje = new char[numLetras];

			try {

				while (true) {
					int posicion = din.readInt();
					int letraInt = din.readInt();
					boolean bien = din.readBoolean();

					if (bien) {
						mensaje[posicion] = (char) letraInt;
					}
				}
			} catch (EOFException eof) {
				System.out.println("Final Binario.");
			}

			din.close();
			String mensajeFinal = new String(mensaje);
			System.out.println("Mensaje: " + mensajeFinal);

			apuntar("Sala 2 superada. El mensaje es: " + mensajeFinal);

		} catch (FileNotFoundException e) {
			System.out.println("Error: No se pudo encontrar el archivo caja_4.dat");
		} finally {
			if (din != null) {
				din.close();
			}
		}
	}

	// =====================================================================
	// SALA 3: CONGELADOR (fichero de objetos con ObjectInputStream)
	// =====================================================================
	@SuppressWarnings("unchecked")
	public static ArrayList<Muestra> sala3() {
		System.out.println("===== SALA 3: CONGELADOR =====");
		ArrayList<Muestra> muestras = new ArrayList<>();
		// PASO 1: abre el fichero que te dijo la sala 2:
		// new ObjectInputStream(new FileInputStream("???"))
		// PASO 2: lee la lista entera de una vez:
		// muestras = (ArrayList<Muestra>) ois.readObject();
		// PASO 3: cierra y muestra todas las muestras (for + println)
		// PASO 4: busca la muestra cuya nota NO este vacia
		// ( !m.getNota().isEmpty() ) y muestra su nota
		// PASO 5: apuntar("Sala 3 superada. ...");
		// (el catch debe ser: catch (IOException | ClassNotFoundException e) )

		ObjectInputStream ois = null;

		try {
			ois = new ObjectInputStream(new FileInputStream("congelador.obj"));

			muestras = (ArrayList<Muestra>) ois.readObject();

			ois.close();

			for (Muestra m : muestras) {
				System.out.println(m);
			}

			String notaEncontrada = "";
			for (Muestra m : muestras) {
				if (!m.getNota().isEmpty()) {
					notaEncontrada = m.getNota();
					System.out.println("nota: " + notaEncontrada);
					break;
				}
			}

			apuntar("Sala 3 superada. Nota encontrada: " + notaEncontrada);

		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
				}
			}
		}

		return muestras;
	}

	// =====================================================================
	// SALA 4: SALA DE ANALISIS (crear un XML con DOM)
	// =====================================================================
	public static void sala4(ArrayList<Muestra> muestras) {
		System.out.println("===== SALA 4: SALA DE ANALISIS =====");
		// PASO 1: crea un documento vacio:
		// DocumentBuilderFactory -> DocumentBuilder -> builder.newDocument()
		// PASO 2: crea la raiz <laboratorio> y enganchala al documento
		// PASO 3: recorre la lista. SOLO para las muestras que diga la nota:
		// - crea <muestra> con el atributo id (setAttribute)
		// - crea <nombre>, <tipo> y <temperatura> con setTextContent(...)
		// (la temperatura es int: usa String.valueOf(...))
		// - enganchalos con appendChild(...)
		// PASO 4: guarda el documento en el fichero que diga la nota
		// con un Transformer (mira el ejemplo G.1.4)
		// PASO 5: apuntar("Sala 4 superada. ...");
		// (el catch puede ser: catch (Exception e) )

		try {
			File archivoFichero = new File("informe.xml");
			if (archivoFichero.exists()) {
				archivoFichero.delete();
			}

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

			Element raiz = doc.createElement("laboratorio");
			doc.appendChild(raiz);

			int contadorMuestrasMetidas = 0;

			for (Muestra m : muestras) {

				if (m != null && m.isPeligrosa()) {

					contadorMuestrasMetidas++;

					Element muestra = doc.createElement("muestra");
					muestra.setAttribute("id", m.getId());

					Element nombre = doc.createElement("nombre");
					nombre.setTextContent(m.getNombre());
					muestra.appendChild(nombre);

					Element tipo = doc.createElement("tipo");
					tipo.setTextContent(m.getTipo());
					muestra.appendChild(tipo);

					Element temperatura = doc.createElement("temperatura");
					temperatura.setTextContent(String.valueOf(m.getTemperatura()));
					muestra.appendChild(temperatura);

					raiz.appendChild(muestra);
				}
			}

			System.out.println("-> Total muestras procesadas que pasaron el filtro: " + contadorMuestrasMetidas);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("informe.xml"));
			transformer.transform(source, result);

			apuntar("Sala 4 superada. Muestras metidas: " + contadorMuestrasMetidas);

		} catch (Exception e) {
			System.out.println("Error en Sala 4: " + e.getMessage());
		}
	}

	// =====================================================================
	// SALA 5: ORDENADOR CENTRAL (consultas XPath)
	// =====================================================================
	public static String sala5() {
		System.out.println("===== SALA 5: ORDENADOR CENTRAL =====");
		String codigo = "";
		// PASO 1: carga TU fichero XML de la sala 4 con builder.parse("???")
		// PASO 2: crea el objeto XPath: XPathFactory.newInstance().newXPath()
		// PASO 3: haz las 3 consultas del enunciado
		// PASO 4: junta el codigo: numero1 + "-" + texto2 + "-" + numero3
		// (los numeros son Double: usa .intValue() para quitar el .0)
		// PASO 5: muestra el codigo y apuntar("Sala 5 superada. Codigo: " + codigo);

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new File("informe.xml")); // ¡CORREGIDO AQUÍ con new File()!

			XPath xpath = XPathFactory.newInstance().newXPath();

			Double num1 = (Double) xpath.compile("count(//muestra)").evaluate(doc, XPathConstants.NUMBER);
			System.out.println("Consulta 1: " + num1.intValue());

			String texto2 = (String) xpath.compile("//muestra[@id='M07']/nombre").evaluate(doc, XPathConstants.STRING);
			System.out.println("Consulta 2: " + texto2);

			Double num3 = (Double) xpath.compile("count(//muestra[temperatura < 0])").evaluate(doc,
					XPathConstants.NUMBER);
			System.out.println("Consulta 3: " + num3.intValue());

			codigo = num1.intValue() + "-" + texto2 + "-" + num3.intValue();

			System.out.println("Código final: " + codigo);
			apuntar("Sala 5 superada. Codigo: " + codigo);

		} catch (Exception e) {
			System.out.println("Error en Sala 5: " + e.getMessage());
		}

		return codigo;
	}

	// =====================================================================
	// SALA 6: PUERTA DE SALIDA (JSON con Gson)
	// =====================================================================
	public static void sala6(String codigo) {
		System.out.println("===== SALA 6: PUERTA DE SALIDA =====");
		// PASO 0: crea en el paquete modelo las clases Cerradura y Resultado
		// (mira el enunciado)
		// PASO 1: lee "cerraduras.json" con Gson en un array: Cerradura[]
		// PASO 2: recorre el array y busca la cerradura con TU codigo
		// (compara Strings con equals)
		// PASO 3: muestra su mensaje
		// PASO 4: crea un Resultado (equipo, codigo, mensaje) y guardalo en
		// "salida.json" con Gson (con setPrettyPrinting)
		// PASO 5: apuntar("Sala 6: " + mensaje);

		try {
			Gson gson = new Gson();

			FileReader reader = new FileReader("cerraduras.json");
			Cerradura[] cerraduras = gson.fromJson(reader, Cerradura[].class);
			reader.close();

			String mensajeEncontrado = "Código inválido o cerradura no encontrada";

			for (Cerradura c : cerraduras) {
				if (c.getCodigo() != null && c.getCodigo().equals(codigo)) {
					mensajeEncontrado = c.getMensaje();
					System.out.println("PUERTA ABIERTA " + mensajeEncontrado);
					break;
				}
			}

			Resultado resultado = new Resultado("Equipo Alfa", codigo, mensajeEncontrado);

			Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
			FileWriter writer = new FileWriter("salida.json");
			gsonPretty.toJson(resultado, writer);
			writer.close();
			apuntar("Sala 6: " + mensajeEncontrado);

		} catch (Exception e) {
			System.out.println("Error en Sala 6: " + e.getMessage());
		}
	}
}