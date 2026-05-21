package com.hospital.web.service;

import com.hospital.web.entity.AgendaMedica;

import java.util.List;
import java.util.Optional;

public interface AgendaMedicaService {

    List<AgendaMedica> listarTodos();

    List<AgendaMedica> listarDisponibles();

    List<AgendaMedica> listarDisponiblesPorMedico(Integer idMedico);

    List<AgendaMedica> listarDisponiblesPorEspecialidad(Integer idEspecialidad);

    Optional<AgendaMedica> buscarPorId(Integer idAgenda);

    void ocuparHorario(Integer idAgenda);

    void liberarHorario(Integer idAgenda);
}