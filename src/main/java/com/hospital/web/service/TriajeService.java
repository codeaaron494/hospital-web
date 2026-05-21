package com.hospital.web.service;

import com.hospital.web.entity.Triaje;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TriajeService {

    List<Triaje> listarTodos();

    Optional<Triaje> buscarPorId(Integer idTriaje);

    Optional<Triaje> buscarPorFicha(Integer idFichaAdmision);

    List<Triaje> buscarPorHistoria(Integer idHistoriaClinica);

    Triaje registrarTriaje(
            Integer idFichaAdmision,
            BigDecimal peso,
            BigDecimal talla,
            BigDecimal temperatura,
            String presionArterial,
            Integer frecuenciaCardiaca
    );
}