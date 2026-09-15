package com.elorrieta.elorserv.controller;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elorrieta.elorserv.model.Erabiltzailea;
import com.elorrieta.elorserv.model.Modulua;

@RestController
@RequestMapping("/modulos")
public class ModuluaKontrolatzailea {
	@PersistenceContext
	private EntityManager entityManager;

	@GetMapping("/getModulos")
	public ResponseEntity<?> moduluGuztiak() {
		try {
			TypedQuery<Modulua> q = entityManager.createQuery("SELECT m FROM Modulua m", Modulua.class);
			List<Modulua> moduluak = q.getResultList();
			return ResponseEntity.ok(moduluak);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Zerbitzari-errorea: " + e.getMessage());
		}
	}

	@GetMapping("/{id}/users")
	public ResponseEntity<?> erabiltzaileakModuluIdBidez(@PathVariable Integer id) {
		try {
			if (id == null)
				return ResponseEntity.badRequest().body("Modulua id beharrezkoa da");

			Modulua mo = entityManager.find(Modulua.class, id);
			if (mo == null)
				return ResponseEntity.status(404).body("Modulua ez da aurkitu: " + id);
			if (mo.getZikloa() == null)
				return ResponseEntity.ok(List.of());
			Integer zikloaId = mo.getZikloa().getId();

			TypedQuery<Erabiltzailea> q = entityManager.createQuery(
					"SELECT DISTINCT m.erabiltzailea FROM Matrikulazioa m WHERE m.zikloa.id = :zikloaId",
					Erabiltzailea.class);
			q.setParameter("zikloaId", zikloaId);
			List<Erabiltzailea> erabiltzaileak = q.getResultList();
			return ResponseEntity.ok(erabiltzaileak);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Zerbitzari-errorea: " + e.getMessage());
		}
	}
}
