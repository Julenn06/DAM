package com.elorrieta.elorserv.controller;

import java.util.Objects;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elorrieta.elorserv.model.Erabiltzailea;
import com.elorrieta.elorserv.model.ErabiltzaileaDTO;
import com.elorrieta.elorserv.model.SaioaHasieraEskaera;

@RestController
@RequestMapping("/login")
public class SaioaHasieraKontrolatzailea {
	@PersistenceContext
	private EntityManager entityManager;

	@PostMapping
	public ResponseEntity<?> saioaHasi(@RequestBody SaioaHasieraEskaera eskaera, HttpSession sesioa) {
		try {
			TypedQuery<Erabiltzailea> q = entityManager
					.createQuery("SELECT u FROM Erabiltzailea u WHERE u.email = :param", Erabiltzailea.class);
			q.setParameter("param", eskaera.getEmail());
			Erabiltzailea u = q.getSingleResult();

			if (u == null) {
				return ResponseEntity.status(404).body("Erabiltzailea ez da aurkitu");
			}

			if (!Objects.equals(u.getPassword(), eskaera.getPassword())) {
				return ResponseEntity.status(401).body("Pasahitza okerra");
			}

			ErabiltzaileaDTO dto = new ErabiltzaileaDTO(u.getId(), u.getEmail(), u.getUsername(), u.getPassword(),
					u.getNombre(), u.getApellidos(), u.getMota() != null ? u.getMota().getId() : null, u.getDireccion(),
					u.getTelefono1(), u.getTelefono2());

			sesioa.setAttribute("erabiltzaileaDto", dto);

			return ResponseEntity.ok(dto);
		} catch (NoResultException e) {
			return ResponseEntity.status(404).body("Erabiltzailea ez da aurkitu");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Zerbitzari-errorea: " + e.getMessage());
		}
	}
}
