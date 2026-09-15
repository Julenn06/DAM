package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GetAllUsers {

	private static final String ZERBITZARI_OSTALARIA = "localhost";
	private static final int ZERBITZARI_PORTUA = 6000;
	private static final int TIMEOUT = 5000;

	private Gson gson;

	public GetAllUsers() {
		this.gson = new Gson();
	}

	public List<ErabiltzaileData> lortuErabiltzaileGuztiak() {
		return exekutatuAgindua("LORTU_USERS");
	}

	public List<ErabiltzaileData> lortuErabiltzaileakMotarenArabera(int motaId) {
		return exekutatuAgindua("LORTU_USERS_TIPO:" + motaId);
	}

	public List<ErabiltzaileData> lortuAlumnoakIrakaslearenArabera(long profeId) {
		return exekutatuAgindua("LORTU_ALUMNOS_PROFE:" + profeId);
	}

	public ErabiltzaileData lortuErabiltzaileaIdArabera(long erabiltzaileId) {
		Socket socket = null;

		try {
			socket = new Socket(ZERBITZARI_OSTALARIA, ZERBITZARI_PORTUA);
			socket.setSoTimeout(TIMEOUT);

			PrintWriter irteera = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader sarrera = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String agindua = "LORTU_USER:" + erabiltzaileId;
			irteera.println(agindua);

			String erantzuna = sarrera.readLine();

			if (erantzuna == null || erantzuna.trim().isEmpty()) {
				return null;
			}

			JsonObject jsonErantzuna = gson.fromJson(erantzuna, JsonObject.class);

			if (jsonErantzuna.has("aurkituta") && jsonErantzuna.get("aurkituta").getAsBoolean()) {
				JsonObject userJson = jsonErantzuna.getAsJsonObject("user");
				return jsonToErabiltzaileData(userJson);
			}

			return null;

		} catch (Exception e) {
			System.err.println("Errorea erabiltzailea ID-z lortzerakoan: " + e.getMessage());
			return null;

		} finally {
			itxiSocket(socket);
		}
	}

	private List<ErabiltzaileData> exekutatuAgindua(String agindua) {
		List<ErabiltzaileData> erabiltzaileak = new ArrayList<>();
		Socket socket = null;

		try {
			socket = new Socket(ZERBITZARI_OSTALARIA, ZERBITZARI_PORTUA);
			socket.setSoTimeout(TIMEOUT);

			PrintWriter irteera = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader sarrera = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			irteera.println(agindua);

			String erantzuna = sarrera.readLine();

			if (erantzuna == null || erantzuna.trim().isEmpty()) {
				System.err.println("Ez da erantzunik jaso zerbitzaritik");
				return erabiltzaileak;
			}

			JsonObject jsonErantzuna = gson.fromJson(erantzuna, JsonObject.class);

			if (jsonErantzuna.has("errorea")) {
				System.err.println("Zerbitzariaren errorea: " + jsonErantzuna.get("errorea").getAsString());
				return erabiltzaileak;
			}

			if (jsonErantzuna.has("users")) {
				JsonArray usersArray = jsonErantzuna.getAsJsonArray("users");

				for (int i = 0; i < usersArray.size(); i++) {
					JsonObject userJson = usersArray.get(i).getAsJsonObject();
					ErabiltzaileData erabiltzailea = jsonToErabiltzaileData(userJson);
					erabiltzaileak.add(erabiltzailea);
				}

				System.out.println("✓ " + erabiltzaileak.size() + " erabiltzaile kargatuta zerbitzaritik");
			}

		} catch (SocketTimeoutException e) {
			System.err.println("Timeout zerbitzarira konektatzerakoan: " + e.getMessage());

		} catch (IOException e) {
			System.err.println("Konexio errorea: " + e.getMessage());

		} catch (Exception e) {
			System.err.println("Ustekabeko errorea: " + e.getMessage());
			e.printStackTrace();

		} finally {
			itxiSocket(socket);
		}

		return erabiltzaileak;
	}

	private ErabiltzaileData jsonToErabiltzaileData(JsonObject userJson) {
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
		Integer motaId = userJson.get("tipoId").getAsInt();
		String telefonoa1 = userJson.has("telefono1") && !userJson.get("telefono1").isJsonNull()
				? userJson.get("telefono1").getAsString()
				: "";
		String argazkiaUrl = userJson.has("argazkiaUrl") && !userJson.get("argazkiaUrl").isJsonNull()
				? userJson.get("argazkiaUrl").getAsString()
				: null;
		String helbidea = userJson.has("direccion") && !userJson.get("direccion").isJsonNull()
				? userJson.get("direccion").getAsString()
				: "";
		String telefonoa2 = userJson.has("telefono2") && !userJson.get("telefono2").isJsonNull()
				? userJson.get("telefono2").getAsString()
				: "";

		return new ErabiltzaileData(id, email, erabiltzaileIzena, izena, abizenak, dni, motaId, telefonoa1, argazkiaUrl,
				helbidea, telefonoa2);
	}

	private void itxiSocket(Socket socket) {
		if (socket != null && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println("Errorea socket-a ixtean: " + e.getMessage());
			}
		}
	}

	public static class ErabiltzaileData {
		private Long id;
		private String email;
		private String erabiltzaileIzena;
		private String izena;
		private String abizenak;
		private String dni;
		private Integer motaId;
		private String telefonoa1;
		private String argazkiaUrl;
		private String helbidea;
		private String telefonoa2;

		public ErabiltzaileData(Long id, String email, String erabiltzaileIzena, String izena, String abizenak,
				String dni, Integer motaId, String telefonoa1, String argazkiaUrl, String helbidea, String telefonoa2) {
			this.id = id;
			this.email = email;
			this.erabiltzaileIzena = erabiltzaileIzena;
			this.izena = izena;
			this.abizenak = abizenak;
			this.dni = dni;
			this.motaId = motaId;
			this.telefonoa1 = telefonoa1;
			this.argazkiaUrl = argazkiaUrl;
			this.helbidea = helbidea;
			this.telefonoa2 = telefonoa2;
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

		public Integer getMotaId() {
			return motaId;
		}

		public String getTelefonoa1() {
			return telefonoa1;
		}

		public String getArgazkiaUrl() {
			return argazkiaUrl;
		}

		public String getHelbidea() {
			return helbidea;
		}

		public String getTelefonoa2() {
			return telefonoa2;
		}

		public String getIzenOsoa() {
			return izena + " " + abizenak;
		}

		public String getMotaIzena() {
			switch (motaId) {
			case 1:
				return "God";
			case 2:
				return "Admin";
			case 3:
				return "Irakaslea";
			case 4:
				return "Ikaslea";
			default:
				return "Ezezaguna";
			}
		}

		@Override
		public String toString() {
			return "ErabiltzaileData{" + "id=" + id + ", email='" + email + '\'' + ", izena='" + izena + '\''
					+ ", abizenak='" + abizenak + '\'' + ", motaId=" + motaId + '}';
		}
	}
}