package com.hospital.web.repository;

import com.hospital.web.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {

    Optional<Especialidad> findByNombreEspecialidad(String nombreEspecialidad);
}