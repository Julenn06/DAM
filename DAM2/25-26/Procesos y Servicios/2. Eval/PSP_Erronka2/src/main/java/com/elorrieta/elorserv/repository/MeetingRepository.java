package com.elorrieta.elorserv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elorrieta.elorserv.model.Bilera;

@Repository
public interface MeetingRepository extends JpaRepository<Bilera, Integer> {
	List<Bilera> findByIrakasleaId(Integer irakasleaId);

	List<Bilera> findByIkasleaId(Integer ikasleaId);
}
