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
import com.elorrieta.elorserv.model.Zikloa;

@RestController
@RequestMapping("/ciclos")
public class ZikloaKontrolatzailea {
	@PersistenceContext
	private EntityManager entityManager;

	@GetMapping("/getCiclos")
	public ResponseEntity<?> zikloGuztiak() {
		try {
			TypedQuery<Zikloa> q = entityManager.createQuery("SELECT c FROM Zikloa c", Zikloa.class);
			List<Zikloa> zikloak = q.getResultList();
			return ResponseEntity.ok(zikloak);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Zerbitzari-errorea: " + e.getMessage());
		}
	}

	@GetMapping("/{id}/users")
	public ResponseEntity<?> erabiltzaileakZikloIdBidez(@PathVariable Integer id) {
		try {
			if (id == null)
				return ResponseEntity.badRequest().body("Zikloa id beharrezkoa da");

			TypedQuery<Erabiltzailea> q = entityManager.createQuery(
					"SELECT DISTINCT m.erabiltzailea FROM Matrikulazioa m WHERE m.zikloa.id = :zikloaId",
					Erabiltzailea.class);
			q.setParameter("zikloaId", id);
			List<Erabiltzailea> erabiltzaileak = q.getResultList();
			return ResponseEntity.ok(erabiltzaileak);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Zerbitzari-errorea: " + e.getMessage());
		}
	}
}
