package com.hospital.web.service.impl;

import com.hospital.web.entity.DetalleGuiaRemision;
import com.hospital.web.entity.GuiaRemision;
import com.hospital.web.entity.Medicamento;
import com.hospital.web.entity.OrdenCompra;
import com.hospital.web.entity.Usuario;
import com.hospital.web.repository.DetalleGuiaRemisionRepository;
import com.hospital.web.repository.GuiaRemisionRepository;
import com.hospital.web.repository.MedicamentoRepository;
import com.hospital.web.repository.OrdenCompraRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.GuiaRemisionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GuiaRemisionServiceImpl implements GuiaRemisionService {

    private static final String AUTORIZADA = "AUTORIZADA";
    private static final String CON_GUIA = "CON_GUIA";
    private static final String REGISTRADA = "REGISTRADA";

    private final GuiaRemisionRepository guiaRemisionRepository;
    private final DetalleGuiaRemisionRepository detalleGuiaRemisionRepository;
    private final OrdenCompraRepository ordenCompraRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public GuiaRemisionServiceImpl(
            GuiaRemisionRepository guiaRemisionRepository,
            DetalleGuiaRemisionRepository detalleGuiaRemisionRepository,
            OrdenCompraRepository ordenCompraRepository,
            MedicamentoRepository medicamentoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.guiaRemisionRepository = guiaRemisionRepository;
        this.detalleGuiaRemisionRepository = detalleGuiaRemisionRepository;
        this.ordenCompraRepository = ordenCompraRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuiaRemision> listarTodas() {
        return guiaRemisionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GuiaRemision> buscarPorId(Integer idGuiaRemision) {
        return guiaRemisionRepository.findById(idGuiaRemision);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuiaRemision> buscarPorOrden(Integer idOrdenCompra) {
        return guiaRemisionRepository.findByOrdenCompraIdOrdenCompra(idOrdenCompra);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleGuiaRemision> listarDetalle(Integer idGuiaRemision) {
        return detalleGuiaRemisionRepository.findByGuiaRemisionIdGuiaRemision(idGuiaRemision);
    }

    @Override
    @Transactional
    public GuiaRemision registrarGuia(
            Integer idOrdenCompra,
            Integer idUsuarioAlmacenero,
            String numeroGuia,
            LocalDate fechaEmision,
            String observacion
    ) {
        OrdenCompra orden = ordenCompraRepository.findById(idOrdenCompra)
                .orElseThrow(() -> new IllegalArgumentException("La orden de compra no existe."));

        if (!AUTORIZADA.equals(orden.getEstadoOrden())) {
            throw new IllegalStateException("Solo se puede registrar guía para una orden AUTORIZADA.");
        }

        if (guiaRemisionRepository.existsByNumeroGuia(numeroGuia)) {
            throw new IllegalStateException("Ya existe una guía con ese número.");
        }

        Usuario almacenero = usuarioRepository.findById(idUsuarioAlmacenero)
                .orElseThrow(() -> new IllegalArgumentException("El usuario almacenero no existe."));

        GuiaRemision guia = new GuiaRemision();
        guia.setOrdenCompra(orden);
        guia.setUsuarioAlmacenero(almacenero);
        guia.setNumeroGuia(numeroGuia);
        guia.setFechaEmision(fechaEmision);
        guia.setFechaRecepcion(LocalDateTime.now());
        guia.setEstadoGuia(REGISTRADA);
        guia.setObservacion(observacion);

        orden.setEstadoOrden(CON_GUIA);
        ordenCompraRepository.save(orden);

        return guiaRemisionRepository.save(guia);
    }

    @Override
    @Transactional
    public DetalleGuiaRemision agregarDetalle(
            Integer idGuiaRemision,
            Integer idMedicamento,
            Integer cantidadRecibida,
            String observacion
    ) {
        GuiaRemision guia = guiaRemisionRepository.findById(idGuiaRemision)
                .orElseThrow(() -> new IllegalArgumentException("La guía de remisión no existe."));

        Medicamento medicamento = medicamentoRepository.findById(idMedicamento)
                .orElseThrow(() -> new IllegalArgumentException("El medicamento no existe."));

        if (cantidadRecibida == null || cantidadRecibida <= 0) {
            throw new IllegalArgumentException("La cantidad recibida debe ser mayor a cero.");
        }

        DetalleGuiaRemision detalle = new DetalleGuiaRemision();
        detalle.setGuiaRemision(guia);
        detalle.setMedicamento(medicamento);
        detalle.setCantidadRecibida(cantidadRecibida);
        detalle.setObservacion(observacion);

        return detalleGuiaRemisionRepository.save(detalle);
    }
}