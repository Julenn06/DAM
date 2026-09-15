package servidor.modelo;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerModel extends Thread {
	private static final int PUERTO = 5000;
	private static final int MAX_CLIENTES = 10;

	private ServerSocket serverSocket;
	private List<ObjectOutputStream> clientesConectados;
	private int numeroClientes;
	private boolean ejecutando;
	private ServerModelListener listener;

	public ServerModel(ServerModelListener listener) {
		this.listener = listener;
		this.clientesConectados = new ArrayList<>();
		this.numeroClientes = 0;
		this.ejecutando = true;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(PUERTO);
		listener.onMensaje("Zerbitzaria hasita " + PUERTO + " portuan");
		listener.onMensaje("Konexioak zain...");
			while (ejecutando && numeroClientes < MAX_CLIENTES) {
				Socket clienteSocket = serverSocket.accept();

			// Stream-ak sortu (GARRANTZITSUA: lehenik output, gero input)
				ObjectOutputStream salida = new ObjectOutputStream(clienteSocket.getOutputStream());
				salida.flush();
				ObjectInputStream entrada = new ObjectInputStream(clienteSocket.getInputStream());

// Bezeroaren goitizena irakurri
				String nickname = (String) entrada.readObject();

				synchronized (clientesConectados) {
					clientesConectados.add(salida);
					numeroClientes++;
				}

				listener.onClienteConectado(nickname, numeroClientes);

// Konexio mezua bezero guztiei bidali
				enviarMensajeATodos(nickname + " txatean sartzen da");

			// Haria sortu bezero honen mezuak jasotzeko
				Thread hiloRecepcion = new HiloRecepcion(entrada, salida, nickname);
				hiloRecepcion.start();
			}
		} catch (IOException | ClassNotFoundException e) {
			if (ejecutando) {
				listener.onMensaje("Error: " + e.getMessage());
			}
		}
	}

	public void desconectar() {
		ejecutando = false;
		try {
// Deskonexio seinalea bezero guztiei bidali
		synchronized (clientesConectados) {
			for (ObjectOutputStream salida : clientesConectados) {
				try {
					salida.writeObject("*");
					salida.flush();
				} catch (IOException e) {
					// Bidaltzean erroreak ezikusi
					}
				}
			}

			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			listener.onMensaje("Servidor cerrado");
		} catch (IOException e) {
			listener.onMensaje("Error al cerrar servidor: " + e.getMessage());
		}
	}

	private void enviarMensajeATodos(String mensaje) {
		synchronized (clientesConectados) {
			Iterator<ObjectOutputStream> iterator = clientesConectados.iterator();
			while (iterator.hasNext()) {
				ObjectOutputStream salida = iterator.next();
				try {
					salida.writeObject(mensaje);
					salida.flush();
				} catch (IOException e) {
					iterator.remove();
					numeroClientes--;
				}
			}
		}
	}

	// Haria bezero bakoitzaren mezuak jasotzeko
	private class HiloRecepcion extends Thread {
		private ObjectInputStream entrada;
		private ObjectOutputStream salida;
		private String nickname;

		public HiloRecepcion(ObjectInputStream entrada, ObjectOutputStream salida, String nickname) {
			this.entrada = entrada;
			this.salida = salida;
			this.nickname = nickname;
		}

		@Override
		public void run() {
			try {
				while (ejecutando) {
					String mensaje = (String) entrada.readObject();

					if (mensaje.equals("*")) {
					// Bezeroa deskonektatzen da
						synchronized (clientesConectados) {
							clientesConectados.remove(salida);
							numeroClientes--;
						}
						listener.onClienteDesconectado(nickname, numeroClientes);
						enviarMensajeATodos(nickname + "-ek txatetik irten da");
						break;
					} else {
					// Mezua bezero guztiei birbidali
						String mensajeCompleto = nickname + ": " + mensaje;
						listener.onMensaje(mensajeCompleto);
						enviarMensajeATodos(mensajeCompleto);
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				// Bezeroa ustekabean deskonektatu da
				synchronized (clientesConectados) {
					clientesConectados.remove(salida);
					numeroClientes--;
				}
				listener.onClienteDesconectado(nickname, numeroClientes);
			}
		}
	}

	// Interfazea gertaerak kontrolagailuari jakinarazteko
	public interface ServerModelListener {
		void onClienteConectado(String nickname, int numClientes);

		void onClienteDesconectado(String nickname, int numClientes);

		void onMensaje(String mensaje);
	}
}
