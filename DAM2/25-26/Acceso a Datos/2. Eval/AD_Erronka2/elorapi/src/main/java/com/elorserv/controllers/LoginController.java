package com.elorserv.controllers;

import java.util.Objects;

import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elorserv.model.*;
import com.elorserv.model.UserDTO;
import com.elorserv.requests.LoginRequest;

@RestController
@RequestMapping("/login")
public class LoginController {
    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            TypedQuery<Users> q = entityManager.createQuery(
                "SELECT u FROM Users u WHERE u.email = :param", Users.class);
            q.setParameter("param", loginRequest.getEmail());
            Users u = q.getSingleResult();

            if (u == null) {
                return ResponseEntity.status(404).body("Usuario no encontrado");
            }

            if (!Objects.equals(u.getPassword(), loginRequest.getPassword())) {
                return ResponseEntity.status(401).body("Contraseña incorrecta");
            }

            UserDTO dto = new UserDTO(
                u.getId(),
                u.getEmail(),
                u.getUsername(),
                u.getPassword(),
                u.getNombre(),
                u.getApellidos(),
                u.getTipos() != null ? u.getTipos().getId() : null,
                u.getDireccion(),
                u.getTelefono1(),
                u.getTelefono2()
            );

            // Store minimal info in session for subsequent requests
            session.setAttribute("userDto", dto);

            return ResponseEntity.ok(dto);
        } catch (NoResultException e) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error en el servidor: " + e.getMessage());
        }
    }
}