package com.hospital.web.service.impl;

import com.hospital.web.entity.CitaMedica;
import com.hospital.web.entity.FichaAdmision;
import com.hospital.web.repository.CitaMedicaRepository;
import com.hospital.web.repository.FichaAdmisionRepository;
import com.hospital.web.service.FichaAdmisionService;
import com.hospital.web.service.HistoriaClinicaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FichaAdmisionServiceImpl implements FichaAdmisionService {

    private static final String ANULADA = "ANULADA";
    private static final String CONFIRMADA = "CONFIRMADA";
    private static final String REGISTRADA = "REGISTRADA";

    private final FichaAdmisionRepository fichaAdmisionRepository;
    private final CitaMedicaRepository citaMedicaRepository;
    private final HistoriaClinicaService historiaClinicaService;

    public FichaAdmisionServiceImpl(
            FichaAdmisionRepository fichaAdmisionRepository,
            CitaMedicaRepository citaMedicaRepository,
            HistoriaClinicaService historiaClinicaService
    ) {
        this.fichaAdmisionRepository = fichaAdmisionRepository;
        this.citaMedicaRepository = citaMedicaRepository;
        this.historiaClinicaService = historiaClinicaService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FichaAdmision> listarTodas() {
        return fichaAdmisionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FichaAdmision> buscarPorId(Integer idFichaAdmision) {
        return fichaAdmisionRepository.findById(idFichaAdmision);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FichaAdmision> buscarPorDniPaciente(String dniPaciente) {
        return fichaAdmisionRepository.findByPacienteDniPaciente(dniPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FichaAdmision> buscarPorEstado(String estadoFicha) {
        return fichaAdmisionRepository.findByEstadoFicha(estadoFicha);
    }

    @Override
    @Transactional
    public FichaAdmision registrarFicha(Integer idCita, String tipoAdmision, String prioridad) {
        CitaMedica cita = citaMedicaRepository.findById(idCita)
                .orElseThrow(() -> new IllegalArgumentException("La cita médica no existe."));

        if (ANULADA.equals(cita.getEstadoCita())) {
            throw new IllegalStateException("No se puede registrar ficha para una cita anulada.");
        }

        if (fichaAdmisionRepository.existsByCitaIdCita(idCita)) {
            throw new IllegalStateException("Ya existe una ficha de admisión para esta cita.");
        }

        historiaClinicaService.obtenerOCrearPorPaciente(cita.getPaciente().getIdPaciente());

        FichaAdmision ficha = new FichaAdmision();
        ficha.setCita(cita);
        ficha.setPaciente(cita.getPaciente());
        ficha.setFechaAdmision(LocalDateTime.now());
        ficha.setTipoAdmision(tipoAdmision);
        ficha.setPrioridad(prioridad);
        ficha.setEstadoFicha(REGISTRADA);

        cita.setEstadoCita(CONFIRMADA);
        citaMedicaRepository.save(cita);

        return fichaAdmisionRepository.save(ficha);
    }
}