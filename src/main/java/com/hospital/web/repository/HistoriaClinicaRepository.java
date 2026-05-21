package com.hospital.web.repository;

import com.hospital.web.entity.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Integer> {

    Optional<HistoriaClinica> findByPacienteIdPaciente(Integer idPaciente);

    Optional<HistoriaClinica> findByPacienteDniPaciente(String dniPaciente);

    boolean existsByPacienteIdPaciente(Integer idPaciente);
}