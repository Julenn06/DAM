package com.elorserv.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.elorserv.model.Reuniones;
import com.elorserv.model.ReunionesDTO;
import com.elorserv.model.Users;

@Service
public class ReunionesService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public ReunionesDTO saveReunion(ReunionesDTO dto) {
        Objects.requireNonNull(dto, "dto is required");

        Reuniones r = new Reuniones();
        r.setTitulo(dto.getTitulo());
        r.setAsunto(dto.getAsunto());
        r.setAula(dto.getAula());

        // parse fecha String -> Timestamp (DTO.fecha is String)
        if (dto.getFecha() != null && !dto.getFecha().isEmpty()) {
            String s = dto.getFecha();
            try {
                Instant inst = Instant.parse(s);
                r.setFecha(Timestamp.from(inst));
            } catch (Exception ex) {
                try {
                    r.setFecha(Timestamp.valueOf(s));
                } catch (Exception ex2) {
                    throw new IllegalArgumentException("Invalid fecha format: " + s);
                }
            }
        } else {
            r.setFecha(null);
        }

        r.setEstado(dto.getEstado());
        r.setEstadoEus(dto.getEstadoEus());
        r.setIdCentro(dto.getIdCentro());

        Timestamp now = new Timestamp(System.currentTimeMillis());
        r.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : now);
        r.setUpdatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : now);

        if (dto.getAlumnoId() != null) {
            Users alumno = entityManager.find(Users.class, dto.getAlumnoId());
            if (alumno == null) throw new IllegalArgumentException("Alumno not found: " + dto.getAlumnoId());
            r.setUsersByAlumnoId(alumno);
        }

        if (dto.getProfesorId() != null) {
            Users profesor = entityManager.find(Users.class, dto.getProfesorId());
            if (profesor == null) throw new IllegalArgumentException("Profesor not found: " + dto.getProfesorId());
            r.setUsersByProfesorId(profesor);
        }

        try {
            entityManager.persist(r);
            entityManager.flush();
        } catch (PersistenceException pe) {
            // Inspect cause message for common MySQL truncation/enum errors and return a clear validation error
            String causeMsg = pe.getMessage();
            if (pe.getCause() != null && pe.getCause().getMessage() != null) {
                causeMsg = pe.getCause().getMessage();
            }
            if (causeMsg != null && causeMsg.contains("Data truncated for column 'estado_eus'")) {
                throw new IllegalArgumentException("Invalid value for estadoEus: " + dto.getEstadoEus());
            }
            // rethrow to let transaction handler propagate unexpected persistence errors
            throw pe;
        }

        ReunionesDTO saved = new ReunionesDTO(
                r.getIdReunion(),
                r.getTitulo(),
                r.getAsunto(),
                r.getAula(),
                r.getFecha() != null ? r.getFecha().toInstant().toString() : null,
                r.getEstado(),
                r.getEstadoEus(),
                r.getIdCentro(),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                r.getUsersByAlumnoId() != null ? r.getUsersByAlumnoId().getId() : null,
                r.getUsersByProfesorId() != null ? r.getUsersByProfesorId().getId() : null
        );

        return saved;
    }

    // new method: delete reunion by id
    @Transactional
    public void deleteReunion(Integer id) {
        if (id == null) throw new IllegalArgumentException("id is required");
        Reuniones r = entityManager.find(Reuniones.class, id);
        if (r == null) throw new IllegalArgumentException("Reunion not found: " + id);
        entityManager.remove(r);
        entityManager.flush();
    }

    // new method: update estado
    @Transactional
    public ReunionesDTO updateEstado(Integer id, String estado) {
        if (id == null) throw new IllegalArgumentException("id is required");
        if (estado == null || estado.trim().isEmpty()) throw new IllegalArgumentException("estado is required");

        String normalized = estado.trim();
        String[] allowed = {"aceptada", "denegada", "pendiente"};
        boolean ok = false;
        for (String a : allowed) if (a.equalsIgnoreCase(normalized)) { normalized = a; ok = true; break; }
        if (!ok) throw new IllegalArgumentException("Invalid estado. Allowed: onartzeke, onartuta, ezeztatuta, gatazka");

        Reuniones r = entityManager.find(Reuniones.class, id);
        if (r == null) throw new IllegalArgumentException("Reunion not found: " + id);

        r.setEstado(normalized);
        r.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        entityManager.merge(r);
        entityManager.flush();

        ReunionesDTO dto = new ReunionesDTO(
            r.getIdReunion(),
            r.getTitulo(),
            r.getAsunto(),
            r.getAula(),
            r.getFecha() != null ? r.getFecha().toInstant().toString() : null,
            r.getEstado(),
            r.getEstadoEus(),
            r.getIdCentro(),
            r.getCreatedAt(),
            r.getUpdatedAt(),
            r.getUsersByAlumnoId() != null ? r.getUsersByAlumnoId().getId() : null,
            r.getUsersByProfesorId() != null ? r.getUsersByProfesorId().getId() : null
        );

        return dto;
    }
}