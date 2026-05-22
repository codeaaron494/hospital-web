package com.hospital.web.service.impl;

import com.hospital.web.entity.CategoriaMedicamento;
import com.hospital.web.entity.Kardex;
import com.hospital.web.entity.Medicamento;
import com.hospital.web.entity.MovimientoKardex;
import com.hospital.web.entity.Usuario;
import com.hospital.web.repository.CategoriaMedicamentoRepository;
import com.hospital.web.repository.KardexRepository;
import com.hospital.web.repository.MedicamentoRepository;
import com.hospital.web.repository.MovimientoKardexRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.InventarioMedicamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventarioMedicamentoServiceImpl implements InventarioMedicamentoService {

    private static final String ACTIVO = "ACTIVO";
    private static final String INGRESO = "INGRESO";

    private final MedicamentoRepository medicamentoRepository;
    private final CategoriaMedicamentoRepository categoriaMedicamentoRepository;
    private final KardexRepository kardexRepository;
    private final MovimientoKardexRepository movimientoKardexRepository;
    private final UsuarioRepository usuarioRepository;

    public InventarioMedicamentoServiceImpl(
            MedicamentoRepository medicamentoRepository,
            CategoriaMedicamentoRepository categoriaMedicamentoRepository,
            KardexRepository kardexRepository,
            MovimientoKardexRepository movimientoKardexRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.medicamentoRepository = medicamentoRepository;
        this.categoriaMedicamentoRepository = categoriaMedicamentoRepository;
        this.kardexRepository = kardexRepository;
        this.movimientoKardexRepository = movimientoKardexRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicamento> listarTodos() {
        return medicamentoRepository.findAll();
    }

    @Override
    @Transactional
    public Medicamento registrarMedicamentoConKardex(
            String nombreMedicamento,
            String concentracion,
            String presentacion,
            Integer idCategoria,
            Integer stockActual,
            Integer stockMinimo,
            Integer idUsuario
    ) {
        if (nombreMedicamento == null || nombreMedicamento.isBlank()) {
            throw new IllegalArgumentException("El nombre del medicamento es obligatorio.");
        }

        if (concentracion == null || concentracion.isBlank()) {
            throw new IllegalArgumentException("La concentración es obligatoria.");
        }

        if (presentacion == null || presentacion.isBlank()) {
            throw new IllegalArgumentException("La presentación es obligatoria.");
        }

        if (stockActual == null || stockActual < 0) {
            throw new IllegalArgumentException("El stock actual no puede ser negativo.");
        }

        if (stockMinimo == null || stockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo.");
        }

        CategoriaMedicamento categoria = categoriaMedicamentoRepository.findById(idCategoria)
                .orElseThrow(() -> new IllegalArgumentException("La categoría no existe."));

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

        Medicamento medicamento = new Medicamento();
        medicamento.setNombreMedicamento(nombreMedicamento.trim());
        medicamento.setConcentracion(concentracion.trim());
        medicamento.setPresentacion(presentacion.trim());
        medicamento.setCategoria(categoria);
        medicamento.setStockActual(stockActual);
        medicamento.setStockMinimo(stockMinimo);

        Medicamento medicamentoGuardado = medicamentoRepository.save(medicamento);

        Kardex kardex = new Kardex();
        kardex.setMedicamento(medicamentoGuardado);
        kardex.setStockActual(stockActual);
        kardex.setStockMinimo(stockMinimo);
        kardex.setFechaUltimaActualizacion(LocalDateTime.now());
        kardex.setEstadoKardex(ACTIVO);

        Kardex kardexGuardado = kardexRepository.save(kardex);

        if (stockActual > 0) {
            MovimientoKardex movimiento = new MovimientoKardex();
            movimiento.setKardex(kardexGuardado);
            movimiento.setTipoMovimiento(INGRESO);
            movimiento.setCantidad(stockActual);
            movimiento.setStockAnterior(0);
            movimiento.setStockResultante(stockActual);
            movimiento.setFechaMovimiento(LocalDateTime.now());
            movimiento.setMotivo("Registro inicial del medicamento en inventario.");
            movimiento.setUsuario(usuario);

            movimientoKardexRepository.save(movimiento);
        }

        return medicamentoGuardado;
    }
}