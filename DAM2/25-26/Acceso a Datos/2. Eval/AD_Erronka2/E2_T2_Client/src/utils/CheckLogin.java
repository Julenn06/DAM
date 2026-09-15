package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import connect.Connect;

public final class CheckLogin {
	private static final Connect konexioa = new Connect();
	private static final String ZERBITZARIA_HOST = konexioa.getServerHost();
	private static final int ZERBITZARIA_PORTUA = konexioa.getServerPort();
	private static final int TIMEOUT = konexioa.getTimeout();

	private final Gson gson;

	public CheckLogin() {
		this.gson = new Gson();
	}

	public LoginErantzuna balidatuLogin(String emaila, String pasahitza) {
		if (emaila == null || emaila.trim().isEmpty()) {
			return new LoginErantzuna(false, "Emaila ezin da hutsik egon", null);
		}

		if (pasahitza == null || pasahitza.trim().isEmpty()) {
			return new LoginErantzuna(false, "Pasahitza ezin da hutsik egon", null);
		}

		try (Socket socket = new Socket(ZERBITZARIA_HOST, ZERBITZARIA_PORTUA)) {
			socket.setSoTimeout(TIMEOUT);

			try (PrintWriter irteera = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader sarrera = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

				String komandoa = String.format("LOGIN:%s:%s", emaila, pasahitza);
				irteera.println(komandoa);

				String erantzuna = sarrera.readLine();

				if (erantzuna == null || erantzuna.trim().isEmpty()) {
					return new LoginErantzuna(false, "Ez da erantzunik jaso zerbitzaritik", null);
				}

				JsonObject jsonErantzuna = gson.fromJson(erantzuna, JsonObject.class);

				boolean arrakasta = jsonErantzuna.get("arrakasta").getAsBoolean();
				String mezua = jsonErantzuna.get("mezua").getAsString();

				if (arrakasta) {
					JsonObject erabiltzaileData = jsonErantzuna.getAsJsonObject("erabiltzailea");
					ErabiltzaileData erabiltzailea = new ErabiltzaileData(erabiltzaileData.get("id").getAsLong(),
							erabiltzaileData.get("email").getAsString(), erabiltzaileData.get("username").getAsString(),
							erabiltzaileData.get("nombre").getAsString(),
							erabiltzaileData.get("apellidos").getAsString(), erabiltzaileData.get("dni").getAsString(),
							erabiltzaileData.get("tipoId").getAsInt(), erabiltzaileData.get("telefono1").getAsString(),
							erabiltzaileData.has("argazkiaUrl") && !erabiltzaileData.get("argazkiaUrl").isJsonNull()
									? erabiltzaileData.get("argazkiaUrl").getAsString()
									: null);

					return new LoginErantzuna(true, mezua, erabiltzailea);
				}

				return new LoginErantzuna(false, mezua, null);
			}

		} catch (SocketTimeoutException e) {
			return new LoginErantzuna(false, "Itxaron-denbora amaitu da. Zerbitzariak ez du erantzuten.", null);
		} catch (IOException e) {
			return new LoginErantzuna(false, "Konexio errorea: " + e.getMessage(), null);
		} catch (Exception e) {
			return new LoginErantzuna(false, "Ustekabeko errorea: " + e.getMessage(), null);
		}
	}

	public static final class LoginErantzuna {
		private final boolean arrakastatsua;
		private final String mezua;
		private final ErabiltzaileData erabiltzailea;

		public LoginErantzuna(boolean arrakastatsua, String mezua, ErabiltzaileData erabiltzailea) {
			this.arrakastatsua = arrakastatsua;
			this.mezua = mezua;
			this.erabiltzailea = erabiltzailea;
		}

		public boolean isArrakastatsua() {
			return arrakastatsua;
		}

		public String getMezua() {
			return mezua;
		}

		public ErabiltzaileData getErabiltzailea() {
			return erabiltzailea;
		}
	}

	public static final class ErabiltzaileData {
		private final Long id;
		private final String email;
		private final String erabiltzaileIzena;
		private final String izena;
		private final String abizenak;
		private final String dni;
		private final Integer mota;
		private final String telefonoa1;
		private final String argazkiaUrl;

		public ErabiltzaileData(Long id, String email, String erabiltzaileIzena, String izena, String abizenak,
				String dni, Integer mota, String telefonoa1, String argazkiaUrl) {
			this.id = id;
			this.email = email;
			this.erabiltzaileIzena = erabiltzaileIzena;
			this.izena = izena;
			this.abizenak = abizenak;
			this.dni = dni;
			this.mota = mota;
			this.telefonoa1 = telefonoa1;
			this.argazkiaUrl = argazkiaUrl;
		}

		public Long getId() {
			return id;
		}

		public String getEmail() {
			return email;
		}

		public String getErabiltzaileIzena() {
			return erabiltzaileIzena;
		}

		public String getIzena() {
			return izena;
		}

		public String getAbizenak() {
			return abizenak;
		}

		public String getDni() {
			return dni;
		}

		public Integer getMota() {
			return mota;
		}

		public String getTelefonoa1() {
			return telefonoa1;
		}

		public String getArgazkiaUrl() {
			return argazkiaUrl;
		}

		public String getIzenOsoa() {
			return izena + " " + abizenak;
		}
	}
}