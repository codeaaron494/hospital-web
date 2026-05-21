package com.hospital.web.service;

import com.hospital.web.entity.DetalleOrdenCompra;
import com.hospital.web.entity.OrdenCompra;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrdenCompraService {

    List<OrdenCompra> listarTodas();

    List<OrdenCompra> listarPorEstado(String estadoOrden);

    Optional<OrdenCompra> buscarPorId(Integer idOrdenCompra);

    List<DetalleOrdenCompra> listarDetalle(Integer idOrdenCompra);

    OrdenCompra generarOrden(
            Integer idProveedor,
            Integer idUsuarioAlmacenero,
            String observacion
    );

    DetalleOrdenCompra agregarDetalle(
            Integer idOrdenCompra,
            Integer idMedicamento,
            Integer cantidadSolicitada,
            BigDecimal precioReferencial
    );

    OrdenCompra enviarRevision(Integer idOrdenCompra);

    OrdenCompra autorizarOrden(
            Integer idOrdenCompra,
            Integer idUsuarioQuimico,
            String observacion
    );

    OrdenCompra rechazarOrden(
            Integer idOrdenCompra,
            Integer idUsuarioQuimico,
            String observacion
    );
}