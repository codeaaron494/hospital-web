package com.hospital.web.service.impl;

import com.hospital.web.entity.Paciente;
import com.hospital.web.repository.PacienteRepository;
import com.hospital.web.service.PacienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paciente> buscarPorId(Integer idPaciente) {
        return pacienteRepository.findById(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paciente> buscarPorDni(String dniPaciente) {
        return pacienteRepository.findByDniPaciente(dniPaciente);
    }

    @Override
    @Transactional
    public Paciente guardar(Paciente paciente) {
        if (paciente.getDniPaciente() == null || !paciente.getDniPaciente().matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener 8 dígitos.");
        }

        if (paciente.getNombresPaciente() == null || paciente.getNombresPaciente().isBlank()) {
            throw new IllegalArgumentException("Los nombres del paciente son obligatorios.");
        }

        if (paciente.getApellidosPaciente() == null || paciente.getApellidosPaciente().isBlank()) {
            throw new IllegalArgumentException("Los apellidos del paciente son obligatorios.");
        }

        if (paciente.getEstadoPaciente() == null || paciente.getEstadoPaciente().isBlank()) {
            paciente.setEstadoPaciente("ACTIVO");
        }

        return pacienteRepository.save(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeDni(String dniPaciente) {
        return pacienteRepository.existsByDniPaciente(dniPaciente);
    }
}