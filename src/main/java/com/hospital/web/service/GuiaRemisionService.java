package com.hospital.web.service;

import com.hospital.web.entity.DetalleGuiaRemision;
import com.hospital.web.entity.GuiaRemision;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GuiaRemisionService {

    List<GuiaRemision> listarTodas();

    Optional<GuiaRemision> buscarPorId(Integer idGuiaRemision);

    List<GuiaRemision> buscarPorOrden(Integer idOrdenCompra);

    List<DetalleGuiaRemision> listarDetalle(Integer idGuiaRemision);

    GuiaRemision registrarGuia(
            Integer idOrdenCompra,
            Integer idUsuarioAlmacenero,
            String numeroGuia,
            LocalDate fechaEmision,
            String observacion
    );

    DetalleGuiaRemision agregarDetalle(
            Integer idGuiaRemision,
            Integer idMedicamento,
            Integer cantidadRecibida,
            String observacion
    );
}