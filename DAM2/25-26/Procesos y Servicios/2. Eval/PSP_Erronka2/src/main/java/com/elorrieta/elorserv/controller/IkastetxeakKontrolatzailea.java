package com.elorrieta.elorserv.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elorrieta.elorserv.service.IkastetxeakZerbitzua;

@RestController
@RequestMapping("/api")
public class IkastetxeakKontrolatzailea {
	private static final Logger logger = LoggerFactory.getLogger(IkastetxeakKontrolatzailea.class);

	@Autowired
	private IkastetxeakZerbitzua ikastetxeakZerbitzua;

	@GetMapping("/ikastetxeak")
	public ResponseEntity<String> lortuIkastetxeak() {
		logger.info("GET /api/ikastetxeak eskaera jasota");

		try {
			String ikastetxeakJson = ikastetxeakZerbitzua.lortuIkastetxeakJson();
			logger.debug("JSON-aren {} karaktere itzultzen", ikastetxeakJson.length());

			return ResponseEntity.ok().header("Content-Type", "application/json").body(ikastetxeakJson);
		} catch (Exception e) {
			logger.error("Errorea ikastetxeak lortzean", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"errorea\": \"Errorea ikastetxeak irakurtzean\"}");
		}
	}

	@GetMapping("/ikastetxeak/kopurua")
	public ResponseEntity<String> lortuKopurua() {
		logger.info("GET /api/ikastetxeak/kopurua eskaera jasota");

		try {
			int kopurua = ikastetxeakZerbitzua.lortuIkastetxeKopurua();
			return ResponseEntity.ok("{\"kopurua\": " + kopurua + "}");
		} catch (Exception e) {
			logger.error("Errorea kopurua lortzean", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"errorea\": \"Errorea kopurua lortzean\"}");
		}
	}
}
