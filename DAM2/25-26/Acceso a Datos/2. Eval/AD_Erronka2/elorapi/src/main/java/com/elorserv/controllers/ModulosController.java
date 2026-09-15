package com.elorserv.controllers;

import com.elorserv.model.Modulos;
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
@RequestMapping("/modulos")
public class ModulosController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/getModulos")
    public ResponseEntity<?> getAllModulos() {
        try {
            TypedQuery<Modulos> q = entityManager.createQuery("SELECT m FROM Modulos m", Modulos.class);
            List<Modulos> modulos = q.getResultList();
            return ResponseEntity.ok(modulos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<?> getUsersByModulo(@PathVariable Integer id) {
        try {
            if (id == null) return ResponseEntity.badRequest().body("Module id required");

            // find module and its ciclo
            Modulos mo = entityManager.find(Modulos.class, id);
            if (mo == null) return ResponseEntity.status(404).body("Modulo not found: " + id);
            if (mo.getCiclos() == null) return ResponseEntity.ok(List.of());
            Integer cicloId = mo.getCiclos().getId();

            // JPQL: matriculaciones link users to ciclos; return distinct users enrolled in that ciclo
            TypedQuery<Users> q = entityManager.createQuery(
                "SELECT DISTINCT m.users FROM Matriculaciones m WHERE m.ciclos.id = :cicloId", Users.class);
            q.setParameter("cicloId", cicloId);
            List<Users> users = q.getResultList();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

}