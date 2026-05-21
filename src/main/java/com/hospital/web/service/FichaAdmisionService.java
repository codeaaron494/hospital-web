package com.hospital.web.service;

import com.hospital.web.entity.FichaAdmision;

import java.util.List;
import java.util.Optional;

public interface FichaAdmisionService {

    List<FichaAdmision> listarTodas();

    Optional<FichaAdmision> buscarPorId(Integer idFichaAdmision);

    List<FichaAdmision> buscarPorDniPaciente(String dniPaciente);

    List<FichaAdmision> buscarPorEstado(String estadoFicha);

    FichaAdmision registrarFicha(Integer idCita, String tipoAdmision, String prioridad);
}