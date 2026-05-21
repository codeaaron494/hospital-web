package com.hospital.web.service;

import com.hospital.web.entity.AtencionMedica;

import java.util.List;
import java.util.Optional;

public interface AtencionMedicaService {

    List<AtencionMedica> listarTodas();

    Optional<AtencionMedica> buscarPorId(Integer idAtencion);

    List<AtencionMedica> buscarPorHistoria(Integer idHistoriaClinica);

    AtencionMedica registrarAtencion(
            Integer idHistoriaClinica,
            Integer idMedico,
            String diagnostico,
            String tratamiento,
            String recomendaciones
    );
}