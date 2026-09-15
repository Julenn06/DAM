package cliente.modelo;

import java.io.*;
import java.net.*;

public class ClientModel {
	private static final String HOST = "localhost";
	private static final int PUERTO = 5000;

	private Socket socket;
	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private String nickname;
	private boolean conectado;
	private ClientModelListener listener;
	private Thread hiloRecepcion;

	public ClientModel(ClientModelListener listener) {
		this.listener = listener;
		this.conectado = false;
	}

	public boolean conectar(String nickname) {
		this.setNickname(nickname);
		try {
			socket = new Socket(HOST, PUERTO);

			// Stream-ak sortu (GARRANTZITSUA: lehenik output, gero input)
			salida = new ObjectOutputStream(socket.getOutputStream());
			salida.flush();
			entrada = new ObjectInputStream(socket.getInputStream());

			// Enviar nickname al servidor
			salida.writeObject(nickname);
			salida.flush();

			conectado = true;

			// Harrera haria hasi
			hiloRecepcion = new Thread(new HiloRecepcion());
			hiloRecepcion.start();

			return true;
		} catch (IOException e) {
			listener.onError("Errorea konektatzean: " + e.getMessage());
			return false;
		}
	}

	public void enviarMensaje(String mensaje) {
		if (conectado && salida != null) {
			try {
				salida.writeObject(mensaje);
				salida.flush();
			} catch (IOException e) {
				listener.onError("Errorea mezua bidaltzerakoan: " + e.getMessage());
			}
		}
	}

	public void desconectar() {
		if (conectado) {
			try {
				salida.writeObject("*");
				salida.flush();
				conectado = false;
				cerrarRecursos();
			} catch (IOException e) {
				listener.onError("Errorea deskonektatzean: " + e.getMessage());
			}
		}
	}

	private void cerrarRecursos() {
		try {
			if (entrada != null)
				entrada.close();
			if (salida != null)
				salida.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			// Ixtean erroreak ezikusi
		}
	}

	// Haria zerbitzaritik mezuak jasotzeko
	private class HiloRecepcion implements Runnable {
		@Override
		public void run() {
			try {
				while (conectado) {
					String mensaje = (String) entrada.readObject();

					if (mensaje.equals("*")) {
					// Zerbitzaria itxi da
						listener.onServidorCerrado();
						conectado = false;
						break;
					} else {
						listener.onMensajeRecibido(mensaje);
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				if (conectado) {
					listener.onError("Zerbitzariarekin konexioa galdu da");
					conectado = false;
				}
			} finally {
				cerrarRecursos();
			}
		}
	}

	public boolean isConectado() {
		return conectado;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	// Interfazea gertaerak kontrolagailuari jakinarazteko
	public interface ClientModelListener {
		void onMensajeRecibido(String mensaje);

		void onServidorCerrado();

		void onError(String error);
	}
}
