package com.elorrieta.elorserv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elorrieta.elorserv.model.Mota;

@Repository
public interface MotaRepository extends JpaRepository<Mota, Integer> {
}
