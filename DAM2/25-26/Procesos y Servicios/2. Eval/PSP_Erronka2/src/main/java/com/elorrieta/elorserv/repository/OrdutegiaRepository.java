package com.elorrieta.elorserv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elorrieta.elorserv.model.Ordutegia;

@Repository
public interface OrdutegiaRepository extends JpaRepository<Ordutegia, Integer> {
	List<Ordutegia> findByErabiltzaileaId(Integer erabiltzaileaId);
}
