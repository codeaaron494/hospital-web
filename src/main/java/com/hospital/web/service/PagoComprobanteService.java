package com.hospital.web.service;

import com.hospital.web.entity.PagoComprobante;

import java.math.BigDecimal;
import java.util.List;

public interface PagoComprobanteService {

    List<PagoComprobante> listarPorComprobante(Integer idComprobantePago);

    PagoComprobante pagarComprobante(
            Integer idComprobantePago,
            Integer idUsuarioCobranza,
            BigDecimal montoPagado,
            String medioPago,
            String observacion
    );
}