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

import com.elorrieta.elorserv.model.Ordutegia;

@RestController
@RequestMapping("/horarios")
public class OrdutegiaKontrolatzailea {
	@PersistenceContext
	private EntityManager entityManager;

	@GetMapping("/getHorarios/{id}")
	public ResponseEntity<?> ordutegiaLortuErabiltzaileId(@PathVariable Integer id) {
		try {
			List<Ordutegia> emaitza = lortuErabiltzaileIdBidez(id);
			return ResponseEntity.ok(emaitza);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Zerbitzari-errorea: " + e.getMessage());
		}
	}

	private List<Ordutegia> lortuErabiltzaileIdBidez(Integer id) {
		TypedQuery<Ordutegia> q = entityManager.createQuery(
				"SELECT DISTINCT h " + "FROM Ordutegia h " + "JOIN FETCH h.modulua mo " + "JOIN FETCH mo.zikloa c "
						+ "WHERE h.erabiltzailea.id = :id " + "ORDER BY h.dia, h.hora",
				Ordutegia.class);
		q.setParameter("id", id);
		return q.getResultList();
	}

	@GetMapping("/getHorariosIkasle/{id}")
	public ResponseEntity<?> ordutegiaLortuIkasleIdBidez(@PathVariable Integer id) {
		try {
			TypedQuery<Ordutegia> query = entityManager.createQuery(
					"SELECT DISTINCT h " + "FROM Ordutegia h " + "JOIN FETCH h.modulua mo " + "JOIN FETCH mo.zikloa c "
							+ "JOIN Matrikulazioa ma ON ma.zikloa.id = c.id " + "WHERE ma.erabiltzailea.id = :id",
					Ordutegia.class);
			query.setParameter("id", id);
			List<Ordutegia> ordutegia = query.getResultList();
			return ResponseEntity.ok(ordutegia);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Errorea ordutegia lortzean: " + e.getMessage());
		}
	}
}
