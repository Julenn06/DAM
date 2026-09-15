package com.elorrieta.elorserv.controller;

import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elorrieta.elorserv.model.Bilera;
import com.elorrieta.elorserv.model.BileraDTO;
import com.elorrieta.elorserv.model.Erabiltzailea;
import com.elorrieta.elorserv.service.BileraZerbitzua;

@RestController
@RequestMapping("/reuniones")
public class BileraKontrolatzailea {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private BileraZerbitzua bileraZerbitzua;

	@GetMapping("/getReuniones/{id}")
	public ResponseEntity<?> bilerakLortu(@PathVariable Integer id) {
		try {
			TypedQuery<Bilera> q = entityManager.createQuery("SELECT DISTINCT r FROM Bilera r "
					+ "LEFT JOIN FETCH r.ikaslea ikaslea " + "LEFT JOIN FETCH r.irakaslea irakaslea "
					+ "WHERE r.ikaslea.id = :id " + "OR r.irakaslea.id = :id " + "ORDER BY r.fecha DESC", Bilera.class);
			q.setParameter("id", id);
			List<Bilera> bilerak = q.getResultList();
			return ResponseEntity.ok(bilerak);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Errorea bilerak lortzean: " + e.getMessage());
		}
	}

	@PostMapping("/create")
	public ResponseEntity<?> bileraSortu(@RequestBody BileraDTO dto) {
		if (dto == null)
			return ResponseEntity.badRequest().body("Request body-a beharrezkoa da");

		if (dto.getIkasleaId() != null) {
			Erabiltzailea ikaslea = entityManager.find(Erabiltzailea.class, dto.getIkasleaId());
			if (ikaslea == null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Ikaslea ez da aurkitu: " + dto.getIkasleaId());
		}
		if (dto.getIrakasleaId() != null) {
			Erabiltzailea irakaslea = entityManager.find(Erabiltzailea.class, dto.getIrakasleaId());
			if (irakaslea == null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Irakaslea ez da aurkitu: " + dto.getIrakasleaId());
		}

		try {
			BileraDTO gordea = bileraZerbitzua.bileraGorde(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(gordea);
		} catch (IllegalArgumentException iae) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Zerbitzari-errorea: " + e.getMessage());
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> bileraEzabatu(@PathVariable Integer id) {
		try {
			bileraZerbitzua.bileraEzabatu(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException iae) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Zerbitzari-errorea: " + e.getMessage());
		}
	}

	@PutMapping("/updateEstado/{id}")
	public ResponseEntity<?> egoeraEguneratu(@PathVariable Integer id, @RequestBody Map<String, String> body) {
		try {
			if (body == null || !body.containsKey("estado")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("'estado' falta da eskaera body-an");
			}
			String egoera = body.get("estado");
			BileraDTO eguneratua = bileraZerbitzua.egoeraEguneratu(id, egoera);
			return ResponseEntity.ok(eguneratua);
		} catch (IllegalArgumentException iae) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Zerbitzari-errorea: " + e.getMessage());
		}
	}
}
