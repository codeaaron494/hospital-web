package com.hospital.web.service.impl;

import com.hospital.web.entity.HistoriaClinica;
import com.hospital.web.entity.Paciente;
import com.hospital.web.repository.HistoriaClinicaRepository;
import com.hospital.web.repository.PacienteRepository;
import com.hospital.web.service.HistoriaClinicaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HistoriaClinicaServiceImpl implements HistoriaClinicaService {

    private static final String ACTIVA = "ACTIVA";

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final PacienteRepository pacienteRepository;

    public HistoriaClinicaServiceImpl(
            HistoriaClinicaRepository historiaClinicaRepository,
            PacienteRepository pacienteRepository
    ) {
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriaClinica> listarTodas() {
        return historiaClinicaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriaClinica> buscarPorId(Integer idHistoriaClinica) {
        return historiaClinicaRepository.findById(idHistoriaClinica);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriaClinica> buscarPorDniPaciente(String dniPaciente) {
        return historiaClinicaRepository.findByPacienteDniPaciente(dniPaciente);
    }

    @Override
    @Transactional
    public HistoriaClinica obtenerOCrearPorPaciente(Integer idPaciente) {
        return historiaClinicaRepository.findByPacienteIdPaciente(idPaciente)
                .orElseGet(() -> {
                    Paciente paciente = pacienteRepository.findById(idPaciente)
                            .orElseThrow(() -> new IllegalArgumentException("El paciente no existe."));

                    HistoriaClinica historia = new HistoriaClinica();
                    historia.setPaciente(paciente);
                    historia.setFechaApertura(LocalDate.now());
                    historia.setEstadoHistoria(ACTIVA);

                    return historiaClinicaRepository.save(historia);
                });
    }
}