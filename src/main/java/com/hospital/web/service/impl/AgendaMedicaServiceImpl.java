package com.hospital.web.service.impl;

import com.hospital.web.entity.AgendaMedica;
import com.hospital.web.repository.AgendaMedicaRepository;
import com.hospital.web.service.AgendaMedicaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AgendaMedicaServiceImpl implements AgendaMedicaService {

    private static final String DISPONIBLE = "DISPONIBLE";
    private static final String OCUPADO = "OCUPADO";

    private final AgendaMedicaRepository agendaMedicaRepository;

    public AgendaMedicaServiceImpl(AgendaMedicaRepository agendaMedicaRepository) {
        this.agendaMedicaRepository = agendaMedicaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendaMedica> listarTodos() {
        return agendaMedicaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendaMedica> listarDisponibles() {
        return agendaMedicaRepository.findByEstadoHorario(DISPONIBLE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendaMedica> listarDisponiblesPorMedico(Integer idMedico) {
        return agendaMedicaRepository.findByMedicoIdMedicoAndEstadoHorario(idMedico, DISPONIBLE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendaMedica> listarDisponiblesPorEspecialidad(Integer idEspecialidad) {
        return agendaMedicaRepository.findByMedicoEspecialidadIdEspecialidadAndEstadoHorario(
                idEspecialidad,
                DISPONIBLE
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgendaMedica> buscarPorId(Integer idAgenda) {
        return agendaMedicaRepository.findById(idAgenda);
    }

    @Override
    @Transactional
    public void ocuparHorario(Integer idAgenda) {
        AgendaMedica agenda = agendaMedicaRepository.findById(idAgenda)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado."));

        if (!DISPONIBLE.equals(agenda.getEstadoHorario())) {
            throw new IllegalStateException("El horario seleccionado no está disponible.");
        }

        agenda.setEstadoHorario(OCUPADO);
        agendaMedicaRepository.save(agenda);
    }

    @Override
    @Transactional
    public void liberarHorario(Integer idAgenda) {
        AgendaMedica agenda = agendaMedicaRepository.findById(idAgenda)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado."));

        agenda.setEstadoHorario(DISPONIBLE);
        agendaMedicaRepository.save(agenda);
    }
}