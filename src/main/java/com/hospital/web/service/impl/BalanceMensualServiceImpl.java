package com.hospital.web.service.impl;

import com.hospital.web.entity.BalanceMensual;
import com.hospital.web.entity.DetalleBalanceMensual;
import com.hospital.web.entity.Kardex;
import com.hospital.web.entity.ObservacionBalance;
import com.hospital.web.entity.Usuario;
import com.hospital.web.repository.BalanceMensualRepository;
import com.hospital.web.repository.DetalleBalanceMensualRepository;
import com.hospital.web.repository.KardexRepository;
import com.hospital.web.repository.ObservacionBalanceRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.BalanceMensualService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BalanceMensualServiceImpl implements BalanceMensualService {

    private static final String GENERADO = "GENERADO";
    private static final String ENVIADO_A_QUIMICO = "ENVIADO_A_QUIMICO";
    private static final String APROBADO = "APROBADO";
    private static final String OBSERVADO = "OBSERVADO";
    private static final String EXPORTADO_DIGEMID = "EXPORTADO_DIGEMID";
    private static final String CONFORME_DIGEMID = "CONFORME_DIGEMID";
    private static final String OBSERVADO_DIGEMID = "OBSERVADO_DIGEMID";

    private static final String QUIMICO = "QUIMICO";
    private static final String DIGEMID = "DIGEMID";
    private static final String PENDIENTE = "PENDIENTE";
    private static final String SUBSANADA = "SUBSANADA";

    private final BalanceMensualRepository balanceMensualRepository;
    private final DetalleBalanceMensualRepository detalleBalanceMensualRepository;
    private final ObservacionBalanceRepository observacionBalanceRepository;
    private final KardexRepository kardexRepository;
    private final UsuarioRepository usuarioRepository;

    public BalanceMensualServiceImpl(
            BalanceMensualRepository balanceMensualRepository,
            DetalleBalanceMensualRepository detalleBalanceMensualRepository,
            ObservacionBalanceRepository observacionBalanceRepository,
            KardexRepository kardexRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.balanceMensualRepository = balanceMensualRepository;
        this.detalleBalanceMensualRepository = detalleBalanceMensualRepository;
        this.observacionBalanceRepository = observacionBalanceRepository;
        this.kardexRepository = kardexRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BalanceMensual> listarTodos() {
        return balanceMensualRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BalanceMensual> listarPorEstado(String estadoBalance) {
        return balanceMensualRepository.findByEstadoBalance(estadoBalance);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BalanceMensual> buscarPorId(Integer idBalanceMensual) {
        return balanceMensualRepository.findById(idBalanceMensual);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleBalanceMensual> listarDetalle(Integer idBalanceMensual) {
        return detalleBalanceMensualRepository.findByBalanceMensualIdBalanceMensual(idBalanceMensual);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObservacionBalance> listarObservaciones(Integer idBalanceMensual) {
        return observacionBalanceRepository.findByBalanceMensualIdBalanceMensual(idBalanceMensual);
    }

    @Override
    @Transactional
    public BalanceMensual generarBalance(
            String periodo,
            Integer idUsuarioAlmacenero,
            String observacion
    ) {
        if (periodo == null || periodo.isBlank()) {
            throw new IllegalArgumentException("El periodo es obligatorio.");
        }

        if (balanceMensualRepository.existsByPeriodo(periodo.trim())) {
            throw new IllegalStateException("Ya existe un balance mensual para ese periodo.");
        }

        Usuario almacenero = usuarioRepository.findById(idUsuarioAlmacenero)
                .orElseThrow(() -> new IllegalArgumentException("El usuario almacenero no existe."));

        BalanceMensual balance = new BalanceMensual();
        balance.setPeriodo(periodo.trim());
        balance.setFechaGeneracion(LocalDateTime.now());
        balance.setEstadoBalance(GENERADO);
        balance.setObservacion(observacion);
        balance.setUsuarioAlmacenero(almacenero);

        BalanceMensual guardado = balanceMensualRepository.save(balance);

        List<Kardex> kardexList = kardexRepository.findAll();

        for (Kardex kardex : kardexList) {
            DetalleBalanceMensual detalle = new DetalleBalanceMensual();
            detalle.setBalanceMensual(guardado);
            detalle.setMedicamento(kardex.getMedicamento());

            detalle.setStockInicial(kardex.getStockActual());
            detalle.setIngresos(0);
            detalle.setSalidas(0);
            detalle.setAjustes(0);
            detalle.setStockFinal(kardex.getStockActual());

            detalleBalanceMensualRepository.save(detalle);
        }

        return guardado;
    }

    @Override
    @Transactional
    public BalanceMensual enviarAQuimico(Integer idBalanceMensual) {
        BalanceMensual balance = balanceMensualRepository.findById(idBalanceMensual)
                .orElseThrow(() -> new IllegalArgumentException("El balance mensual no existe."));

        if (!GENERADO.equals(balance.getEstadoBalance()) && !OBSERVADO.equals(balance.getEstadoBalance())) {
            throw new IllegalStateException("Solo un balance generado u observado puede enviarse al químico.");
        }

        balance.setEstadoBalance(ENVIADO_A_QUIMICO);
        return balanceMensualRepository.save(balance);
    }

    @Override
    @Transactional
    public BalanceMensual aprobarBalance(
            Integer idBalanceMensual,
            Integer idUsuarioQuimico,
            String observacion
    ) {
        BalanceMensual balance = balanceMensualRepository.findById(idBalanceMensual)
                .orElseThrow(() -> new IllegalArgumentException("El balance mensual no existe."));

        if (!ENVIADO_A_QUIMICO.equals(balance.getEstadoBalance())) {
            throw new IllegalStateException("Solo se puede aprobar un balance enviado al químico.");
        }

        Usuario quimico = usuarioRepository.findById(idUsuarioQuimico)
                .orElseThrow(() -> new IllegalArgumentException("El usuario químico no existe."));

        balance.setUsuarioQuimico(quimico);
        balance.setEstadoBalance(APROBADO);
        balance.setObservacion(observacion);

        return balanceMensualRepository.save(balance);
    }

    @Override
    @Transactional
    public BalanceMensual observarBalance(
            Integer idBalanceMensual,
            Integer idUsuarioQuimico,
            String descripcion
    ) {
        BalanceMensual balance = balanceMensualRepository.findById(idBalanceMensual)
                .orElseThrow(() -> new IllegalArgumentException("El balance mensual no existe."));

        if (!ENVIADO_A_QUIMICO.equals(balance.getEstadoBalance())) {
            throw new IllegalStateException("Solo se puede observar un balance enviado al químico.");
        }

        Usuario quimico = usuarioRepository.findById(idUsuarioQuimico)
                .orElseThrow(() -> new IllegalArgumentException("El usuario químico no existe."));

        balance.setUsuarioQuimico(quimico);
        balance.setEstadoBalance(OBSERVADO);
        balanceMensualRepository.save(balance);

        ObservacionBalance observacion = new ObservacionBalance();
        observacion.setBalanceMensual(balance);
        observacion.setOrigenObservacion(QUIMICO);
        observacion.setDescripcion(descripcion);
        observacion.setFechaObservacion(LocalDateTime.now());
        observacion.setEstadoObservacion(PENDIENTE);
        observacionBalanceRepository.save(observacion);

        return balance;
    }

    @Override
    @Transactional
    public BalanceMensual exportarDigemid(Integer idBalanceMensual) {
        BalanceMensual balance = balanceMensualRepository.findById(idBalanceMensual)
                .orElseThrow(() -> new IllegalArgumentException("El balance mensual no existe."));

        // MODIFICACIÓN APLICADA AQUÍ: Permite exportar si está APROBADO o si regresó OBSERVADO_DIGEMID
        if (!APROBADO.equals(balance.getEstadoBalance()) && !OBSERVADO_DIGEMID.equals(balance.getEstadoBalance())) {
            throw new IllegalStateException("Solo se puede exportar a DIGEMID un balance aprobado o subsanado.");
        }

        balance.setEstadoBalance(EXPORTADO_DIGEMID);
        return balanceMensualRepository.save(balance);
    }

    @Override
    @Transactional
    public BalanceMensual registrarConformidadDigemid(Integer idBalanceMensual) {
        BalanceMensual balance = balanceMensualRepository.findById(idBalanceMensual)
                .orElseThrow(() -> new IllegalArgumentException("El balance mensual no existe."));

        if (!EXPORTADO_DIGEMID.equals(balance.getEstadoBalance())) {
            throw new IllegalStateException("Solo se puede registrar conformidad si el balance fue exportado.");
        }

        balance.setEstadoBalance(CONFORME_DIGEMID);
        return balanceMensualRepository.save(balance);
    }

    @Override
    @Transactional
    public BalanceMensual registrarObservacionDigemid(
            Integer idBalanceMensual,
            String descripcion
    ) {
        BalanceMensual balance = balanceMensualRepository.findById(idBalanceMensual)
                .orElseThrow(() -> new IllegalArgumentException("El balance mensual no existe."));

        if (!EXPORTADO_DIGEMID.equals(balance.getEstadoBalance())) {
            throw new IllegalStateException("Solo se puede observar por DIGEMID si el balance fue exportado.");
        }

        balance.setEstadoBalance(OBSERVADO_DIGEMID);
        balanceMensualRepository.save(balance);

        ObservacionBalance observacion = new ObservacionBalance();
        observacion.setBalanceMensual(balance);
        observacion.setOrigenObservacion(DIGEMID);
        observacion.setDescripcion(descripcion);
        observacion.setFechaObservacion(LocalDateTime.now());
        observacion.setEstadoObservacion(PENDIENTE);
        observacionBalanceRepository.save(observacion);

        return balance;
    }

    @Override
    @Transactional
    public ObservacionBalance subsanarObservacion(Integer idObservacionBalance) {
        ObservacionBalance observacion = observacionBalanceRepository.findById(idObservacionBalance)
                .orElseThrow(() -> new IllegalArgumentException("La observación no existe."));

        observacion.setEstadoObservacion(SUBSANADA);
        return observacionBalanceRepository.save(observacion);
    }
}