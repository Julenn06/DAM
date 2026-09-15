package com.elorrieta.elorserv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elorrieta.elorserv.model.Modulua;

@Repository
public interface ModuluaRepository extends JpaRepository<Modulua, Integer> {
	List<Modulua> findByZikloaId(Integer zikloaId);

	List<Modulua> findByZikloaIdAndCurso(Integer zikloaId, byte curso);
}
