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

public final class Profile {
	private static final Connect konexioa = new Connect();
	private static final String ZERBITZARIA_HOST = konexioa.getServerHost();
	private static final int ZERBITZARIA_PORTUA = konexioa.getServerPort();
	private static final int TIMEOUT = konexioa.getTimeout();

	private final Gson gson;

	public Profile() {
		this.gson = new Gson();
	}

	public ErabiltzaileProfilData lortuErabiltzaileProfila(long erabiltzaileId) {
		try (Socket socket = new Socket(ZERBITZARIA_HOST, ZERBITZARIA_PORTUA)) {
			socket.setSoTimeout(TIMEOUT);

			try (PrintWriter irteera = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader sarrera = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

				String komandoa = "LORTU_USER:" + erabiltzaileId;
				irteera.println(komandoa);

				String erantzuna = sarrera.readLine();

				if (erantzuna == null || erantzuna.trim().isEmpty()) {
					System.err.println("Ez da erantzunik jaso zerbitzaritik");
					return null;
				}

				JsonObject jsonErantzuna = gson.fromJson(erantzuna, JsonObject.class);

				if (jsonErantzuna.has("errorea")) {
					System.err.println("Zerbitzari errorea: " + jsonErantzuna.get("errorea").getAsString());
					return null;
				}

				if (jsonErantzuna.has("aurkituta") && jsonErantzuna.get("aurkituta").getAsBoolean()) {
					JsonObject userJson = jsonErantzuna.getAsJsonObject("user");
					return jsonToErabiltzaileProfilData(userJson);
				}

				System.err.println("Ez da aurkitu erabiltzailea ID honekin: " + erabiltzaileId);
				return null;
			}
		} catch (SocketTimeoutException e) {
			System.err.println("Timeout zerbitzarira konektatzean: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.err.println("Konexio errorea: " + e.getMessage());
			return null;
		} catch (Exception e) {
			System.err.println("Ustekabeko errorea: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public String lortuMotaIzena(long motaId) {
		try (Socket socket = new Socket(ZERBITZARIA_HOST, ZERBITZARIA_PORTUA)) {
			socket.setSoTimeout(TIMEOUT);

			try (PrintWriter irteera = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader sarrera = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

				String komandoa = "LORTU_TIPO:" + motaId;
				irteera.println(komandoa);

				String erantzuna = sarrera.readLine();

				if (erantzuna == null || erantzuna.trim().isEmpty()) {
					return getMotaIzenaLehenetsiz(motaId);
				}

				JsonObject jsonErantzuna = gson.fromJson(erantzuna, JsonObject.class);

				if (jsonErantzuna.has("aurkituta") && jsonErantzuna.get("aurkituta").getAsBoolean()) {
					JsonObject tipoJson = jsonErantzuna.getAsJsonObject("tipo");
					return tipoJson.has("name") && !tipoJson.get("name").isJsonNull()
							? tipoJson.get("name").getAsString()
							: getMotaIzenaLehenetsiz(motaId);
				}

				return getMotaIzenaLehenetsiz(motaId);
			}
		} catch (Exception e) {
			System.err.println("Errorea mota lortzean: " + e.getMessage());
			return getMotaIzenaLehenetsiz(motaId);
		}
	}

	private String getMotaIzenaLehenetsiz(long motaId) {
		return switch ((int) motaId) {
		case 1 -> "God";
		case 2 -> "Administratzailea";
		case 3 -> "Irakaslea";
		case 4 -> "Ikaslea";
		default -> "Erabiltzailea";
		};
	}

	public boolean eguneratuErabiltzailea(Long id, String izena, String abizenak, String dni, 
			String helbidea, String telefonoa1, String telefonoa2) {
		try (Socket socket = new Socket(ZERBITZARIA_HOST, ZERBITZARIA_PORTUA)) {
			socket.setSoTimeout(TIMEOUT);

			try (PrintWriter irteera = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader sarrera = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

				// Create JSON with updated user data
				JsonObject userData = new JsonObject();
				userData.addProperty("id", id);
				userData.addProperty("nombre", izena);
				userData.addProperty("apellidos", abizenak);
				userData.addProperty("dni", dni);
				userData.addProperty("direccion", helbidea);
				userData.addProperty("telefono1", telefonoa1);
				userData.addProperty("telefono2", telefonoa2);

				String komandoa = "EGUNERATU_USER:" + gson.toJson(userData);
				irteera.println(komandoa);

				String erantzuna = sarrera.readLine();

				if (erantzuna == null || erantzuna.trim().isEmpty()) {
					System.err.println("Ez da erantzunik jaso zerbitzaritik");
					return false;
				}

				JsonObject jsonErantzuna = gson.fromJson(erantzuna, JsonObject.class);

				if (jsonErantzuna.has("errorea")) {
					System.err.println("Zerbitzari errorea: " + jsonErantzuna.get("errorea").getAsString());
					return false;
				}

				if (jsonErantzuna.has("arrakasta") && jsonErantzuna.get("arrakasta").getAsBoolean()) {
					System.out.println("Erabiltzailea ondo eguneratu da");
					return true;
				}

				System.err.println("Ezin izan da erabiltzailea eguneratu");
				return false;
			}
		} catch (SocketTimeoutException e) {
			System.err.println("Timeout zerbitzarira konektatzean: " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.err.println("Konexio errorea: " + e.getMessage());
			return false;
		} catch (Exception e) {
			System.err.println("Ustekabeko errorea: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	private ErabiltzaileProfilData jsonToErabiltzaileProfilData(JsonObject userJson) {
		Long id = userJson.get("id").getAsLong();
		String email = userJson.get("email").getAsString();
		String erabiltzaileIzena = userJson.get("username").getAsString();

		String izena = userJson.has("nombre") && !userJson.get("nombre").isJsonNull()
				? userJson.get("nombre").getAsString()
				: "";

		String abizenak = userJson.has("apellidos") && !userJson.get("apellidos").isJsonNull()
				? userJson.get("apellidos").getAsString()
				: "";

		String dni = userJson.has("dni") && !userJson.get("dni").isJsonNull() ? userJson.get("dni").getAsString() : "";

		String helbidea = userJson.has("direccion") && !userJson.get("direccion").isJsonNull()
				? userJson.get("direccion").getAsString()
				: "";

		String telefonoa1 = userJson.has("telefono1") && !userJson.get("telefono1").isJsonNull()
				? userJson.get("telefono1").getAsString()
				: "";

		String telefonoa2 = userJson.has("telefono2") && !userJson.get("telefono2").isJsonNull()
				? userJson.get("telefono2").getAsString()
				: "";

		Integer mota = userJson.get("tipoId").getAsInt();

		String argazkiaUrl = userJson.has("argazkiaUrl") && !userJson.get("argazkiaUrl").isJsonNull()
				? userJson.get("argazkiaUrl").getAsString()
				: null;

		return new ErabiltzaileProfilData(id, email, erabiltzaileIzena, izena, abizenak, dni, helbidea, telefonoa1,
				telefonoa2, mota, argazkiaUrl);
	}

	public static final class ErabiltzaileProfilData {
		private final Long id;
		private final String email;
		private final String erabiltzaileIzena;
		private final String izena;
		private final String abizenak;
		private final String dni;
		private final String helbidea;
		private final String telefonoa1;
		private final String telefonoa2;
		private final Integer mota;
		private final String argazkiaUrl;

		public ErabiltzaileProfilData(Long id, String email, String erabiltzaileIzena, String izena, String abizenak,
				String dni, String helbidea, String telefonoa1, String telefonoa2, Integer mota, String argazkiaUrl) {
			this.id = id;
			this.email = email;
			this.erabiltzaileIzena = erabiltzaileIzena;
			this.izena = izena;
			this.abizenak = abizenak;
			this.dni = dni;
			this.helbidea = helbidea;
			this.telefonoa1 = telefonoa1;
			this.telefonoa2 = telefonoa2;
			this.mota = mota;
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

		public String getHelbidea() {
			return helbidea;
		}

		public String getTelefonoa1() {
			return telefonoa1;
		}

		public String getTelefonoa2() {
			return telefonoa2;
		}

		public Integer getMota() {
			return mota;
		}

		public String getArgazkiaUrl() {
			return argazkiaUrl;
		}

		public String getIzenOsoa() {
			if (izena.isEmpty() && abizenak.isEmpty()) {
				return erabiltzaileIzena;
			}
			return (izena + " " + abizenak).trim();
		}

		public String getMotaIzena() {
			return switch (mota) {
			case 1 -> "God";
			case 2 -> "Administratzailea";
			case 3 -> "Irakaslea";
			case 4 -> "Ikaslea";
			default -> "Erabiltzailea";
			};
		}

		@Override
		public String toString() {
			return "ErabiltzaileProfilData{" + "id=" + id + ", email='" + email + '\'' + ", izena='" + getIzenOsoa()
					+ '\'' + ", mota=" + mota + '}';
		}
	}
}