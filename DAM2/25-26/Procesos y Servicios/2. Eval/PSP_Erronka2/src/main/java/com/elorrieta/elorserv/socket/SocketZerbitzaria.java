package com.elorrieta.elorserv.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.elorrieta.elorserv.model.Bilera;
import com.elorrieta.elorserv.model.Erabiltzailea;
import com.elorrieta.elorserv.model.Matrikulazioa;
import com.elorrieta.elorserv.model.Modulua;
import com.elorrieta.elorserv.model.Mota;
import com.elorrieta.elorserv.model.Ordutegia;
import com.elorrieta.elorserv.model.Zikloa;
import com.elorrieta.elorserv.repository.ErabiltzaileaRepository;
import com.elorrieta.elorserv.repository.MatrikulazioaRepository;
import com.elorrieta.elorserv.repository.MeetingRepository;
import com.elorrieta.elorserv.repository.ModuluaRepository;
import com.elorrieta.elorserv.repository.MotaRepository;
import com.elorrieta.elorserv.repository.OrdutegiaRepository;
import com.elorrieta.elorserv.repository.ZikloaRepository;
import com.elorrieta.elorserv.service.IkastetxeakZerbitzua;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

// TCP Socket zerbitzaria (6000 ataka) - Datu-base osoko CRUD operazioak
// Komandoak: LOGIN, LORTU_HORARIO, LORTU_USERS, LORTU_REUNIONES, LORTU_CICLOS, etab.
@Service
public class SocketZerbitzaria {
	private static final Logger logger = LoggerFactory.getLogger(SocketZerbitzaria.class);

	@Value("${socket.server.port:6000}")
	private int ataka;

	@Autowired
	private IkastetxeakZerbitzua ikastetxeakZerbitzua;

	@Autowired
	private ErabiltzaileaRepository erabiltzaileaRepository;

	@Autowired
	private OrdutegiaRepository ordutegiaRepository;

	@Autowired
	private MeetingRepository meetingRepository;

	@Autowired
	private ZikloaRepository zikloaRepository;

	@Autowired
	private ModuluaRepository moduluaRepository;

	@Autowired
	private MatrikulazioaRepository matrikulazioaRepository;

	@Autowired
	private MotaRepository motaRepository;

	private ServerSocket zerbitzariSocket;
	private Thread zerbitzariHaria;
	private boolean aktiboa = false;
	private Gson gson = new Gson();

	public void abiarazi() {
		if (aktiboa) {
			logger.warn("Socket zerbitzaria dagoeneko aktibo dago");
			return;
		}

		zerbitzariHaria = new Thread(() -> {
			try {
				zerbitzariSocket = new ServerSocket(ataka);
				aktiboa = true;
				logger.info("✓ TCP Socket zerbitzaria abiarazi da {} atakan", ataka);
				logger.info("  (ElorES konexioak itxaroten - sare lokala)");
				logger.info("  (MySQL: users, horarios, reuniones, ciclos, modulos, matriculaciones, tipos)");

				while (aktiboa) {
					try {
						Socket bezeroSocket = zerbitzariSocket.accept();
						logger.info("Konexio berria: {}", bezeroSocket.getInetAddress().getHostAddress());
						new Thread(() -> prozesatuBezeroa(bezeroSocket)).start();
					} catch (IOException e) {
						if (aktiboa) {
							logger.error("Errorea konexioa onartzen", e);
						}
					}
				}
			} catch (IOException e) {
				logger.error("Errorea Socket zerbitzaria abiaraztean", e);
				aktiboa = false;
			}
		});

		zerbitzariHaria.setDaemon(true);
		zerbitzariHaria.start();
	}

	private void prozesatuBezeroa(Socket socket) {
		try (BufferedReader sarrera = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter irteera = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {
			String komandoa = sarrera.readLine();
			logger.debug("Komandoa jasota: {}", komandoa);

			if (komandoa == null || komandoa.trim().isEmpty()) {
				errorBidali(irteera, "Komando hutsa");
				return;
			}

			String erantzuna = prozesatuKomandoa(komandoa.trim());
			irteera.println(erantzuna);
		} catch (IOException e) {
			logger.error("Errorea bezeroa prozesatzen", e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				logger.error("Errorea socket-a ixten", e);
			}
		}
	}

	private String prozesatuKomandoa(String komandoa) {
		try {
			String[] zatiak = komandoa.split(":");

			switch (zatiak[0]) {
			case "LOGIN":
				if (zatiak.length < 3) {
					return errorBidaliBezero("Formatua: LOGIN:email:password");
				}
				return login(zatiak[1], zatiak[2]);

			case "LORTU_HORARIO":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_HORARIO:profe_id");
				}
				return lortuOrdutegia(Integer.parseInt(zatiak[1]));

			case "LORTU_HORARIOS":
				return lortuOrdutegiGuztiak();

			case "LORTU_USERS":
				return lortuErabiltzaileak();

			case "LORTU_USER":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_USER:id");
				}
				return lortuErabiltzailea(Integer.parseInt(zatiak[1]));

			case "EGUNERATU_USER":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: EGUNERATU_USER:jsonData");
				}
				// Reconstruct the full JSON string (may contain : characters)
				String jsonData = komandoa.substring("EGUNERATU_USER:".length());
				return eguneratuErabiltzailea(jsonData);

			case "LORTU_USERS_TIPO":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_USERS_TIPO:tipo_id");
				}
				return lortuErabiltzaileakMotaz(Integer.parseInt(zatiak[1]));

			case "LORTU_ALUMNOS_PROFE":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_ALUMNOS_PROFE:profe_id");
				}
				return lortuAlumnoakIrakasleIdBidez(Integer.parseInt(zatiak[1]));

			case "LORTU_REUNIONES":
				return lortuBilerak();

			case "LORTU_REUNIONES_PROFE":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_REUNIONES_PROFE:profe_id");
				}
				return lortuBilerakIrakasleIdBidez(Integer.parseInt(zatiak[1]));

			case "LORTU_REUNIONES_ALUMNO":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_REUNIONES_ALUMNO:alumno_id");
				}
				return lortuBilerakIkasleIdBidez(Integer.parseInt(zatiak[1]));

			case "LORTU_CICLOS":
				return lortuZikloak();

			case "LORTU_CICLO":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_CICLO:id");
				}
				return lortuZikloa(Integer.parseInt(zatiak[1]));

			case "LORTU_CICLO_NOMBRE":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_CICLO_NOMBRE:nombre");
				}
				return lortuZikloaIzenez(zatiak[1]);

			case "LORTU_MODULOS":
				return lortuModuluak();

			case "LORTU_MODULO":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_MODULO:id");
				}
				return lortuModulua(Integer.parseInt(zatiak[1]));

			case "LORTU_MODULOS_CICLO":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_MODULOS_CICLO:ciclo_id");
				}
				return lortuModuluakZikloaz(Integer.parseInt(zatiak[1]));

			case "LORTU_MODULOS_CICLO_CURSO":
				if (zatiak.length < 3) {
					return errorBidaliBezero("Formatua: LORTU_MODULOS_CICLO_CURSO:ciclo_id:curso");
				}
				return lortuModuluakZikloaKursuz(Integer.parseInt(zatiak[1]), Byte.parseByte(zatiak[2]));

			case "LORTU_MATRICULACIONES":
				return lortuMatrikulazioak();

			case "LORTU_MATRICULACIONES_ALUMNO":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_MATRICULACIONES_ALUMNO:alum_id");
				}
				return lortuMatrikulazioakIkasleIdBidez(Integer.parseInt(zatiak[1]));

			case "LORTU_MATRICULACIONES_CICLO":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_MATRICULACIONES_CICLO:ciclo_id");
				}
				return lortuMatrikulazioakZikloIdBidez(Integer.parseInt(zatiak[1]));

			case "LORTU_TIPOS":
				return lortuMotak();

			case "LORTU_TIPO":
				if (zatiak.length < 2) {
					return errorBidaliBezero("Formatua: LORTU_TIPO:id");
				}
				return lortuMota(Integer.parseInt(zatiak[1]));

			case "LORTU_IKASTETXEAK":
				return lortuIkastetxeak();

			case "LORTU_EGOERA":
				return lortuEgoera();

			default:
				logger.warn("Komando ezezaguna: {}", komandoa);
				return errorBidaliBezero("Komandoa ez da ezagutzen: " + zatiak[0]);
			}
		} catch (NumberFormatException e) {
			return errorBidaliBezero("ID-a zenbaki bat izan behar da");
		} catch (Exception e) {
			logger.error("Errorea komandoa prozesatzen: {}", komandoa, e);
			return errorBidaliBezero("Barne-errorea: " + e.getMessage());
		}
	}

	private String login(String email, String password) {
		try {
			Optional<Erabiltzailea> erabiltzaileaOpt = erabiltzaileaRepository.findByEmailAndPassword(email, password);
			JsonObject erantzuna = new JsonObject();

			if (erabiltzaileaOpt.isPresent()) {
				Erabiltzailea u = erabiltzaileaOpt.get();
				
				// Verificar que el usuario es profesor (tipo_id = 3)
				if (u.getMota() == null || u.getMota().getId() != 3) {
					erantzuna.addProperty("arrakasta", false);
					erantzuna.addProperty("mezua", "Sarbidea ukatua. Irakasleek bakarrik sar daitezke.");
					logger.warn("LOGIN ukatua (ez da irakaslea): {} (tipo_id={})", u.getEmail(), 
							u.getMota() != null ? u.getMota().getId() : "null");
					return gson.toJson(erantzuna);
				}
				
				erantzuna.addProperty("arrakasta", true);
				erantzuna.addProperty("mezua", "Login arrakastatsua");
				erantzuna.add("erabiltzailea", erabiltzaileaToJson(u));
				logger.info("LOGIN arrakastatsua (irakaslea): {} ({})", u.getEmail(), u.getId());
				String jsonResponse = gson.toJson(erantzuna);
				logger.debug("LOGIN JSON erantzuna: {}", jsonResponse);
				return jsonResponse;
			} else {
				erantzuna.addProperty("arrakasta", false);
				erantzuna.addProperty("mezua", "Email edo password okerra");
				logger.warn("LOGIN huts egin du: {}", email);
			}
			return gson.toJson(erantzuna);
		} catch (Exception e) {
			logger.error("Errorea login-ean", e);
			return errorBidaliBezero("Errorea login-ean");
		}
	}

	private String lortuOrdutegia(Integer irakasleId) {
		return lortuZerrenda(ordutegiaRepository.findByErabiltzaileaId(irakasleId), this::ordutegiaToJson, "horarios", "Errorea ordutegia lortzean");
	}

	private String lortuOrdutegiGuztiak() {
		return lortuZerrenda(ordutegiaRepository.findAll(), this::ordutegiaToJson, "horarios", "Errorea ordutegia lortzean");
	}

	private JsonObject ordutegiaToJson(Ordutegia h) {
		JsonObject obj = new JsonObject();
		obj.addProperty("id", h.getId());
		obj.addProperty("dia", h.getDia());
		obj.addProperty("hora", h.getHora());
		obj.addProperty("profeId", h.getErabiltzailea() != null ? h.getErabiltzailea().getId() : null);
		obj.addProperty("moduloId", h.getModulua() != null ? h.getModulua().getId() : null);
		obj.addProperty("aula", h.getAula());
		obj.addProperty("observaciones", h.getObservaciones());
		return obj;
	}

	private String lortuErabiltzaileak() {
		return lortuZerrenda(erabiltzaileaRepository.findAll(), this::erabiltzaileaToJson, "users", "Errorea erabiltzaileak lortzean");
	}

	private String lortuErabiltzailea(@NonNull Integer id) {
		return lortuElementua(erabiltzaileaRepository.findById(id), this::erabiltzaileaToJson, "user", "Ez da erabiltzailerik aurkitu ID honekin: " + id);
	}

	private String lortuErabiltzaileakMotaz(Integer motaId) {
		return lortuZerrenda(erabiltzaileaRepository.findByMotaId(motaId), this::erabiltzaileaToJson, "users", "Errorea erabiltzaileak lortzean");
	}

	private String lortuAlumnoakIrakasleIdBidez(Integer irakasleId) {
		return lortuZerrenda(erabiltzaileaRepository.findAlumnosByProfesorId(irakasleId), this::erabiltzaileaToJson, "users", "Errorea alumnoak lortzean");
	}

	private JsonObject erabiltzaileaToJson(Erabiltzailea u) {
		JsonObject obj = new JsonObject();
		obj.addProperty("id", u.getId());
		obj.addProperty("email", u.getEmail() != null ? u.getEmail() : "");
		obj.addProperty("username", u.getUsername() != null ? u.getUsername() : "");
		obj.addProperty("password", u.getPassword() != null ? u.getPassword() : "");
		obj.addProperty("nombre", u.getNombre() != null ? u.getNombre() : "");
		obj.addProperty("apellidos", u.getApellidos() != null ? u.getApellidos() : "");
		obj.addProperty("dni", u.getDni() != null ? u.getDni() : "");
		obj.addProperty("direccion", u.getDireccion() != null ? u.getDireccion() : "");
		obj.addProperty("telefono1", u.getTelefono1() != null ? u.getTelefono1() : "");
		obj.addProperty("telefono2", u.getTelefono2() != null ? u.getTelefono2() : "");
		obj.addProperty("tipoId", u.getMota() != null ? u.getMota().getId() : null);
		obj.addProperty("argazkiaUrl", u.getArgazkiaUrl() != null ? u.getArgazkiaUrl() : "");
		return obj;
	}

	private String eguneratuErabiltzailea(String jsonData) {
		try {
			JsonObject userData = gson.fromJson(jsonData, JsonObject.class);
			JsonObject erantzuna = new JsonObject();

			if (!userData.has("id")) {
				return errorBidaliBezero("Erabiltzaile ID-a beharrezkoa da");
			}

			Integer id = userData.get("id").getAsInt();
			Optional<Erabiltzailea> erabiltzaileaOpt = erabiltzaileaRepository.findById(id);

			if (erabiltzaileaOpt.isEmpty()) {
				erantzuna.addProperty("arrakasta", false);
				erantzuna.addProperty("mezua", "Ez da erabiltzailerik aurkitu ID honekin: " + id);
				return gson.toJson(erantzuna);
			}

			Erabiltzailea u = erabiltzaileaOpt.get();

			// Update only the fields that are provided and allowed to change
			if (userData.has("nombre") && !userData.get("nombre").isJsonNull()) {
				u.setNombre(userData.get("nombre").getAsString());
			}
			if (userData.has("apellidos") && !userData.get("apellidos").isJsonNull()) {
				u.setApellidos(userData.get("apellidos").getAsString());
			}
			if (userData.has("dni") && !userData.get("dni").isJsonNull()) {
				u.setDni(userData.get("dni").getAsString());
			}
			if (userData.has("direccion") && !userData.get("direccion").isJsonNull()) {
				u.setDireccion(userData.get("direccion").getAsString());
			}
			if (userData.has("telefono1") && !userData.get("telefono1").isJsonNull()) {
				u.setTelefono1(userData.get("telefono1").getAsString());
			}
			if (userData.has("telefono2") && !userData.get("telefono2").isJsonNull()) {
				u.setTelefono2(userData.get("telefono2").getAsString());
			}

			erabiltzaileaRepository.save(u);
			
			erantzuna.addProperty("arrakasta", true);
			erantzuna.addProperty("mezua", "Erabiltzailea eguneratua");
			erantzuna.add("erabiltzailea", erabiltzaileaToJson(u));
			
			logger.info("Erabiltzailea eguneratu da: {} ({})", u.getEmail(), u.getId());
			return gson.toJson(erantzuna);

		} catch (Exception e) {
			logger.error("Errorea erabiltzailea eguneratzean", e);
			return errorBidaliBezero("Errorea erabiltzailea eguneratzean: " + e.getMessage());
		}
	}

	private String lortuBilerak() {
		logger.debug("Lortu bilera guztiak eskaera jasota");
		String erantzuna = lortuZerrenda(meetingRepository.findAll(), this::bileraToJson, "reuniones", "Errorea bilerak lortzean");
		logger.debug("Bilerak JSON erantzuna: {}", erantzuna);
		return erantzuna;
	}

	private String lortuBilerakIrakasleIdBidez(Integer irakasleId) {
		return lortuZerrenda(meetingRepository.findByIrakasleaId(irakasleId), this::bileraToJson, "reuniones", "Errorea bilerak lortzean");
	}

	private String lortuBilerakIkasleIdBidez(Integer ikasleId) {
		return lortuZerrenda(meetingRepository.findByIkasleaId(ikasleId), this::bileraToJson, "reuniones", "Errorea bilerak lortzean");
	}

	private JsonObject bileraToJson(Bilera r) {
		JsonObject obj = new JsonObject();
		
		// idReunion es obligatorio
		if (r.getIdBilera() == null) {
			logger.warn("Bilera sin ID encontrada, se omitirá");
			return null;
		}
		
		obj.addProperty("idReunion", r.getIdBilera().longValue());
		obj.addProperty("estado", r.getEstado() != null ? r.getEstado() : "");
		obj.addProperty("estadoEus", r.getEstadoEus() != null ? r.getEstadoEus() : "");
		obj.addProperty("profesorId", r.getIrakaslea() != null ? r.getIrakaslea().getId() : null);
		obj.addProperty("alumnoId", r.getIkaslea() != null ? r.getIkaslea().getId() : null);
		obj.addProperty("idCentro", r.getIdCentro() != null ? r.getIdCentro() : "");
		obj.addProperty("titulo", r.getTitulo() != null ? r.getTitulo() : "");
		obj.addProperty("asunto", r.getAsunto() != null ? r.getAsunto() : "");
		obj.addProperty("aula", r.getAula() != null ? r.getAula() : "");
		obj.addProperty("fecha", r.getFecha() != null ? r.getFecha().toString() : "");
		return obj;
	}

	private String lortuZikloak() {
		return lortuZerrenda(zikloaRepository.findAll(), this::zikloaToJson, "ciclos", "Errorea zikloak lortzean");
	}

	private String lortuZikloa(@NonNull Integer id) {
		return lortuElementua(zikloaRepository.findById(id), this::zikloaToJson, "ciclo", "Ez da ziklorik aurkitu ID honekin: " + id);
	}

	private String lortuZikloaIzenez(String izena) {
		return lortuElementua(zikloaRepository.findByNombre(izena), this::zikloaToJson, "ciclo", "Ez da ziklorik aurkitu izen honekin: " + izena);
	}

	private JsonObject zikloaToJson(Zikloa c) {
		JsonObject obj = new JsonObject();
		obj.addProperty("id", c.getId());
		obj.addProperty("nombre", c.getNombre());
		return obj;
	}

	private String lortuModuluak() {
		return lortuZerrenda(moduluaRepository.findAll(), this::moduluaToJson, "modulos", "Errorea moduluak lortzean");
	}

	private String lortuModulua(@NonNull Integer id) {
		return lortuElementua(moduluaRepository.findById(id), this::moduluaToJson, "modulo", "Ez da modulurik aurkitu ID honekin: " + id);
	}

	private String lortuModuluakZikloaz(Integer zikloaId) {
		return lortuZerrenda(moduluaRepository.findByZikloaId(zikloaId), this::moduluaToJson, "modulos", "Errorea moduluak lortzean");
	}

	private String lortuModuluakZikloaKursuz(Integer zikloaId, byte kursua) {
		return lortuZerrenda(moduluaRepository.findByZikloaIdAndCurso(zikloaId, kursua), this::moduluaToJson, "modulos", "Errorea moduluak lortzean");
	}

	private JsonObject moduluaToJson(Modulua m) {
		JsonObject obj = new JsonObject();
		obj.addProperty("id", m.getId());
		obj.addProperty("nombre", m.getNombre());
		obj.addProperty("nombreEus", m.getNombreEus());
		obj.addProperty("horas", m.getHoras());
		obj.addProperty("cicloId", m.getZikloa() != null ? m.getZikloa().getId() : null);
		obj.addProperty("curso", m.getCurso());
		return obj;
	}

	private String lortuMatrikulazioak() {
		return lortuZerrenda(matrikulazioaRepository.findAll(), this::matrikulazioaToJson, "matriculaciones", "Errorea matrikulazioak lortzean");
	}

	private String lortuMatrikulazioakIkasleIdBidez(Integer ikasleId) {
		return lortuZerrenda(matrikulazioaRepository.findByErabiltzaileaId(ikasleId), this::matrikulazioaToJson, "matriculaciones", "Errorea matrikulazioak lortzean");
	}

	private String lortuMatrikulazioakZikloIdBidez(Integer zikloaId) {
		return lortuZerrenda(matrikulazioaRepository.findByZikloaId(zikloaId), this::matrikulazioaToJson, "matriculaciones", "Errorea matrikulazioak lortzean");
	}

	private JsonObject matrikulazioaToJson(Matrikulazioa m) {
		JsonObject obj = new JsonObject();
		obj.addProperty("id", m.getId());
		obj.addProperty("alumId", m.getErabiltzailea() != null ? m.getErabiltzailea().getId() : null);
		obj.addProperty("cicloId", m.getZikloa() != null ? m.getZikloa().getId() : null);
		obj.addProperty("curso", m.getCurso());
		obj.addProperty("fecha", m.getFecha() != null ? m.getFecha().toString() : null);
		return obj;
	}

	private String lortuMotak() {
		return lortuZerrenda(motaRepository.findAll(), this::motaToJson, "tipos", "Errorea motak lortzean");
	}

	private String lortuMota(@NonNull Integer id) {
		return lortuElementua(motaRepository.findById(id), this::motaToJson, "tipo", "Ez da motarik aurkitu ID honekin: " + id);
	}

	private JsonObject motaToJson(Mota t) {
		JsonObject obj = new JsonObject();
		obj.addProperty("id", t.getId());
		obj.addProperty("name", t.getName());
		obj.addProperty("nameEu", t.getNameEu());
		return obj;
	}

	private String lortuIkastetxeak() {
		String ikastetxeakJson = ikastetxeakZerbitzua.lortuIkastetxeakJson();
		logger.debug("Ikastetxeak bidalita");
		return ikastetxeakJson;
	}

	private String lortuEgoera() {
		JsonObject egoera = new JsonObject();
		egoera.addProperty("egoera", "MARTXAN");
		egoera.addProperty("zerbitzaria", "ElorServ Bateratua v2.0");
		egoera.addProperty("datu-basea", "MySQL - elordb");
		egoera.addProperty("ataka", ataka);

		try {
			long erabiltzaileKop = erabiltzaileaRepository.count();
			long ordutegiaKop = ordutegiaRepository.count();
			long bileraKop = meetingRepository.count();
			long zikloaKop = zikloaRepository.count();
			long moduluaKop = moduluaRepository.count();
			long matrikulazioaKop = matrikulazioaRepository.count();
			long motaKop = motaRepository.count();

			egoera.addProperty("users", erabiltzaileKop);
			egoera.addProperty("horarios", ordutegiaKop);
			egoera.addProperty("reuniones", bileraKop);
			egoera.addProperty("ciclos", zikloaKop);
			egoera.addProperty("modulos", moduluaKop);
			egoera.addProperty("matriculaciones", matrikulazioaKop);
			egoera.addProperty("tipos", motaKop);
		} catch (Exception e) {
			egoera.addProperty("bd-errorea", e.getMessage());
		}

		return gson.toJson(egoera);
	}

	private <T> String lortuZerrenda(List<T> zerrenda, java.util.function.Function<T, JsonObject> converter, String gakoa, String errorMezua) {
		try {
			JsonObject erantzuna = new JsonObject();
			JsonArray array = new JsonArray();
			zerrenda.forEach(item -> {
				JsonObject json = converter.apply(item);
				if (json != null) {
					array.add(json);
				}
			});
			erantzuna.addProperty("kopurua", array.size());
			erantzuna.add(gakoa, array);
			return gson.toJson(erantzuna);
		} catch (Exception e) {
			logger.error(errorMezua, e);
			return errorBidaliBezero(errorMezua);
		}
	}

	private <T> String lortuElementua(Optional<T> optional, java.util.function.Function<T, JsonObject> converter, String gakoa, String errorMezua) {
		try {
			JsonObject erantzuna = new JsonObject();
			if (optional.isPresent()) {
				erantzuna.addProperty("aurkituta", true);
				erantzuna.add(gakoa, converter.apply(optional.get()));
			} else {
				erantzuna.addProperty("aurkituta", false);
				erantzuna.addProperty("mezua", errorMezua);
			}
			return gson.toJson(erantzuna);
		} catch (Exception e) {
			logger.error(errorMezua, e);
			return errorBidaliBezero(errorMezua);
		}
	}

	private String errorBidaliBezero(String mezua) {
		JsonObject obj = new JsonObject();
		obj.addProperty("errorea", mezua);
		return gson.toJson(obj);
	}

	private void errorBidali(PrintWriter irteera, String mezua) {
		irteera.println(errorBidaliBezero(mezua));
	}

	public void gelditu() {
		logger.info("Socket zerbitzaria gelditzen...");
		aktiboa = false;

		try {
			if (zerbitzariSocket != null && !zerbitzariSocket.isClosed()) {
				zerbitzariSocket.close();
			}
		} catch (IOException e) {
			logger.error("Errorea socket-a ixten", e);
		}

		if (zerbitzariHaria != null) {
			zerbitzariHaria.interrupt();
		}

		logger.info("Socket zerbitzaria geldituta");
	}
}

