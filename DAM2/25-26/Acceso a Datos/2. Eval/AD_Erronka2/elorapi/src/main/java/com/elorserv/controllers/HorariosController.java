package com.elorserv.controllers;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elorserv.model.Horarios;

@RestController
@RequestMapping("/horarios")
public class HorariosController {

    @PersistenceContext
    private EntityManager entityManager;


    @GetMapping("/getHorarios/{id}")
    public ResponseEntity<?> getHorariosByUserId(@PathVariable Integer id) {
        try {
            List<Horarios> result = findByUserId(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }


    private List<Horarios> findByUserId(Integer id) throws Exception {
        try {
            TypedQuery<Horarios> q = entityManager.createQuery(
            		"SELECT DISTINCT h "
            		+ "FROM Horarios h "
            		+ "JOIN FETCH h.modulos mo "
            		+ "JOIN FETCH mo.ciclos c "
            		+ "WHERE h.users.id = :id "
            		+ "ORDER BY h.dia, h.hora", Horarios.class);
            q.setParameter("id", id);
            return q.getResultList(); // return even if empty
        } catch (IllegalArgumentException ex) {
            // invalid JPQL for this mapping, try next candidate
        }

        throw new Exception("Unable to build JPQL for Horarios by user id. Check Horarios entity mapping.");
    }
    
    @GetMapping("/getHorariosIkasle/{id}")
    public ResponseEntity<?> getHorariosFrom(@PathVariable Integer id) throws Exception{
    		try {
    			TypedQuery<Horarios> query = entityManager.createQuery(
    					"SELECT DISTINCT h "
    					+ "FROM Horarios h "
    					+ "JOIN FETCH h.modulos mo "
    					+ "JOIN FETCH mo.ciclos c "
    					+ "JOIN Matriculaciones ma ON ma.ciclos.id = c.id "
    					+ "WHERE ma.users.id = :id", Horarios.class);
    			
    			query.setParameter("id", id);
    			
    			List<Horarios> horarios = query.getResultList();
    			
    			return ResponseEntity.ok(horarios);
    		} catch (IllegalArgumentException ex) {
    			
    		}
    	throw new Exception("Unable to build JPQL for Horarios by student id. Check Horarios entity mapping");
    }
}