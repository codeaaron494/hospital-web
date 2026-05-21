package com.hospital.web.repository;

import com.hospital.web.entity.CitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Integer> {

    List<CitaMedica> findByPacienteDniPaciente(String dniPaciente);

    List<CitaMedica> findByEstadoCita(String estadoCita);

    List<CitaMedica> findByPacienteIdPaciente(Integer idPaciente);

    boolean existsByAgendaIdAgenda(Integer idAgenda);
}