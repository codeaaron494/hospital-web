package com.hospital.web.service;

import com.hospital.web.entity.ConteoFisico;
import com.hospital.web.entity.DetalleConteoFisico;

import java.util.List;
import java.util.Optional;

public interface ConteoFisicoService {

    List<ConteoFisico> listarTodos();

    List<ConteoFisico> listarPorEstado(String estadoConteo);

    Optional<ConteoFisico> buscarPorId(Integer idConteoFisico);

    List<DetalleConteoFisico> listarDetalle(Integer idConteoFisico);

    ConteoFisico registrarConteo(
            Integer idUsuarioAlmacenero,
            String observacion
    );

    DetalleConteoFisico agregarDetalleConteo(
            Integer idConteoFisico,
            Integer idMedicamento,
            Integer stockFisico,
            String observacion
    );

    ConteoFisico enviarAQuimico(Integer idConteoFisico);
}