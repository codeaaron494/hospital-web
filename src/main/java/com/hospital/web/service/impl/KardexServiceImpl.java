package com.hospital.web.service.impl;

import com.hospital.web.entity.Kardex;
import com.hospital.web.entity.Medicamento;
import com.hospital.web.entity.MovimientoKardex;
import com.hospital.web.entity.Usuario;
import com.hospital.web.repository.KardexRepository;
import com.hospital.web.repository.MedicamentoRepository;
import com.hospital.web.repository.MovimientoKardexRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.KardexService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class KardexServiceImpl implements KardexService {

    private static final String INGRESO = "INGRESO";
    private static final String SALIDA = "SALIDA";
    private static final String AJUSTE_POSITIVO = "AJUSTE_POSITIVO";
    private static final String AJUSTE_NEGATIVO = "AJUSTE_NEGATIVO";

    private final KardexRepository kardexRepository;
    private final MovimientoKardexRepository movimientoKardexRepository;
    private final UsuarioRepository usuarioRepository;
    private final MedicamentoRepository medicamentoRepository;

    public KardexServiceImpl(
            KardexRepository kardexRepository,
            MovimientoKardexRepository movimientoKardexRepository,
            UsuarioRepository usuarioRepository,
            MedicamentoRepository medicamentoRepository
    ) {
        this.kardexRepository = kardexRepository;
        this.movimientoKardexRepository = movimientoKardexRepository;
        this.usuarioRepository = usuarioRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Kardex> listarTodos() {
        return kardexRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Kardex> buscarPorId(Integer idKardex) {
        return kardexRepository.findById(idKardex);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Kardex> buscarPorMedicamento(Integer idMedicamento) {
        return kardexRepository.findByMedicamentoIdMedicamento(idMedicamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoKardex> listarMovimientos(Integer idKardex) {
        return movimientoKardexRepository.findByKardexIdKardex(idKardex);
    }

    @Override
    @Transactional
    public MovimientoKardex registrarMovimiento(
            Integer idKardex,
            String tipoMovimiento,
            Integer cantidad,
            String motivo,
            Integer idUsuario
    ) {
        Kardex kardex = kardexRepository.findById(idKardex)
                .orElseThrow(() -> new IllegalArgumentException("El Kardex no existe."));

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        Integer stockAnterior = kardex.getStockActual();
        Integer stockResultante;

        if (INGRESO.equals(tipoMovimiento) || AJUSTE_POSITIVO.equals(tipoMovimiento)) {
            stockResultante = stockAnterior + cantidad;
        } else if (SALIDA.equals(tipoMovimiento) || AJUSTE_NEGATIVO.equals(tipoMovimiento)) {
            stockResultante = stockAnterior - cantidad;

            if (stockResultante < 0) {
                throw new IllegalStateException("No se puede registrar movimiento porque el stock resultante sería negativo.");
            }
        } else {
            throw new IllegalArgumentException("Tipo de movimiento no válido.");
        }

        kardex.setStockActual(stockResultante);
        kardex.setFechaUltimaActualizacion(LocalDateTime.now());
        kardexRepository.save(kardex);

        Medicamento medicamento = kardex.getMedicamento();
        medicamento.setStockActual(stockResultante);
        medicamentoRepository.save(medicamento);

        MovimientoKardex movimiento = new MovimientoKardex();
        movimiento.setKardex(kardex);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setCantidad(cantidad);
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockResultante(stockResultante);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setMotivo(motivo);
        movimiento.setUsuario(usuario);

        return movimientoKardexRepository.save(movimiento);
    }
}