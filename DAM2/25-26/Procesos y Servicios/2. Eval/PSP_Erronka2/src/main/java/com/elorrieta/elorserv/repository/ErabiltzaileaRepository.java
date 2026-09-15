package com.elorrieta.elorserv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elorrieta.elorserv.model.Erabiltzailea;

@Repository
public interface ErabiltzaileaRepository extends JpaRepository<Erabiltzailea, Integer> {
	Optional<Erabiltzailea> findByEmailAndPassword(String email, String password);

	List<Erabiltzailea> findByMotaId(Integer motaId);
	
	// Obtener alumnos de un profesor (basado en los ciclos de los módulos que imparte)
	@Query("SELECT DISTINCT u FROM Erabiltzailea u " +
	       "JOIN Matrikulazioa m ON m.erabiltzailea.id = u.id " +
	       "JOIN Modulua mod ON mod.zikloa.id = m.zikloa.id " +
	       "JOIN Ordutegia h ON h.modulua.id = mod.id " +
	       "WHERE h.erabiltzailea.id = :profeId AND u.mota.id = 4")
	List<Erabiltzailea> findAlumnosByProfesorId(@Param("profeId") Integer profeId);
}

