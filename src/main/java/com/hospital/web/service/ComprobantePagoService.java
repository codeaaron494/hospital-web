package com.hospital.web.service;

import com.hospital.web.entity.ComprobantePago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ComprobantePagoService {

    List<ComprobantePago> listarTodos();

    List<ComprobantePago> listarPorEstado(String estadoComprobante);

    Optional<ComprobantePago> buscarPorId(Integer idComprobantePago);

    ComprobantePago registrarComprobante(
            Integer idOrdenCompra,
            Integer idUsuarioCobranza,
            String numeroComprobante,
            String tipoComprobante,
            LocalDate fechaEmision,
            BigDecimal subtotal,
            BigDecimal igv
    );

    ComprobantePago anularComprobante(Integer idComprobantePago);
}