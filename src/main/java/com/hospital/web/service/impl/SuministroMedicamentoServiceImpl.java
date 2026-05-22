package com.hospital.web.service.impl;

import com.hospital.web.entity.DespachoMedicamento;
import com.hospital.web.entity.DetalleDespachoMedicamento;
import com.hospital.web.entity.DetalleReceta;
import com.hospital.web.entity.Kardex;
import com.hospital.web.entity.RecetaMedica;
import com.hospital.web.entity.Usuario;
import com.hospital.web.repository.DespachoMedicamentoRepository;
import com.hospital.web.repository.DetalleDespachoMedicamentoRepository;
import com.hospital.web.repository.DetalleRecetaRepository;
import com.hospital.web.repository.KardexRepository;
import com.hospital.web.repository.RecetaMedicaRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.KardexService;
import com.hospital.web.service.SuministroMedicamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SuministroMedicamentoServiceImpl implements SuministroMedicamentoService {

    private static final String VIGENTE = "VIGENTE";

    private static final String ATENDIDO = "ATENDIDO";
    private static final String RECHAZADO = "RECHAZADO";
    private static final String SIN_STOCK = "SIN_STOCK";

    private static final String ENTREGADO = "ENTREGADO";
    private static final String NO_ENTREGADO = "NO_ENTREGADO";

    private static final String SALIDA = "SALIDA";

    private final RecetaMedicaRepository recetaMedicaRepository;
    private final DetalleRecetaRepository detalleRecetaRepository;
    private final DespachoMedicamentoRepository despachoMedicamentoRepository;
    private final DetalleDespachoMedicamentoRepository detalleDespachoMedicamentoRepository;
    private final KardexRepository kardexRepository;
    private final UsuarioRepository usuarioRepository;
    private final KardexService kardexService;

    public SuministroMedicamentoServiceImpl(
            RecetaMedicaRepository recetaMedicaRepository,
            DetalleRecetaRepository detalleRecetaRepository,
            DespachoMedicamentoRepository despachoMedicamentoRepository,
            DetalleDespachoMedicamentoRepository detalleDespachoMedicamentoRepository,
            KardexRepository kardexRepository,
            UsuarioRepository usuarioRepository,
            KardexService kardexService
    ) {
        this.recetaMedicaRepository = recetaMedicaRepository;
        this.detalleRecetaRepository = detalleRecetaRepository;
        this.despachoMedicamentoRepository = despachoMedicamentoRepository;
        this.detalleDespachoMedicamentoRepository = detalleDespachoMedicamentoRepository;
        this.kardexRepository = kardexRepository;
        this.usuarioRepository = usuarioRepository;
        this.kardexService = kardexService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecetaMedica> listarRecetas() {
        return recetaMedicaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecetaMedica> listarRecetasPorEstado(String estadoReceta) {
        return recetaMedicaRepository.findByEstadoReceta(estadoReceta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecetaMedica> buscarRecetaPorId(Integer idReceta) {
        return recetaMedicaRepository.findById(idReceta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleReceta> listarDetalleReceta(Integer idReceta) {
        return detalleRecetaRepository.findByRecetaIdReceta(idReceta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DespachoMedicamento> buscarDespachoPorReceta(Integer idReceta) {
        return despachoMedicamentoRepository.findByRecetaMedicaIdReceta(idReceta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DespachoMedicamento> listarDespachos() {
        return despachoMedicamentoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DespachoMedicamento> listarDespachosPorEstado(String estadoDespacho) {
        return despachoMedicamentoRepository.findByEstadoDespacho(estadoDespacho);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DespachoMedicamento> buscarDespachoPorId(Integer idDespacho) {
        return despachoMedicamentoRepository.findById(idDespacho);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleDespachoMedicamento> listarDetalleDespacho(Integer idDespacho) {
        return detalleDespachoMedicamentoRepository
                .findByDespachoMedicamentoIdDespachoMedicamento(idDespacho);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean recetaYaDespachada(Integer idReceta) {
        return despachoMedicamentoRepository.existsByRecetaMedicaIdReceta(idReceta);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean recetaTieneStockSuficiente(Integer idReceta) {
        List<DetalleReceta> detalles = detalleRecetaRepository.findByRecetaIdReceta(idReceta);

        if (detalles.isEmpty()) {
            return false;
        }

        for (DetalleReceta detalle : detalles) {
            Kardex kardex = kardexRepository
                    .findByMedicamentoIdMedicamento(detalle.getMedicamento().getIdMedicamento())
                    .orElse(null);

            if (kardex == null) {
                return false;
            }

            if (kardex.getStockActual() < detalle.getCantidadIndicada()) {
                return false;
            }
        }

        return true;
    }

    @Override
    @Transactional
    public DespachoMedicamento registrarDespachoAtendido(
            Integer idReceta,
            Integer idUsuarioTecnico,
            String observacion
    ) {
        RecetaMedica receta = recetaMedicaRepository.findById(idReceta)
                .orElseThrow(() -> new IllegalArgumentException("La receta médica no existe."));

        Usuario tecnico = usuarioRepository.findById(idUsuarioTecnico)
                .orElseThrow(() -> new IllegalArgumentException("El usuario técnico no existe."));

        if (despachoMedicamentoRepository.existsByRecetaMedicaIdReceta(idReceta)) {
            throw new IllegalStateException("La receta ya tiene un despacho registrado.");
        }

        if (!VIGENTE.equals(receta.getEstadoReceta())) {
            throw new IllegalStateException("La receta no está vigente para despacho.");
        }

        List<DetalleReceta> detalles = detalleRecetaRepository.findByRecetaIdReceta(idReceta);

        if (detalles.isEmpty()) {
            throw new IllegalStateException("La receta no tiene medicamentos indicados.");
        }

        if (!recetaTieneStockSuficiente(idReceta)) {
            return registrarDespachoRechazado(
                    idReceta,
                    idUsuarioTecnico,
                    SIN_STOCK,
                    "No se pudo despachar porque uno o más medicamentos no tienen stock suficiente."
            );
        }

        DespachoMedicamento despacho = new DespachoMedicamento();
        despacho.setRecetaMedica(receta);
        despacho.setFechaDespacho(LocalDateTime.now());
        despacho.setEstadoDespacho(ATENDIDO);
        despacho.setObservacion(
                observacion != null && !observacion.isBlank()
                        ? observacion
                        : "Despacho de medicamentos realizado conforme."
        );
        despacho.setUsuarioTecnico(tecnico);

        DespachoMedicamento despachoGuardado = despachoMedicamentoRepository.save(despacho);

        for (DetalleReceta detalle : detalles) {
            Kardex kardex = kardexRepository
                    .findByMedicamentoIdMedicamento(detalle.getMedicamento().getIdMedicamento())
                    .orElseThrow(() -> new IllegalStateException(
                            "El medicamento " + detalle.getMedicamento().getNombreMedicamento() + " no tiene Kardex."
                    ));

            Integer cantidad = detalle.getCantidadIndicada();

            kardexService.registrarMovimiento(
                    kardex.getIdKardex(),
                    SALIDA,
                    cantidad,
                    "Salida por despacho de receta médica REC-" + receta.getIdReceta(),
                    idUsuarioTecnico
            );

            DetalleDespachoMedicamento detalleDespacho = new DetalleDespachoMedicamento();
            detalleDespacho.setDespachoMedicamento(despachoGuardado);
            detalleDespacho.setDetalleReceta(detalle);
            detalleDespacho.setMedicamento(detalle.getMedicamento());
            detalleDespacho.setCantidadRecetada(cantidad);
            detalleDespacho.setCantidadEntregada(cantidad);
            detalleDespacho.setEstadoDetalle(ENTREGADO);
            detalleDespacho.setObservacion("Medicamento entregado según receta.");

            detalleDespachoMedicamentoRepository.save(detalleDespacho);
        }

        return despachoGuardado;
    }

    @Override
    @Transactional
    public DespachoMedicamento registrarDespachoRechazado(
            Integer idReceta,
            Integer idUsuarioTecnico,
            String estadoDespacho,
            String observacion
    ) {
        RecetaMedica receta = recetaMedicaRepository.findById(idReceta)
                .orElseThrow(() -> new IllegalArgumentException("La receta médica no existe."));

        Usuario tecnico = usuarioRepository.findById(idUsuarioTecnico)
                .orElseThrow(() -> new IllegalArgumentException("El usuario técnico no existe."));

        if (despachoMedicamentoRepository.existsByRecetaMedicaIdReceta(idReceta)) {
            throw new IllegalStateException("La receta ya tiene un despacho registrado.");
        }

        if (!RECHAZADO.equals(estadoDespacho) && !SIN_STOCK.equals(estadoDespacho)) {
            throw new IllegalArgumentException("El estado de rechazo no es válido.");
        }

        List<DetalleReceta> detalles = detalleRecetaRepository.findByRecetaIdReceta(idReceta);

        DespachoMedicamento despacho = new DespachoMedicamento();
        despacho.setRecetaMedica(receta);
        despacho.setFechaDespacho(LocalDateTime.now());
        despacho.setEstadoDespacho(estadoDespacho);
        despacho.setObservacion(observacion);
        despacho.setUsuarioTecnico(tecnico);

        DespachoMedicamento despachoGuardado = despachoMedicamentoRepository.save(despacho);

        for (DetalleReceta detalle : detalles) {
            DetalleDespachoMedicamento detalleDespacho = new DetalleDespachoMedicamento();
            detalleDespacho.setDespachoMedicamento(despachoGuardado);
            detalleDespacho.setDetalleReceta(detalle);
            detalleDespacho.setMedicamento(detalle.getMedicamento());
            detalleDespacho.setCantidadRecetada(detalle.getCantidadIndicada());
            detalleDespacho.setCantidadEntregada(0);

            if (SIN_STOCK.equals(estadoDespacho)) {
                detalleDespacho.setEstadoDetalle(SIN_STOCK);
                detalleDespacho.setObservacion("No se entregó por falta de stock disponible.");
            } else {
                detalleDespacho.setEstadoDetalle(NO_ENTREGADO);
                detalleDespacho.setObservacion("No se entregó porque la receta fue rechazada.");
            }

            detalleDespachoMedicamentoRepository.save(detalleDespacho);
        }

        return despachoGuardado;
    }
}