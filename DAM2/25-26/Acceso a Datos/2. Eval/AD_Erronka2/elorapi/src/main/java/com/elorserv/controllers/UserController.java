package com.elorserv.controllers;

import java.util.List;
import java.util.Collections;

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

import com.elorserv.model.Users;
import com.elorserv.model.Horarios;

@RestController
@RequestMapping("/users")
public class UserController {

	@PersistenceContext
	private EntityManager entityManager;

	@GetMapping("/getAlumnos")
	public ResponseEntity<?> getAllStudents() {
		try {
			TypedQuery<Users> q = entityManager.createQuery("SELECT u FROM Users u WHERE u.tipos.id = :tipoid",
					Users.class);
			q.setParameter("tipoid", 4);
			List<Users> list = q.getResultList();
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Server error: " + e.getMessage());
		}
	}

	@GetMapping("/getUsers")
	public ResponseEntity<?> getAllUsers(@RequestParam Integer id) {
		try {
			TypedQuery<Users> q = entityManager.createQuery("SELECT u FROM Users u WHERE u.id != :id", Users.class);
			q.setParameter("id", id);
			List<Users> list = q.getResultList();
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Server error: " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Integer id) {
		try {
			Users u = entityManager.find(Users.class, id);
			if (u == null)
				return ResponseEntity.status(404).body("User not found: " + id);
			return ResponseEntity.ok(u);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Server error: " + e.getMessage());
		}
	}
	
	@GetMapping("/alumno/{id}/profesores")
	public ResponseEntity<?> getProfesoresFromAlumnos(@PathVariable Integer id) throws Exception {
		try {
			TypedQuery<Users> query = entityManager.createQuery(
					"SELECT DISTINCT h.users FROM Horarios h "
					+ "JOIN h.modulos mo "
					+ "JOIN mo.ciclos c "
					+ "JOIN Matriculaciones ma ON ma.ciclos.id = c.id "
					+ "WHERE ma.users.id = :id", Users.class);
			
			query.setParameter("id", id);
			List<Users> teachers = query.getResultList();
			
			return ResponseEntity.ok(teachers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		throw new Exception("Unable to build JPQL for Profesores by alumId. Check Horarios entity mapping.");
	}

	@GetMapping("/profesor/{profesorId}/alumnos")
	public ResponseEntity<?> getAlumnosFromProfesor(@PathVariable Integer profesorId) {
		try {
			TypedQuery<Users> query = entityManager.createQuery(
					"SELECT DISTINCT ma.users "
					+ "FROM Matriculaciones ma "
					+ "JOIN ma.ciclos c "
					+ "JOIN c.modulos mo "
					+ "JOIN mo.horarios h "
					+ "WHERE h.users.id = :profesorId", Users.class);
			query.setParameter("profesorId", profesorId);
			List<Users> students = query.getResultList();

			return ResponseEntity.ok(students);
		} catch (Exception e) {
			System.err.println("ERROR en getAlumnosFromProfesor: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(500).body("Server error: " + e.getMessage());
		}
	}

	@PostMapping("/updateProfilePic/{id}/{url}")
	@Transactional
	public ResponseEntity<?> updateProfilePic(@PathVariable Integer id, @PathVariable String url) {
		try {
			// Buscar el usuario
			Users user = entityManager.find(Users.class, id);

			if (user == null) {
				return ResponseEntity.status(404).body("Usuario no encontrado con id: " + id);
			}

			// Actualizar el campo argazkiaUrl
			user.setArgazkiaUrl(url);

			// Persistir el cambio
			entityManager.merge(user);

			System.out.println("Foto de perfil actualizada correctamente");

			return ResponseEntity.ok("Foto de perfil actualizada correctamente");
		} catch (Exception e) {
			System.err.println("ERROR al actualizar foto de perfil: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(500).body("Server error: " + e.getMessage());
		}
	}

    // New endpoint: obtener el horario de un alumno según el/los ciclo(s) en los que está matriculado
    @GetMapping("/alumno/{id}/horario")
    public ResponseEntity<?> getHorarioForAlumno(@PathVariable Integer id) {
        try {

            // Paso 1: Obtener los ciclos en los que está matriculado el alumno
            TypedQuery<Integer> qCiclos = entityManager.createQuery(
                "SELECT DISTINCT mat.ciclos.id FROM Matriculaciones mat WHERE mat.users.id = :uid",
                Integer.class
            );
            qCiclos.setParameter("uid", id);
            List<Integer> cicloIds = qCiclos.getResultList();

            System.out.println("Ciclos encontrados para alumno " + id + ": " + cicloIds);

            if (cicloIds == null || cicloIds.isEmpty()) {
                System.out.println("No hay matriculaciones para el alumno " + id);
                return ResponseEntity.ok(Collections.emptyList());
            }

            // Paso 2: Obtener los horarios de los módulos que pertenecen a esos ciclos
            TypedQuery<Horarios> qHorarios = entityManager.createQuery(
                "SELECT DISTINCT h FROM Horarios h JOIN h.modulos m WHERE m.ciclos.id IN :cicloIds",
                Horarios.class
            );
            qHorarios.setParameter("cicloIds", cicloIds);
            List<Horarios> horarios = qHorarios.getResultList();

            System.out.println("Entradas de horario encontradas: " + (horarios == null ? 0 : horarios.size()));

            return ResponseEntity.ok(horarios);
        } catch (Exception e) {
            System.err.println("ERROR en getHorarioForAlumno: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

}