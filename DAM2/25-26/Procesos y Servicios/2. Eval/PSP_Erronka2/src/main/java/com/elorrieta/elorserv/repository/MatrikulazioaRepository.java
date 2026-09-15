package com.elorrieta.elorserv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elorrieta.elorserv.model.Matrikulazioa;

@Repository
public interface MatrikulazioaRepository extends JpaRepository<Matrikulazioa, Integer> {
	List<Matrikulazioa> findByErabiltzaileaId(Integer erabiltzaileaId);

	List<Matrikulazioa> findByZikloaId(Integer zikloaId);
}
