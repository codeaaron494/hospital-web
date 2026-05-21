package com.hospital.web.service;

import com.hospital.web.entity.CitaMedica;

import java.util.List;
import java.util.Optional;

public interface CitaService {

    List<CitaMedica> listarTodas();

    List<CitaMedica> buscarPorDniPaciente(String dniPaciente);

    List<CitaMedica> buscarPorPaciente(Integer idPaciente);

    Optional<CitaMedica> buscarPorId(Integer idCita);

    CitaMedica registrarCita(
            Integer idPaciente,
            Integer idMedico,
            Integer idEspecialidad,
            Integer idAgenda,
            String motivoConsulta,
            String username
    );

    CitaMedica reprogramarCita(
            Integer idCita,
            Integer nuevoIdAgenda
    );

    void anularCita(Integer idCita);
}