package com.hospital.web.repository;

import com.hospital.web.entity.RecetaMedica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecetaMedicaRepository extends JpaRepository<RecetaMedica, Integer> {

    Optional<RecetaMedica> findByAtencionIdAtencion(Integer idAtencion);

    List<RecetaMedica> findByEstadoReceta(String estadoReceta);

    boolean existsByAtencionIdAtencion(Integer idAtencion);
}