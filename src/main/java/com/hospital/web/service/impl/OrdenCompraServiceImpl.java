package com.hospital.web.service.impl;

import com.hospital.web.entity.DetalleOrdenCompra;
import com.hospital.web.entity.Medicamento;
import com.hospital.web.entity.OrdenCompra;
import com.hospital.web.entity.Proveedor;
import com.hospital.web.entity.Usuario;
import com.hospital.web.repository.DetalleOrdenCompraRepository;
import com.hospital.web.repository.MedicamentoRepository;
import com.hospital.web.repository.OrdenCompraRepository;
import com.hospital.web.repository.ProveedorRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.OrdenCompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private static final String GENERADA = "GENERADA";
    private static final String PENDIENTE_REVISION = "PENDIENTE_REVISION";
    private static final String AUTORIZADA = "AUTORIZADA";
    private static final String RECHAZADA = "RECHAZADA";
    private static final String CON_GUIA = "CON_GUIA";

    private final OrdenCompraRepository ordenCompraRepository;
    private final DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    private final ProveedorRepository proveedorRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public OrdenCompraServiceImpl(
            OrdenCompraRepository ordenCompraRepository,
            DetalleOrdenCompraRepository detalleOrdenCompraRepository,
            ProveedorRepository proveedorRepository,
            MedicamentoRepository medicamentoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.detalleOrdenCompraRepository = detalleOrdenCompraRepository;
        this.proveedorRepository = proveedorRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> listarTodas() {
        return ordenCompraRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> listarPorEstado(String estadoOrden) {
        return ordenCompraRepository.findByEstadoOrden(estadoOrden);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenCompra> buscarPorId(Integer idOrdenCompra) {
        return ordenCompraRepository.findById(idOrdenCompra);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleOrdenCompra> listarDetalle(Integer idOrdenCompra) {
        return detalleOrdenCompraRepository.findByOrdenCompraIdOrdenCompra(idOrdenCompra);
    }

    @Override
    @Transactional
    public OrdenCompra generarOrden(
            Integer idProveedor,
            Integer idUsuarioAlmacenero,
            String observacion
    ) {
        Proveedor proveedor = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new IllegalArgumentException("El proveedor no existe."));

        Usuario almacenero = usuarioRepository.findById(idUsuarioAlmacenero)
                .orElseThrow(() -> new IllegalArgumentException("El usuario almacenero no existe."));

        OrdenCompra orden = new OrdenCompra();
        orden.setProveedor(proveedor);
        orden.setUsuarioAlmacenero(almacenero);
        orden.setFechaEmision(LocalDateTime.now());
        orden.setEstadoOrden(GENERADA);
        orden.setObservacion(observacion);

        return ordenCompraRepository.save(orden);
    }

    @Override
    @Transactional
    public DetalleOrdenCompra agregarDetalle(
            Integer idOrdenCompra,
            Integer idMedicamento,
            Integer cantidadSolicitada,
            BigDecimal precioReferencial
    ) {
        OrdenCompra orden = ordenCompraRepository.findById(idOrdenCompra)
                .orElseThrow(() -> new IllegalArgumentException("La orden de compra no existe."));

        if (!GENERADA.equals(orden.getEstadoOrden())) {
            throw new IllegalStateException("Solo se puede agregar detalle a una orden en estado GENERADA.");
        }

        Medicamento medicamento = medicamentoRepository.findById(idMedicamento)
                .orElseThrow(() -> new IllegalArgumentException("El medicamento no existe."));

        if (cantidadSolicitada == null || cantidadSolicitada <= 0) {
            throw new IllegalArgumentException("La cantidad solicitada debe ser mayor a cero.");
        }

        if (precioReferencial == null || precioReferencial.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio referencial debe ser mayor a cero.");
        }

        DetalleOrdenCompra detalle = new DetalleOrdenCompra();
        detalle.setOrdenCompra(orden);
        detalle.setMedicamento(medicamento);
        detalle.setCantidadSolicitada(cantidadSolicitada);
        detalle.setPrecioReferencial(precioReferencial);
        detalle.setSubtotal(precioReferencial.multiply(BigDecimal.valueOf(cantidadSolicitada)));

        return detalleOrdenCompraRepository.save(detalle);
    }

    @Override
    @Transactional
    public OrdenCompra enviarRevision(Integer idOrdenCompra) {
        OrdenCompra orden = ordenCompraRepository.findById(idOrdenCompra)
                .orElseThrow(() -> new IllegalArgumentException("La orden de compra no existe."));

        if (!GENERADA.equals(orden.getEstadoOrden())) {
            throw new IllegalStateException("Solo una orden GENERADA puede enviarse a revisión.");
        }

        List<DetalleOrdenCompra> detalles =
                detalleOrdenCompraRepository.findByOrdenCompraIdOrdenCompra(idOrdenCompra);

        if (detalles.isEmpty()) {
            throw new IllegalStateException("La orden debe tener al menos un medicamento.");
        }

        orden.setEstadoOrden(PENDIENTE_REVISION);
        return ordenCompraRepository.save(orden);
    }

    @Override
    @Transactional
    public OrdenCompra autorizarOrden(
            Integer idOrdenCompra,
            Integer idUsuarioQuimico,
            String observacion
    ) {
        OrdenCompra orden = ordenCompraRepository.findById(idOrdenCompra)
                .orElseThrow(() -> new IllegalArgumentException("La orden de compra no existe."));

        if (!PENDIENTE_REVISION.equals(orden.getEstadoOrden())) {
            throw new IllegalStateException("Solo se puede autorizar una orden pendiente de revisión.");
        }

        Usuario quimico = usuarioRepository.findById(idUsuarioQuimico)
                .orElseThrow(() -> new IllegalArgumentException("El usuario químico no existe."));

        orden.setUsuarioQuimico(quimico);
        orden.setEstadoOrden(AUTORIZADA);
        orden.setObservacion(observacion);

        return ordenCompraRepository.save(orden);
    }

    @Override
    @Transactional
    public OrdenCompra rechazarOrden(
            Integer idOrdenCompra,
            Integer idUsuarioQuimico,
            String observacion
    ) {
        OrdenCompra orden = ordenCompraRepository.findById(idOrdenCompra)
                .orElseThrow(() -> new IllegalArgumentException("La orden de compra no existe."));

        if (!PENDIENTE_REVISION.equals(orden.getEstadoOrden())) {
            throw new IllegalStateException("Solo se puede rechazar una orden pendiente de revisión.");
        }

        Usuario quimico = usuarioRepository.findById(idUsuarioQuimico)
                .orElseThrow(() -> new IllegalArgumentException("El usuario químico no existe."));

        orden.setUsuarioQuimico(quimico);
        orden.setEstadoOrden(RECHAZADA);
        orden.setObservacion(observacion);

        return ordenCompraRepository.save(orden);
    }

    public static String getEstadoConGuia() {
        return CON_GUIA;
    }
}