package com.hospital.web.service;

import com.hospital.web.entity.DetalleReceta;
import com.hospital.web.entity.RecetaMedica;

import java.util.List;
import java.util.Optional;

public interface RecetaMedicaService {

    List<RecetaMedica> listarTodas();

    Optional<RecetaMedica> buscarPorId(Integer idReceta);

    Optional<RecetaMedica> buscarPorAtencion(Integer idAtencion);

    List<DetalleReceta> listarDetalle(Integer idReceta);

    RecetaMedica generarReceta(Integer idAtencion);

    DetalleReceta agregarDetalle(
            Integer idReceta,
            Integer idMedicamento,
            String dosis,
            String frecuencia,
            String duracion,
            Integer cantidadIndicada
    );
    void eliminarDetalle(Integer idDetalle);
}