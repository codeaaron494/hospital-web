package com.hospital.web.repository;

import com.hospital.web.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    Optional<Paciente> findByDniPaciente(String dniPaciente);

    boolean existsByDniPaciente(String dniPaciente);

    List<Paciente> findByEstadoPaciente(String estadoPaciente);
}