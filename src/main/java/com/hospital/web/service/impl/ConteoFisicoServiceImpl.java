package com.hospital.web.service.impl;

import com.hospital.web.entity.ConteoFisico;
import com.hospital.web.entity.DetalleConteoFisico;
import com.hospital.web.entity.Kardex;
import com.hospital.web.entity.Medicamento;
import com.hospital.web.entity.Usuario;
import com.hospital.web.repository.ConteoFisicoRepository;
import com.hospital.web.repository.DetalleConteoFisicoRepository;
import com.hospital.web.repository.KardexRepository;
import com.hospital.web.repository.MedicamentoRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.ConteoFisicoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConteoFisicoServiceImpl implements ConteoFisicoService {

    private static final String REGISTRADO = "REGISTRADO";
    private static final String CON_DIFERENCIAS = "CON_DIFERENCIAS";
    private static final String SIN_DIFERENCIAS = "SIN_DIFERENCIAS";
    private static final String ENVIADO_A_QUIMICO = "ENVIADO_A_QUIMICO";

    private final ConteoFisicoRepository conteoFisicoRepository;
    private final DetalleConteoFisicoRepository detalleConteoFisicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final KardexRepository kardexRepository;

    public ConteoFisicoServiceImpl(
            ConteoFisicoRepository conteoFisicoRepository,
            DetalleConteoFisicoRepository detalleConteoFisicoRepository,
            UsuarioRepository usuarioRepository,
            MedicamentoRepository medicamentoRepository,
            KardexRepository kardexRepository
    ) {
        this.conteoFisicoRepository = conteoFisicoRepository;
        this.detalleConteoFisicoRepository = detalleConteoFisicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.kardexRepository = kardexRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConteoFisico> listarTodos() {
        return conteoFisicoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConteoFisico> listarPorEstado(String estadoConteo) {
        return conteoFisicoRepository.findByEstadoConteo(estadoConteo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConteoFisico> buscarPorId(Integer idConteoFisico) {
        return conteoFisicoRepository.findById(idConteoFisico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleConteoFisico> listarDetalle(Integer idConteoFisico) {
        return detalleConteoFisicoRepository.findByConteoFisicoIdConteoFisico(idConteoFisico);
    }

    @Override
    @Transactional
    public ConteoFisico registrarConteo(Integer idUsuarioAlmacenero, String observacion) {
        Usuario almacenero = usuarioRepository.findById(idUsuarioAlmacenero)
                .orElseThrow(() -> new IllegalArgumentException("El usuario almacenero no existe."));

        ConteoFisico conteo = new ConteoFisico();
        conteo.setUsuarioAlmacenero(almacenero);
        conteo.setFechaConteo(LocalDateTime.now());
        conteo.setEstadoConteo(REGISTRADO);
        conteo.setObservacion(observacion);

        return conteoFisicoRepository.save(conteo);
    }

    @Override
    @Transactional
    public DetalleConteoFisico agregarDetalleConteo(
            Integer idConteoFisico,
            Integer idMedicamento,
            Integer stockFisico,
            String observacion
    ) {
        ConteoFisico conteo = conteoFisicoRepository.findById(idConteoFisico)
                .orElseThrow(() -> new IllegalArgumentException("El conteo físico no existe."));

        Medicamento medicamento = medicamentoRepository.findById(idMedicamento)
                .orElseThrow(() -> new IllegalArgumentException("El medicamento no existe."));

        if (detalleConteoFisicoRepository
                .findByConteoFisicoIdConteoFisicoAndMedicamentoIdMedicamento(idConteoFisico, idMedicamento)
                .isPresent()) {
            throw new IllegalStateException("Este medicamento ya fue registrado en el conteo.");
        }

        if (stockFisico == null || stockFisico < 0) {
            throw new IllegalArgumentException("El stock físico no puede ser negativo.");
        }

        Kardex kardex = kardexRepository.findByMedicamentoIdMedicamento(idMedicamento)
                .orElseThrow(() -> new IllegalStateException("El medicamento no tiene Kardex."));

        Integer stockSistema = kardex.getStockActual();
        Integer diferencia = stockFisico - stockSistema;

        DetalleConteoFisico detalle = new DetalleConteoFisico();
        detalle.setConteoFisico(conteo);
        detalle.setMedicamento(medicamento);
        detalle.setStockSistema(stockSistema);
        detalle.setStockFisico(stockFisico);
        detalle.setDiferencia(diferencia);
        detalle.setObservacion(observacion);

        DetalleConteoFisico guardado = detalleConteoFisicoRepository.save(detalle);

        List<DetalleConteoFisico> detalles =
                detalleConteoFisicoRepository.findByConteoFisicoIdConteoFisico(idConteoFisico);

        boolean tieneDiferencias = detalles.stream().anyMatch(d -> d.getDiferencia() != 0);

        conteo.setEstadoConteo(tieneDiferencias ? CON_DIFERENCIAS : SIN_DIFERENCIAS);
        conteoFisicoRepository.save(conteo);

        return guardado;
    }

    @Override
    @Transactional
    public ConteoFisico enviarAQuimico(Integer idConteoFisico) {
        ConteoFisico conteo = conteoFisicoRepository.findById(idConteoFisico)
                .orElseThrow(() -> new IllegalArgumentException("El conteo físico no existe."));

        List<DetalleConteoFisico> detalles =
                detalleConteoFisicoRepository.findByConteoFisicoIdConteoFisico(idConteoFisico);

        if (detalles.isEmpty()) {
            throw new IllegalStateException("El conteo debe tener al menos un medicamento.");
        }

        conteo.setEstadoConteo(ENVIADO_A_QUIMICO);
        return conteoFisicoRepository.save(conteo);
    }
}