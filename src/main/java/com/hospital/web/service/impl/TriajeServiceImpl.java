package com.hospital.web.service.impl;

import com.hospital.web.entity.FichaAdmision;
import com.hospital.web.entity.HistoriaClinica;
import com.hospital.web.entity.Triaje;
import com.hospital.web.repository.FichaAdmisionRepository;
import com.hospital.web.repository.HistoriaClinicaRepository;
import com.hospital.web.repository.TriajeRepository;
import com.hospital.web.service.TriajeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TriajeServiceImpl implements TriajeService {

    private static final String TRIAJE_REGISTRADO = "TRIAJE_REGISTRADO";

    private final TriajeRepository triajeRepository;
    private final FichaAdmisionRepository fichaAdmisionRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;

    public TriajeServiceImpl(
            TriajeRepository triajeRepository,
            FichaAdmisionRepository fichaAdmisionRepository,
            HistoriaClinicaRepository historiaClinicaRepository
    ) {
        this.triajeRepository = triajeRepository;
        this.fichaAdmisionRepository = fichaAdmisionRepository;
        this.historiaClinicaRepository = historiaClinicaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Triaje> listarTodos() {
        return triajeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Triaje> buscarPorId(Integer idTriaje) {
        return triajeRepository.findById(idTriaje);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Triaje> buscarPorFicha(Integer idFichaAdmision) {
        return triajeRepository.findByFichaAdmisionIdFichaAdmision(idFichaAdmision);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Triaje> buscarPorHistoria(Integer idHistoriaClinica) {
        return triajeRepository.findByHistoriaClinicaIdHistoriaClinica(idHistoriaClinica);
    }

    @Override
    @Transactional
    public Triaje registrarTriaje(
            Integer idFichaAdmision,
            BigDecimal peso,
            BigDecimal talla,
            BigDecimal temperatura,
            String presionArterial,
            Integer frecuenciaCardiaca
    ) {
        FichaAdmision ficha = fichaAdmisionRepository.findById(idFichaAdmision)
                .orElseThrow(() -> new IllegalArgumentException("La ficha de admisión no existe."));

        if (triajeRepository.existsByFichaAdmisionIdFichaAdmision(idFichaAdmision)) {
            throw new IllegalStateException("Ya existe triaje registrado para esta ficha.");
        }

        HistoriaClinica historia = historiaClinicaRepository
                .findByPacienteIdPaciente(ficha.getPaciente().getIdPaciente())
                .orElseThrow(() -> new IllegalStateException("El paciente no tiene historia clínica."));

        Triaje triaje = new Triaje();
        triaje.setFichaAdmision(ficha);
        triaje.setHistoriaClinica(historia);
        triaje.setPeso(peso);
        triaje.setTalla(talla);
        triaje.setTemperatura(temperatura);
        triaje.setPresionArterial(presionArterial);
        triaje.setFrecuenciaCardiaca(frecuenciaCardiaca);

        ficha.setEstadoFicha(TRIAJE_REGISTRADO);
        fichaAdmisionRepository.save(ficha);

        return triajeRepository.save(triaje);
    }
}