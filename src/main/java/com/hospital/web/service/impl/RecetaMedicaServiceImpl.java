package com.hospital.web.service.impl;

import com.hospital.web.entity.AtencionMedica;
import com.hospital.web.entity.DetalleReceta;
import com.hospital.web.entity.Medicamento;
import com.hospital.web.entity.RecetaMedica;
import com.hospital.web.repository.AtencionMedicaRepository;
import com.hospital.web.repository.DetalleRecetaRepository;
import com.hospital.web.repository.MedicamentoRepository;
import com.hospital.web.repository.RecetaMedicaRepository;
import com.hospital.web.service.RecetaMedicaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecetaMedicaServiceImpl implements RecetaMedicaService {

    private static final String VIGENTE = "VIGENTE";

    private final RecetaMedicaRepository recetaMedicaRepository;
    private final DetalleRecetaRepository detalleRecetaRepository;
    private final AtencionMedicaRepository atencionMedicaRepository;
    private final MedicamentoRepository medicamentoRepository;

    public RecetaMedicaServiceImpl(
            RecetaMedicaRepository recetaMedicaRepository,
            DetalleRecetaRepository detalleRecetaRepository,
            AtencionMedicaRepository atencionMedicaRepository,
            MedicamentoRepository medicamentoRepository
    ) {
        this.recetaMedicaRepository = recetaMedicaRepository;
        this.detalleRecetaRepository = detalleRecetaRepository;
        this.atencionMedicaRepository = atencionMedicaRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecetaMedica> listarTodas() {
        return recetaMedicaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecetaMedica> buscarPorId(Integer idReceta) {
        return recetaMedicaRepository.findById(idReceta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecetaMedica> buscarPorAtencion(Integer idAtencion) {
        return recetaMedicaRepository.findByAtencionIdAtencion(idAtencion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleReceta> listarDetalle(Integer idReceta) {
        return detalleRecetaRepository.findByRecetaIdReceta(idReceta);
    }

    @Override
    @Transactional
    public RecetaMedica generarReceta(Integer idAtencion) {
        AtencionMedica atencion = atencionMedicaRepository.findById(idAtencion)
                .orElseThrow(() -> new IllegalArgumentException("La atención médica no existe."));

        if (recetaMedicaRepository.existsByAtencionIdAtencion(idAtencion)) {
            throw new IllegalStateException("Ya existe una receta para esta atención médica.");
        }

        RecetaMedica receta = new RecetaMedica();
        receta.setAtencion(atencion);
        receta.setFechaEmision(LocalDateTime.now());
        receta.setEstadoReceta(VIGENTE);

        return recetaMedicaRepository.save(receta);
    }

    @Override
    @Transactional
    public DetalleReceta agregarDetalle(
            Integer idReceta,
            Integer idMedicamento,
            String dosis,
            String frecuencia,
            String duracion,
            Integer cantidadIndicada
    ) {
        RecetaMedica receta = recetaMedicaRepository.findById(idReceta)
                .orElseThrow(() -> new IllegalArgumentException("La receta médica no existe."));

        Medicamento medicamento = medicamentoRepository.findById(idMedicamento)
                .orElseThrow(() -> new IllegalArgumentException("El medicamento no existe."));

        if (cantidadIndicada == null || cantidadIndicada <= 0) {
            throw new IllegalArgumentException("La cantidad indicada debe ser mayor a cero.");
        }

        DetalleReceta detalle = new DetalleReceta();
        detalle.setReceta(receta);
        detalle.setMedicamento(medicamento);
        detalle.setDosis(dosis);
        detalle.setFrecuencia(frecuencia);
        detalle.setDuracion(duracion);
        detalle.setCantidadIndicada(cantidadIndicada);

        return detalleRecetaRepository.save(detalle);
    }

    // Agrégalo al final de tu clase
    @Override
    @Transactional
    public void eliminarDetalle(Integer idDetalle) {
        if (!detalleRecetaRepository.existsById(idDetalle)) {
            throw new IllegalArgumentException("El medicamento no se encuentra en la receta.");
        }
        detalleRecetaRepository.deleteById(idDetalle);
    }
}