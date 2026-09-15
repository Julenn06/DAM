package com.elorrieta.elorserv.controller;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elorrieta.elorserv.model.Erabiltzailea;
import com.elorrieta.elorserv.model.Ordutegia;

@RestController
@RequestMapping("/users")
public class ErabiltzaileaKontrolatzailea {
	private static final int ALUMNO_MOTA_ID = 4;

	@PersistenceContext
	private EntityManager entityManager;

	@GetMapping("/getAlumnos")
	public ResponseEntity<?> ikasleakLortu() {
		try {
			TypedQuery<Erabiltzailea> q = entityManager
					.createQuery("SELECT u FROM Erabiltzailea u WHERE u.mota.id = :motaId", Erabiltzailea.class);
			q.setParameter("motaId", ALUMNO_MOTA_ID);
			List<Erabiltzailea> zerrenda = q.getResultList();
			return ResponseEntity.ok(zerrenda);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Zerbitzari-errorea: " + e.getMessage());
		}
	}

	@GetMapping("/getUsers")
	public ResponseEntity<?> erabiltzaileakLortu(@RequestParam Integer id) {
		try {
			TypedQuery<Erabiltzailea> q = entityManager.createQuery("SELECT u FROM Erabiltzailea u WHERE u.id != :id",
					Erabiltzailea.class);
			q.setParameter("id", id);
			List<Erabiltzailea> zerrenda = q.getResultList();
			return ResponseEntity.ok(zerrenda);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Zerbitzari-errorea: " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> erabiltzaileaLortuId(@PathVariable Integer id) {
		try {
			Erabiltzailea u = entityManager.find(Erabiltzailea.class, id);
			if (u == null)
				return ResponseEntity.status(404).body("Erabiltzailea ez da aurkitu: " + id);
			return ResponseEntity.ok(u);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Zerbitzari-errorea: " + e.getMessage());
		}
	}

	@GetMapping("/alumno/{id}/profesores")
	public ResponseEntity<?> irakasleLortuIkasleIdBidez(@PathVariable Integer id) {
		try {
			TypedQuery<Erabiltzailea> query = entityManager.createQuery(
					"SELECT DISTINCT h.erabiltzailea FROM Ordutegia h " + "JOIN h.modulua mo " + "JOIN mo.zikloa c "
							+ "JOIN Matrikulazioa ma ON ma.zikloa.id = c.id " + "WHERE ma.erabiltzailea.id = :id",
					Erabiltzailea.class);
			query.setParameter("id", id);
			List<Erabiltzailea> irakasleak = query.getResultList();
			return ResponseEntity.ok(irakasleak);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Errorea irakasleak lortzean: " + e.getMessage());
		}
	}

	@GetMapping("/profesor/{irakasleId}/alumnos")
	public ResponseEntity<?> ikasleLortuIrakasleIdBidez(@PathVariable Integer irakasleId) {
		try {
			TypedQuery<Erabiltzailea> query = entityManager.createQuery(
					"SELECT DISTINCT ma.erabiltzailea " + "FROM Matrikulazioa ma " + "JOIN ma.zikloa c "
							+ "JOIN c.moduluak mo " + "JOIN mo.ordutegia h " + "WHERE h.erabiltzailea.id = :irakasleId",
					Erabiltzailea.class);
			query.setParameter("irakasleId", irakasleId);
			List<Erabiltzailea> ikasleak = query.getResultList();

			return ResponseEntity.ok(ikasleak);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Zerbitzari-errorea: " + e.getMessage());
		}
	}

	@PostMapping("/updateProfilePic/{id}/{url}")
	@Transactional
	public ResponseEntity<?> argazkiaEguneratu(@PathVariable Integer id, @PathVariable String url) {
		try {
			Erabiltzailea erabiltzailea = entityManager.find(Erabiltzailea.class, id);

			if (erabiltzailea == null) {
				return ResponseEntity.status(404).body("Erabiltzailea ez da aurkitu id honekin: " + id);
			}

			erabiltzailea.setArgazkiaUrl(url);
			entityManager.merge(erabiltzailea);

			return ResponseEntity.ok("Profileko argazkia ondo eguneratu da");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Zerbitzari-errorea: " + e.getMessage());
		}
	}

	@GetMapping("/alumno/{id}/horario")
	public ResponseEntity<?> ordutegiaLortuIkasleId(@PathVariable Integer id) {
		try {
			TypedQuery<Integer> qZikloak = entityManager.createQuery(
					"SELECT DISTINCT mat.zikloa.id FROM Matrikulazioa mat WHERE mat.erabiltzailea.id = :uid",
					Integer.class);
			qZikloak.setParameter("uid", id);
			List<Integer> zikloIdak = qZikloak.getResultList();

			if (zikloIdak == null || zikloIdak.isEmpty()) {
				return ResponseEntity.ok(Collections.emptyList());
			}

			TypedQuery<Ordutegia> qOrdutegia = entityManager.createQuery(
					"SELECT DISTINCT h FROM Ordutegia h JOIN h.modulua m WHERE m.zikloa.id IN :zikloIdak",
					Ordutegia.class);
			qOrdutegia.setParameter("zikloIdak", zikloIdak);
			List<Ordutegia> ordutegia = qOrdutegia.getResultList();

			return ResponseEntity.ok(ordutegia);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Zerbitzari-errorea: " + e.getMessage());
		}
	}
}
