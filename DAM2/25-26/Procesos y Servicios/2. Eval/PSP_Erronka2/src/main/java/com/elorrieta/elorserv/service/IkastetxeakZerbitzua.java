package com.elorrieta.elorserv.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@Service
public class IkastetxeakZerbitzua {
	private static final Logger logger = LoggerFactory.getLogger(IkastetxeakZerbitzua.class);
	private static final String FITXATEGI_IZENA = "ikastetxeak.json";

	private String ikastetxeakJson;
	private JsonArray ikastetxeakArray;

	@PostConstruct
	public void kargatu() {
		try {
			ClassPathResource resource = new ClassPathResource(FITXATEGI_IZENA);

			if (!resource.exists()) {
				logger.warn("Ez da {} fitxategia aurkitu classpath-ean", FITXATEGI_IZENA);
				ikastetxeakJson = "[]";
				ikastetxeakArray = new JsonArray();
				return;
			}

			try (InputStream is = resource.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
				ikastetxeakJson = reader.lines().collect(Collectors.joining("\n"));

				JsonElement jsonElement = JsonParser.parseString(ikastetxeakJson);
				if (jsonElement.isJsonObject()) {
					com.google.gson.JsonObject jsonObject = jsonElement.getAsJsonObject();
					if (jsonObject.has("CENTROS")) {
						ikastetxeakArray = jsonObject.getAsJsonArray("CENTROS");
					} else {
						logger.warn("JSON objektuak ez du 'CENTROS' gakoa");
						ikastetxeakArray = new JsonArray();
					}
				} else if (jsonElement.isJsonArray()) {
					ikastetxeakArray = jsonElement.getAsJsonArray();
				} else {
					logger.warn("JSON formatua ezezaguna");
					ikastetxeakArray = new JsonArray();
				}

				logger.info("✓ {} fitxategia kargatuta: {} ikastetxe", FITXATEGI_IZENA, ikastetxeakArray.size());
			}
		} catch (IOException e) {
			logger.error("Errorea {} fitxategia kargatzen", FITXATEGI_IZENA, e);
			ikastetxeakJson = "[]";
			ikastetxeakArray = new JsonArray();
		} catch (JsonSyntaxException e) {
			logger.error("Errorea JSON parseatzen: {}", FITXATEGI_IZENA, e);
			ikastetxeakJson = "[]";
			ikastetxeakArray = new JsonArray();
		}
	}

	public String lortuIkastetxeakJson() {
		if (ikastetxeakJson == null) {
			return "[]";
		}
		return ikastetxeakJson;
	}

	public int lortuIkastetxeKopurua() {
		if (ikastetxeakArray == null) {
			return 0;
		}
		return ikastetxeakArray.size();
	}
}
