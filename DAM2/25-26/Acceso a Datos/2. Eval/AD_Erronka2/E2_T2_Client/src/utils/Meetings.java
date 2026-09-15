package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import connect.Connect;

public final class Meetings {
	private static final Connect konexioa = new Connect();
	private static final String ZERBITZARIA_HOST = konexioa.getServerHost();
	private static final int ZERBITZARIA_PORTUA = konexioa.getServerPort();
	private static final int TIMEOUT = konexioa.getTimeout();

	private final Gson gson;

	public Meetings() {
		this.gson = new Gson();
	}

	public List<BileraData> lortuBileraGuztiak() {
		return exekutatuKomandoa("LORTU_REUNIONES");
	}

	public List<BileraData> lortuBilerakIrakasleaArabera(long irakasleaId) {
		return exekutatuKomandoa("LORTU_REUNIONES_PROFE:" + irakasleaId);
	}

	public List<BileraData> lortuBilerakIkasleaArabera(long ikasleaId) {
		return exekutatuKomandoa("LORTU_REUNIONES_ALUMNO:" + ikasleaId);
	}

	private List<BileraData> exekutatuKomandoa(String komandoa) {
		List<BileraData> bilerak = new ArrayList<>();

		try (Socket socket = new Socket(ZERBITZARIA_HOST, ZERBITZARIA_PORTUA)) {
			socket.setSoTimeout(TIMEOUT);

			try (PrintWriter irteera = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader sarrera = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

				irteera.println(komandoa);

				String erantzuna = sarrera.readLine();

				if (erantzuna == null || erantzuna.trim().isEmpty()) {
					System.err.println("Ez da erantzunik jaso zerbitzaritik");
					return bilerak;
				}

				JsonObject jsonErantzuna = gson.fromJson(erantzuna, JsonObject.class);

				if (jsonErantzuna.has("errorea")) {
					System.err.println("Zerbitzari errorea: " + jsonErantzuna.get("errorea").getAsString());
					return bilerak;
				}

				if (jsonErantzuna.has("reuniones")) {
					JsonArray bilerakArray = jsonErantzuna.getAsJsonArray("reuniones");

					for (int i = 0; i < bilerakArray.size(); i++) {
						JsonObject bileraJson = bilerakArray.get(i).getAsJsonObject();
						BileraData bilera = jsonToBileraData(bileraJson);
						bilerak.add(bilera);
					}

					System.out.println("✓ " + bilerak.size() + " bilera kargatuta zerbitzaritik");
				}
			}
		} catch (SocketTimeoutException e) {
			System.err.println("Timeout zerbitzarira konektatzean: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Konexio errorea: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Ustekabeko errorea: " + e.getMessage());
			e.printStackTrace();
		}

		return bilerak;
	}

	private BileraData jsonToBileraData(JsonObject bileraJson) {
		Long bileraId = bileraJson.has("idReunion") && !bileraJson.get("idReunion").isJsonNull()
				? bileraJson.get("idReunion").getAsLong()
				: null;
		
		if (bileraId == null) {
			throw new IllegalArgumentException("El campo 'idReunion' es obligatorio");
		}

		String egoera = bileraJson.has("estado") && !bileraJson.get("estado").isJsonNull()
				? bileraJson.get("estado").getAsString()
				: "pendiente";

		String egoeraeus = bileraJson.has("estadoEus") && !bileraJson.get("estadoEus").isJsonNull()
				? bileraJson.get("estadoEus").getAsString()
				: "onartzeke";

		Long irakasleaId = bileraJson.has("profesorId") && !bileraJson.get("profesorId").isJsonNull()
				? bileraJson.get("profesorId").getAsLong()
				: null;

		Long ikasleaId = bileraJson.has("alumnoId") && !bileraJson.get("alumnoId").isJsonNull()
				? bileraJson.get("alumnoId").getAsLong()
				: null;

		String zentroId = bileraJson.has("idCentro") && !bileraJson.get("idCentro").isJsonNull()
				? bileraJson.get("idCentro").getAsString()
				: "";

		String izenburua = bileraJson.has("titulo") && !bileraJson.get("titulo").isJsonNull()
				? bileraJson.get("titulo").getAsString()
				: "";

		String gaia = bileraJson.has("asunto") && !bileraJson.get("asunto").isJsonNull()
				? bileraJson.get("asunto").getAsString()
				: "";

		String gela = bileraJson.has("aula") && !bileraJson.get("aula").isJsonNull()
				? bileraJson.get("aula").getAsString()
				: "";

		LocalDateTime data = null;
		if (bileraJson.has("fecha") && !bileraJson.get("fecha").isJsonNull()) {
			String dataStr = bileraJson.get("fecha").getAsString();
			if (!dataStr.isEmpty()) {
				try {
					// Intentar primero formato ISO
					data = LocalDateTime.parse(dataStr);
				} catch (Exception e1) {
					try {
						// Intentar formato SQL timestamp: "yyyy-MM-dd HH:mm:ss.S"
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
						data = LocalDateTime.parse(dataStr, formatter);
					} catch (Exception e2) {
						System.err.println("Errorea data analizatzean: " + dataStr);
					}
				}
			}
		}

		return new BileraData(bileraId, egoera, egoeraeus, irakasleaId, ikasleaId, zentroId, izenburua, gaia, gela,
				data);
	}

	public static final class BileraData {
		private final Long bileraId;
		private final String egoera;
		private final String egoeraeus;
		private final Long irakasleaId;
		private final Long ikasleaId;
		private final String zentroId;
		private final String izenburua;
		private final String gaia;
		private final String gela;
		private final LocalDateTime data;

		public BileraData(Long bileraId, String egoera, String egoeraeus, Long irakasleaId, Long ikasleaId,
				String zentroId, String izenburua, String gaia, String gela, LocalDateTime data) {
			this.bileraId = bileraId;
			this.egoera = egoera;
			this.egoeraeus = egoeraeus;
			this.irakasleaId = irakasleaId;
			this.ikasleaId = ikasleaId;
			this.zentroId = zentroId;
			this.izenburua = izenburua;
			this.gaia = gaia;
			this.gela = gela;
			this.data = data;
		}

		public Long getBileraId() {
			return bileraId;
		}

		public String getEgoera() {
			return egoera;
		}

		public String getEgoeraeus() {
			return egoeraeus;
		}

		public Long getIrakasleaId() {
			return irakasleaId;
		}

		public Long getIkasleaId() {
			return ikasleaId;
		}

		public String getZentroId() {
			return zentroId;
		}

		public String getIzenburua() {
			return izenburua;
		}

		public String getGaia() {
			return gaia;
		}

		public String getGela() {
			return gela;
		}

		public LocalDateTime getData() {
			return data;
		}

		public String getEgoeraEuskera() {
			return switch (egoera.toLowerCase()) {
			case "pendiente" -> "Onartzeke";
			case "aceptada" -> "Onartua";
			case "denegada" -> "Ukatua";
			case "conflicto" -> "Gatazka";
			default -> egoera;
			};
		}

		public String getDataFormateatua() {
			if (data == null)
				return "Data gabe";
			DateTimeFormatter formateatzailea = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
			return data.format(formateatzailea);
		}

		public String getDataSoilik() {
			if (data == null)
				return "Data gabe";
			DateTimeFormatter formateatzailea = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			return data.format(formateatzailea);
		}

		public String getOrduaSoilik() {
			if (data == null)
				return "--:--";
			DateTimeFormatter formateatzailea = DateTimeFormatter.ofPattern("HH:mm");
			return data.format(formateatzailea);
		}

		@Override
		public String toString() {
			return "BileraData{" + "bileraId=" + bileraId + ", izenburua='" + izenburua + '\'' + ", egoera=" + egoera
					+ ", irakasleaId=" + irakasleaId + ", ikasleaId=" + ikasleaId + ", data=" + getDataFormateatua()
					+ '}';
		}
	}
}