package com.elorrieta.elorserv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elorrieta.elorserv.model.Zikloa;

@Repository
public interface ZikloaRepository extends JpaRepository<Zikloa, Integer> {
	Optional<Zikloa> findByNombre(String nombre);
}
