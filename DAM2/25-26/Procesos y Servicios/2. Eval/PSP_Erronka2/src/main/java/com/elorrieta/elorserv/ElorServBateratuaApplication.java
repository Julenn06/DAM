package com.elorrieta.elorserv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.elorrieta.elorserv.repository.ErabiltzaileaRepository;
import com.elorrieta.elorserv.repository.MeetingRepository;
import com.elorrieta.elorserv.repository.OrdutegiaRepository;
import com.elorrieta.elorserv.socket.SocketZerbitzaria;

// Aplikazio nagusia - REST API (8080) eta Socket zerbitzaria (6000) kudeatzen ditu
// MySQL datu-basean oinarrituta: users, horarios, reuniones, modulos, ciclos, etc.
@SpringBootApplication
public class ElorServBateratuaApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(ElorServBateratuaApplication.class);

	@Autowired
	private SocketZerbitzaria socketZerbitzaria;

	@Autowired
	private ErabiltzaileaRepository erabiltzaileaRepository;

	@Autowired
	private OrdutegiaRepository ordutegiaRepository;

	@Autowired
	private MeetingRepository meetingRepository;

	public static void main(String[] args) {
		logger.info("╔══════════════════════════════════════════════════════════╗");
		logger.info("║   ELORSERV BATERATUA ABIARAZTEN - ZERBITZARI NAGUSIA     ║");
		logger.info("╚══════════════════════════════════════════════════════════╝");

		SpringApplication.run(ElorServBateratuaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("");
		logger.info("═══════════════════════════════════════════════════════════");
		logger.info("ELORSERV BATERATUA HASIERATZEN");
		logger.info("═══════════════════════════════════════════════════════════");

		logger.info("");
		logger.info("[1] TCP Socket zerbitzaria altxatzen...");
		socketZerbitzaria.abiarazi();

		logger.info("");
		logger.info("[2] MySQL datu-basea egiaztatzen...");
		egiaztatuDatuBasea();

		erakutsiInformazioa();
	}

	private void egiaztatuDatuBasea() {
		try {
			long erabiltzaileKop = erabiltzaileaRepository.count();
			long ordutegiaKop = ordutegiaRepository.count();
			long bileraKop = meetingRepository.count();

			logger.info("   ✓ MySQL konexioa ondo dago");
			logger.info("   ✓ Datu-basea: elordb");
			logger.info("   ✓ Erabiltzaileak: {}", erabiltzaileKop);
			logger.info("   ✓ Ordutegia sarrerak: {}", ordutegiaKop);
			logger.info("   ✓ Bilerak: {}", bileraKop);
		} catch (Exception e) {
			logger.warn("   ⚠ Errorea datu-basea egiaztatzen: {}", e.getMessage());
			logger.warn("   💡 Egiaztatu MySQL exekutatzen ari dela");
			logger.warn("   💡 Datu-basea: elordb");
			logger.warn("   💡 Erabiltzailea: eloruser");
		}
	}

	private void erakutsiInformazioa() {
		logger.info("✓ ELORSERV BATERATUA ONDO ABIARAZI DA");
		logger.info("═══════════════════════════════════════════════════════════");
		logger.info("");
		logger.info("ZERBITZUETARAKO SARBIDEA:");
		logger.info("");

		logger.info("▸ REST API (HTTP):");
		logger.info("   • GET http://localhost:8080/elorserv/api/ikastetxeak");
		logger.info("   • GET http://localhost:8080/elorserv/api/ikastetxeak/kopurua");
		logger.info("   • POST http://localhost:8080/elorserv/login");
		logger.info("   • GET http://localhost:8080/elorserv/users/getUsers");
		logger.info("   • GET http://localhost:8080/elorserv/horarios/getHorarios/{id}");
		logger.info("   • GET http://localhost:8080/elorserv/reuniones/getReuniones/{id}");
		logger.info("   • GET http://localhost:8080/elorserv/ciclos/getCiclos");
		logger.info("   • GET http://localhost:8080/elorserv/modulos/getModulos");
		logger.info("");

		logger.info("▸ SOCKET ZERBITZARIA (TCP - Sare Lokala):");
		logger.info("   • Konektatu: localhost:6000");
		logger.info("   • Komandoak:");
		logger.info("");
		logger.info("   LOGIN:");
		logger.info("      LOGIN:email:password");
		logger.info("");
		logger.info("   ORDUTEGIA:");
		logger.info("      LORTU_HORARIO:profe_id");
		logger.info("      LORTU_HORARIOS");
		logger.info("");
		logger.info("   ERABILTZAILEAK:");
		logger.info("      LORTU_USERS");
		logger.info("      LORTU_USER:id");
		logger.info("      LORTU_USERS_TIPO:tipo_id");
		logger.info("");
		logger.info("   BILERAK:");
		logger.info("      LORTU_REUNIONES");
		logger.info("      LORTU_REUNIONES_PROFE:profe_id");
		logger.info("      LORTU_REUNIONES_ALUMNO:alumno_id");
		logger.info("");
		logger.info("   ZIKLOAK:");
		logger.info("      LORTU_CICLOS");
		logger.info("      LORTU_CICLO:id");
		logger.info("");
		logger.info("   MODULUAK:");
		logger.info("      LORTU_MODULOS");
		logger.info("      LORTU_MODULO:id");
		logger.info("      LORTU_MODULOS_CICLO:ciclo_id");
		logger.info("");
		logger.info("   MATRIKULAZIOAK:");
		logger.info("      LORTU_MATRICULACIONES");
		logger.info("      LORTU_MATRICULACIONES_ALUMNO:alum_id");
		logger.info("");
		logger.info("   MOTAK:");
		logger.info("      LORTU_TIPOS");
		logger.info("      LORTU_TIPO:id");
		logger.info("");
		logger.info("   BESTE BATZUK:");
		logger.info("      LORTU_IKASTETXEAK (JSON fitxategia)");
		logger.info("      LORTU_EGOERA");
		logger.info("");

		logger.info("▸ DATU-BASEA (MySQL):");
		logger.info("   • Host: application.properties-en konfiguratuta");
		logger.info("   • Datu-basea: elordb");
		logger.info("   • Taulak: users, horarios, reuniones, modulos, ciclos...");
		logger.info("");
		logger.info("═══════════════════════════════════════════════════════════");
		logger.info("");
	}
}
