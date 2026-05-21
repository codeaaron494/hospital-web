package com.hospital.web.service;

import com.hospital.web.entity.HistoriaClinica;

import java.util.List;
import java.util.Optional;

public interface HistoriaClinicaService {

    List<HistoriaClinica> listarTodas();

    Optional<HistoriaClinica> buscarPorId(Integer idHistoriaClinica);

    Optional<HistoriaClinica> buscarPorDniPaciente(String dniPaciente);

    HistoriaClinica obtenerOCrearPorPaciente(Integer idPaciente);
}