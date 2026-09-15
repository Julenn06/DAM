package com.elorserv.controllers;

import com.elorserv.model.Ciclos;
import com.elorserv.model.Users;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@RestController
@RequestMapping("/ciclos")
public class CiclosController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/getCiclos")
    public ResponseEntity<?> getAllCiclos() {
        try {
            TypedQuery<Ciclos> q = entityManager.createQuery("SELECT c FROM Ciclos c", Ciclos.class);
            List<Ciclos> ciclos = q.getResultList();
            return ResponseEntity.ok(ciclos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<?> getUsersByCiclo(@PathVariable Integer id) {
        try {
            if (id == null) return ResponseEntity.badRequest().body("Ciclo id required");
            // Use JPQL on Matriculaciones to get distinct users for the given ciclo id
            TypedQuery<Users> q = entityManager.createQuery(
                "SELECT DISTINCT m.users FROM Matriculaciones m WHERE m.ciclos.id = :cicloId", Users.class);
            q.setParameter("cicloId", id);
            List<Users> users = q.getResultList();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

}