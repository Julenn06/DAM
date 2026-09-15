package com.elorrieta.elorserv.service;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elorrieta.elorserv.model.Bilera;
import com.elorrieta.elorserv.model.BileraDTO;
import com.elorrieta.elorserv.model.Erabiltzailea;
import com.elorrieta.elorserv.repository.ErabiltzaileaRepository;
import com.elorrieta.elorserv.repository.MeetingRepository;

@Service
public class BileraZerbitzua {

	@Autowired
	private MeetingRepository meetingRepository;

	@Autowired
	private ErabiltzaileaRepository erabiltzaileaRepository;

	@Transactional
	public BileraDTO bileraGorde(BileraDTO dto) {
		Bilera bilera = new Bilera();

		if (dto.getIrakasleaId() != null) {
			Optional<Erabiltzailea> irakasleaOpt = erabiltzaileaRepository.findById(dto.getIrakasleaId());
			if (irakasleaOpt.isPresent()) {
				bilera.setIrakaslea(irakasleaOpt.get());
			} else {
				throw new IllegalArgumentException("Ez da irakaslea aurkitu ID honekin: " + dto.getIrakasleaId());
			}
		}

		if (dto.getIkasleaId() != null) {
			Optional<Erabiltzailea> ikasleaOpt = erabiltzaileaRepository.findById(dto.getIkasleaId());
			if (ikasleaOpt.isPresent()) {
				bilera.setIkaslea(ikasleaOpt.get());
			} else {
				throw new IllegalArgumentException("Ez da ikaslea aurkitu ID honekin: " + dto.getIkasleaId());
			}
		}

		bilera.setEstado(dto.getEstado());
		bilera.setEstadoEus(dto.getEstadoEus());
		bilera.setTitulo(dto.getTitulo());
		bilera.setAsunto(dto.getAsunto());
		bilera.setAula(dto.getAula());
		bilera.setFecha(stringToTimestamp(dto.getFecha()));
		bilera.setIdCentro(dto.getIdCentro());

		Bilera gordeta = meetingRepository.save(bilera);
		return bileraToDTO(gordeta);
	}

	@Transactional
	public void bileraEzabatu(Integer id) {
		if (!meetingRepository.existsById(id)) {
			throw new IllegalArgumentException("Ez da bilera aurkitu ID honekin: " + id);
		}

		meetingRepository.deleteById(id);
	}

	@Transactional
	public BileraDTO egoeraEguneratu(Integer id, String egoera) {
		Optional<Bilera> bileraOpt = meetingRepository.findById(id);

		if (!bileraOpt.isPresent()) {
			throw new IllegalArgumentException("Ez da bilera aurkitu ID honekin: " + id);
		}

		Bilera bilera = bileraOpt.get();
		bilera.setEstado(egoera);
		bilera.setEstadoEus(itzuliEgoera(egoera));

		Bilera eguneratua = meetingRepository.save(bilera);
		return bileraToDTO(eguneratua);
	}

	private BileraDTO bileraToDTO(Bilera bilera) {
		BileraDTO dto = new BileraDTO();
		dto.setIdBilera(bilera.getIdBilera());
		dto.setEstado(bilera.getEstado());
		dto.setEstadoEus(bilera.getEstadoEus());
		dto.setIrakasleaId(bilera.getIrakaslea() != null ? bilera.getIrakaslea().getId() : null);
		dto.setIkasleaId(bilera.getIkaslea() != null ? bilera.getIkaslea().getId() : null);
		dto.setIdCentro(bilera.getIdCentro());
		dto.setTitulo(bilera.getTitulo());
		dto.setAsunto(bilera.getAsunto());
		dto.setAula(bilera.getAula());
		dto.setFecha(bilera.getFecha() != null ? bilera.getFecha().toString() : null);
		return dto;
	}

	private Timestamp stringToTimestamp(String fechaStr) {
		if (fechaStr == null || fechaStr.isEmpty())
			return null;
		try {
			return Timestamp.valueOf(fechaStr);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	private String itzuliEgoera(String egoera) {
		if (egoera == null)
			return null;
		switch (egoera.toLowerCase()) {
		case "pendiente":
			return "Zain";
		case "confirmada":
			return "Baieztatua";
		case "cancelada":
			return "Bertan behera utzita";
		case "completada":
			return "Osatua";
		case "rechazada":
			return "Baztertua";
		default:
			return egoera;
		}
	}
}
