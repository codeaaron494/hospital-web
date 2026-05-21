package com.hospital.web.service;

import com.hospital.web.entity.Paciente;

import java.util.List;
import java.util.Optional;

public interface PacienteService {

    List<Paciente> listarTodos();

    Optional<Paciente> buscarPorId(Integer idPaciente);

    Optional<Paciente> buscarPorDni(String dniPaciente);

    Paciente guardar(Paciente paciente);

    boolean existeDni(String dniPaciente);
}