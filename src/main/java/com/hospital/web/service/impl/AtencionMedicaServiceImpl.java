package com.hospital.web.service.impl;

import com.hospital.web.entity.AtencionMedica;
import com.hospital.web.entity.HistoriaClinica;
import com.hospital.web.entity.Medico;
import com.hospital.web.repository.AtencionMedicaRepository;
import com.hospital.web.repository.HistoriaClinicaRepository;
import com.hospital.web.repository.MedicoRepository;
import com.hospital.web.service.AtencionMedicaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AtencionMedicaServiceImpl implements AtencionMedicaService {

    private final AtencionMedicaRepository atencionMedicaRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final MedicoRepository medicoRepository;

    public AtencionMedicaServiceImpl(
            AtencionMedicaRepository atencionMedicaRepository,
            HistoriaClinicaRepository historiaClinicaRepository,
            MedicoRepository medicoRepository
    ) {
        this.atencionMedicaRepository = atencionMedicaRepository;
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.medicoRepository = medicoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtencionMedica> listarTodas() {
        return atencionMedicaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AtencionMedica> buscarPorId(Integer idAtencion) {
        return atencionMedicaRepository.findById(idAtencion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtencionMedica> buscarPorHistoria(Integer idHistoriaClinica) {
        return atencionMedicaRepository.findByHistoriaClinicaIdHistoriaClinica(idHistoriaClinica);
    }

    @Override
    @Transactional
    public AtencionMedica registrarAtencion(
            Integer idHistoriaClinica,
            Integer idMedico,
            String diagnostico,
            String tratamiento,
            String recomendaciones
    ) {
        HistoriaClinica historia = historiaClinicaRepository.findById(idHistoriaClinica)
                .orElseThrow(() -> new IllegalArgumentException("La historia clínica no existe."));

        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new IllegalArgumentException("El médico no existe."));

        if (diagnostico == null || diagnostico.isBlank()) {
            throw new IllegalArgumentException("El diagnóstico es obligatorio.");
        }

        AtencionMedica atencion = new AtencionMedica();
        atencion.setHistoriaClinica(historia);
        atencion.setMedico(medico);
        atencion.setFechaAtencion(LocalDateTime.now());
        atencion.setDiagnostico(diagnostico);
        atencion.setTratamiento(tratamiento);
        atencion.setRecomendaciones(recomendaciones);

        return atencionMedicaRepository.save(atencion);
    }
}