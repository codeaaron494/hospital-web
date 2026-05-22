package com.hospital.web.service.impl;

import com.hospital.web.entity.AjusteInventario;
import com.hospital.web.entity.DetalleConteoFisico;
import com.hospital.web.entity.Kardex;
import com.hospital.web.entity.Usuario;
import com.hospital.web.repository.AjusteInventarioRepository;
import com.hospital.web.repository.DetalleConteoFisicoRepository;
import com.hospital.web.repository.KardexRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.AjusteInventarioService;
import com.hospital.web.service.KardexService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AjusteInventarioServiceImpl implements AjusteInventarioService {

    private static final String POSITIVO = "POSITIVO";
    private static final String NEGATIVO = "NEGATIVO";
    private static final String AJUSTE_POSITIVO = "AJUSTE_POSITIVO";
    private static final String AJUSTE_NEGATIVO = "AJUSTE_NEGATIVO";

    private final AjusteInventarioRepository ajusteInventarioRepository;
    private final DetalleConteoFisicoRepository detalleConteoFisicoRepository;
    private final KardexRepository kardexRepository;
    private final UsuarioRepository usuarioRepository;
    private final KardexService kardexService;

    public AjusteInventarioServiceImpl(
            AjusteInventarioRepository ajusteInventarioRepository,
            DetalleConteoFisicoRepository detalleConteoFisicoRepository,
            KardexRepository kardexRepository,
            UsuarioRepository usuarioRepository,
            KardexService kardexService
    ) {
        this.ajusteInventarioRepository = ajusteInventarioRepository;
        this.detalleConteoFisicoRepository = detalleConteoFisicoRepository;
        this.kardexRepository = kardexRepository;
        this.usuarioRepository = usuarioRepository;
        this.kardexService = kardexService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AjusteInventario> listarTodos() {
        return ajusteInventarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AjusteInventario> buscarPorId(Integer idAjusteInventario) {
        return ajusteInventarioRepository.findById(idAjusteInventario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AjusteInventario> buscarPorKardex(Integer idKardex) {
        return ajusteInventarioRepository.findByKardexIdKardex(idKardex);
    }

    @Override
    @Transactional
    public AjusteInventario registrarAjuste(
            Integer idDetalleConteo,
            Integer idUsuarioAlmacenero,
            String motivo
    ) {
        DetalleConteoFisico detalle = detalleConteoFisicoRepository.findById(idDetalleConteo)
                .orElseThrow(() -> new IllegalArgumentException("El detalle de conteo no existe."));

        if (detalle.getDiferencia() == null || detalle.getDiferencia() == 0) {
            throw new IllegalStateException("No se puede ajustar un detalle sin diferencia.");
        }

        Usuario almacenero = usuarioRepository.findById(idUsuarioAlmacenero)
                .orElseThrow(() -> new IllegalArgumentException("El usuario almacenero no existe."));

        Kardex kardex = kardexRepository
                .findByMedicamentoIdMedicamento(detalle.getMedicamento().getIdMedicamento())
                .orElseThrow(() -> new IllegalStateException("El medicamento no tiene Kardex."));

        Integer diferencia = detalle.getDiferencia();
        Integer cantidadAjuste = Math.abs(diferencia);
        String tipoAjuste = diferencia > 0 ? POSITIVO : NEGATIVO;
        String tipoMovimiento = diferencia > 0 ? AJUSTE_POSITIVO : AJUSTE_NEGATIVO;

        AjusteInventario ajuste = new AjusteInventario();
        ajuste.setDetalleConteo(detalle);
        ajuste.setKardex(kardex);
        ajuste.setCantidadAjuste(cantidadAjuste);
        ajuste.setTipoAjuste(tipoAjuste);
        ajuste.setMotivo(motivo);
        ajuste.setFechaAjuste(LocalDateTime.now());
        ajuste.setUsuarioAlmacenero(almacenero);

        AjusteInventario guardado = ajusteInventarioRepository.save(ajuste);

        kardexService.registrarMovimiento(
                kardex.getIdKardex(),
                tipoMovimiento,
                cantidadAjuste,
                motivo,
                idUsuarioAlmacenero
        );

        return guardado;
    }
}