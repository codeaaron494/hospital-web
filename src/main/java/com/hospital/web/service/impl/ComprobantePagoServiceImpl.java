package com.hospital.web.service.impl;

import com.hospital.web.entity.ComprobantePago;
import com.hospital.web.entity.OrdenCompra;
import com.hospital.web.entity.Usuario;
import com.hospital.web.repository.ComprobantePagoRepository;
import com.hospital.web.repository.OrdenCompraRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.ComprobantePagoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ComprobantePagoServiceImpl implements ComprobantePagoService {

    private static final String REGISTRADO = "REGISTRADO";
    private static final String ANULADO = "ANULADO";
    private static final String CON_GUIA = "CON_GUIA";
    private static final String AUTORIZADA = "AUTORIZADA";

    private final ComprobantePagoRepository comprobantePagoRepository;
    private final OrdenCompraRepository ordenCompraRepository;
    private final UsuarioRepository usuarioRepository;

    public ComprobantePagoServiceImpl(
            ComprobantePagoRepository comprobantePagoRepository,
            OrdenCompraRepository ordenCompraRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.comprobantePagoRepository = comprobantePagoRepository;
        this.ordenCompraRepository = ordenCompraRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobantePago> listarTodos() {
        return comprobantePagoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobantePago> listarPorEstado(String estadoComprobante) {
        return comprobantePagoRepository.findByEstadoComprobante(estadoComprobante);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprobantePago> buscarPorId(Integer idComprobantePago) {
        return comprobantePagoRepository.findById(idComprobantePago);
    }

    @Override
    @Transactional
    public ComprobantePago registrarComprobante(
            Integer idOrdenCompra,
            Integer idUsuarioCobranza,
            String numeroComprobante,
            String tipoComprobante,
            LocalDate fechaEmision,
            BigDecimal subtotal,
            BigDecimal igv
    ) {
        OrdenCompra orden = ordenCompraRepository.findById(idOrdenCompra)
                .orElseThrow(() -> new IllegalArgumentException("La orden de compra no existe."));

        if (!CON_GUIA.equals(orden.getEstadoOrden()) && !AUTORIZADA.equals(orden.getEstadoOrden())) {
            throw new IllegalStateException("Solo se puede registrar comprobante para una orden autorizada o con guía.");
        }

        if (comprobantePagoRepository.existsByNumeroComprobante(numeroComprobante)) {
            throw new IllegalStateException("Ya existe un comprobante con ese número.");
        }

        Usuario cobranza = usuarioRepository.findById(idUsuarioCobranza)
                .orElseThrow(() -> new IllegalArgumentException("El usuario de cobranza no existe."));

        BigDecimal total = subtotal.add(igv);

        ComprobantePago comprobante = new ComprobantePago();
        comprobante.setOrdenCompra(orden);
        comprobante.setUsuarioCobranza(cobranza);
        comprobante.setNumeroComprobante(numeroComprobante);
        comprobante.setTipoComprobante(tipoComprobante);
        comprobante.setFechaEmision(fechaEmision);
        comprobante.setSubtotal(subtotal);
        comprobante.setIgv(igv);
        comprobante.setTotal(total);
        comprobante.setEstadoComprobante(REGISTRADO);

        return comprobantePagoRepository.save(comprobante);
    }

    @Override
    @Transactional
    public ComprobantePago anularComprobante(Integer idComprobantePago) {
        ComprobantePago comprobante = comprobantePagoRepository.findById(idComprobantePago)
                .orElseThrow(() -> new IllegalArgumentException("El comprobante no existe."));

        if ("PAGADO".equals(comprobante.getEstadoComprobante())) {
            throw new IllegalStateException("No se puede anular un comprobante pagado.");
        }

        comprobante.setEstadoComprobante(ANULADO);
        return comprobantePagoRepository.save(comprobante);
    }
}