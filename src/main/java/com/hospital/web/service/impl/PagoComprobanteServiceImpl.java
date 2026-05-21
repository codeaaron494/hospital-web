package com.hospital.web.service.impl;

import com.hospital.web.entity.ComprobantePago;
import com.hospital.web.entity.PagoComprobante;
import com.hospital.web.entity.Usuario;
import com.hospital.web.repository.ComprobantePagoRepository;
import com.hospital.web.repository.PagoComprobanteRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.PagoComprobanteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoComprobanteServiceImpl implements PagoComprobanteService {

    private static final String REGISTRADO = "REGISTRADO";
    private static final String PAGADO = "PAGADO";
    private static final String ANULADO = "ANULADO";

    private final PagoComprobanteRepository pagoComprobanteRepository;
    private final ComprobantePagoRepository comprobantePagoRepository;
    private final UsuarioRepository usuarioRepository;

    public PagoComprobanteServiceImpl(
            PagoComprobanteRepository pagoComprobanteRepository,
            ComprobantePagoRepository comprobantePagoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.pagoComprobanteRepository = pagoComprobanteRepository;
        this.comprobantePagoRepository = comprobantePagoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoComprobante> listarPorComprobante(Integer idComprobantePago) {
        return pagoComprobanteRepository.findByComprobantePagoIdComprobantePago(idComprobantePago);
    }

    @Override
    @Transactional
    public PagoComprobante pagarComprobante(
            Integer idComprobantePago,
            Integer idUsuarioCobranza,
            BigDecimal montoPagado,
            String medioPago,
            String observacion
    ) {
        ComprobantePago comprobante = comprobantePagoRepository.findById(idComprobantePago)
                .orElseThrow(() -> new IllegalArgumentException("El comprobante no existe."));

        if (ANULADO.equals(comprobante.getEstadoComprobante())) {
            throw new IllegalStateException("No se puede pagar un comprobante anulado.");
        }

        if (PAGADO.equals(comprobante.getEstadoComprobante())) {
            throw new IllegalStateException("El comprobante ya fue pagado.");
        }

        if (pagoComprobanteRepository.existsByComprobantePagoIdComprobantePago(idComprobantePago)) {
            throw new IllegalStateException("Ya existe un pago registrado para este comprobante.");
        }

        if (montoPagado == null || montoPagado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto pagado debe ser mayor a cero.");
        }

        if (montoPagado.compareTo(comprobante.getTotal()) != 0) {
            throw new IllegalArgumentException("El monto pagado debe coincidir con el total del comprobante.");
        }

        Usuario cobranza = usuarioRepository.findById(idUsuarioCobranza)
                .orElseThrow(() -> new IllegalArgumentException("El usuario de cobranza no existe."));

        PagoComprobante pago = new PagoComprobante();
        pago.setComprobantePago(comprobante);
        pago.setUsuarioCobranza(cobranza);
        pago.setFechaPago(LocalDateTime.now());
        pago.setMontoPagado(montoPagado);
        pago.setMedioPago(medioPago);
        pago.setEstadoPago(REGISTRADO);
        pago.setObservacion(observacion);

        comprobante.setEstadoComprobante(PAGADO);
        comprobantePagoRepository.save(comprobante);

        return pagoComprobanteRepository.save(pago);
    }
}