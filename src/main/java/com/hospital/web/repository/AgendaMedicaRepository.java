package com.hospital.web.repository;

import com.hospital.web.entity.AgendaMedica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaMedicaRepository extends JpaRepository<AgendaMedica, Integer> {

    List<AgendaMedica> findByEstadoHorario(String estadoHorario);

    List<AgendaMedica> findByMedicoIdMedicoAndEstadoHorario(
            Integer idMedico,
            String estadoHorario
    );

    List<AgendaMedica> findByMedicoEspecialidadIdEspecialidadAndEstadoHorario(
            Integer idEspecialidad,
            String estadoHorario
    );
}