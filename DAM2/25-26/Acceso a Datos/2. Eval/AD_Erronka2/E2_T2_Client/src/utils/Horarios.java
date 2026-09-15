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

import connect.Connect;

public final class Horarios {
	private static final Connect konexioa = new Connect();
	private static final String ZERBITZARIA_HOST = konexioa.getServerHost();
	private static final int ZERBITZARIA_PORTUA = konexioa.getServerPort();
	private static final int TIMEOUT = konexioa.getTimeout();

	private final Gson gson;

	public Horarios() {
		this.gson = new Gson();
	}

	public List<HorarioData> lortuProfeHorarioak(long profeId) {
		try (Socket socket = new Socket(ZERBITZARIA_HOST, ZERBITZARIA_PORTUA)) {
			socket.setSoTimeout(TIMEOUT);

			try (PrintWriter irteera = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader sarrera = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

				String komandoa = "LORTU_HORARIO:" + profeId;
				irteera.println(komandoa);

				String erantzuna = sarrera.readLine();

				if (erantzuna == null || erantzuna.trim().isEmpty()) {
					System.err.println("Ez da erantzunik jaso zerbitzaritik");
					return new ArrayList<>();
				}

				JsonObject jsonErantzuna = gson.fromJson(erantzuna, JsonObject.class);

				if (jsonErantzuna.has("errorea")) {
					System.err.println("Zerbitzari errorea: " + jsonErantzuna.get("errorea").getAsString());
					return new ArrayList<>();
				}

				if (jsonErantzuna.has("horarios")) {
					JsonArray horariosArray = jsonErantzuna.getAsJsonArray("horarios");
					List<HorarioData> horarios = new ArrayList<>();

					for (int i = 0; i < horariosArray.size(); i++) {
						JsonObject horarioJson = horariosArray.get(i).getAsJsonObject();
						horarios.add(jsonToHorarioData(horarioJson));
					}

					System.out.println("✓ " + horarios.size() + " horario kargatuta");
					return horarios;
				}

				return new ArrayList<>();
			}
		} catch (SocketTimeoutException e) {
			System.err.println("Timeout zerbitzarira konektatzean: " + e.getMessage());
			return new ArrayList<>();
		} catch (IOException e) {
			System.err.println("Konexio errorea: " + e.getMessage());
			return new ArrayList<>();
		} catch (Exception e) {
			System.err.println("Ustekabeko errorea: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	private HorarioData jsonToHorarioData(JsonObject horarioJson) {
		Long id = horarioJson.get("id").getAsLong();
		String dia = horarioJson.get("dia").getAsString();
		int hora = horarioJson.get("hora").getAsInt();
		Long profeId = horarioJson.has("profeId") && !horarioJson.get("profeId").isJsonNull()
				? horarioJson.get("profeId").getAsLong()
				: null;
		Long moduloId = horarioJson.has("moduloId") && !horarioJson.get("moduloId").isJsonNull()
				? horarioJson.get("moduloId").getAsLong()
				: null;
		String aula = horarioJson.has("aula") && !horarioJson.get("aula").isJsonNull()
				? horarioJson.get("aula").getAsString()
				: "";
		String observaciones = horarioJson.has("observaciones") && !horarioJson.get("observaciones").isJsonNull()
				? horarioJson.get("observaciones").getAsString()
				: "";

		return new HorarioData(id, dia, hora, profeId, moduloId, aula, observaciones);
	}

	public static final class HorarioData {
		private final Long id;
		private final String dia;
		private final int hora;
		private final Long profeId;
		private final Long moduloId;
		private final String aula;
		private final String observaciones;

		public HorarioData(Long id, String dia, int hora, Long profeId, Long moduloId, String aula,
				String observaciones) {
			this.id = id;
			this.dia = dia;
			this.hora = hora;
			this.profeId = profeId;
			this.moduloId = moduloId;
			this.aula = aula;
			this.observaciones = observaciones;
		}

		public Long getId() {
			return id;
		}

		public String getDia() {
			return dia;
		}

		public int getHora() {
			return hora;
		}

		public Long getProfeId() {
			return profeId;
		}

		public Long getModuloId() {
			return moduloId;
		}

		public String getAula() {
			return aula;
		}

		public String getObservaciones() {
			return observaciones;
		}

		@Override
		public String toString() {
			return "HorarioData{" + "id=" + id + ", dia='" + dia + '\'' + ", hora=" + hora + ", aula='" + aula + '\''
					+ '}';
		}
	}
}
