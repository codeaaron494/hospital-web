package com.hospital.web.service.impl;

import com.hospital.web.entity.*;
import com.hospital.web.repository.*;
import com.hospital.web.service.CitaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CitaServiceImpl implements CitaService {

    private static final String ACTIVO = "ACTIVO";
    private static final String DISPONIBLE = "DISPONIBLE";
    private static final String OCUPADO = "OCUPADO";
    private static final String PROGRAMADA = "PROGRAMADA";
    private static final String REPROGRAMADA = "REPROGRAMADA";
    private static final String ANULADA = "ANULADA";

    private final CitaMedicaRepository citaMedicaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final AgendaMedicaRepository agendaMedicaRepository;
    private final UsuarioRepository usuarioRepository;

    public CitaServiceImpl(
            CitaMedicaRepository citaMedicaRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository,
            EspecialidadRepository especialidadRepository,
            AgendaMedicaRepository agendaMedicaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.citaMedicaRepository = citaMedicaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.especialidadRepository = especialidadRepository;
        this.agendaMedicaRepository = agendaMedicaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaMedica> listarTodas() {
        return citaMedicaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaMedica> buscarPorDniPaciente(String dniPaciente) {
        return citaMedicaRepository.findByPacienteDniPaciente(dniPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaMedica> buscarPorPaciente(Integer idPaciente) {
        return citaMedicaRepository.findByPacienteIdPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CitaMedica> buscarPorId(Integer idCita) {
        return citaMedicaRepository.findById(idCita);
    }

    @Override
    @Transactional
    public CitaMedica registrarCita(
            Integer idPaciente,
            Integer idMedico,
            Integer idEspecialidad,
            Integer idAgenda,
            String motivoConsulta,
            String username
    ) {
        Paciente paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new IllegalArgumentException("El paciente no existe."));

        if (!ACTIVO.equals(paciente.getEstadoPaciente())) {
            throw new IllegalStateException("El paciente no se encuentra activo.");
        }

        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new IllegalArgumentException("El médico no existe."));

        Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new IllegalArgumentException("La especialidad no existe."));

        if (!medico.getEspecialidad().getIdEspecialidad().equals(especialidad.getIdEspecialidad())) {
            throw new IllegalStateException("El médico no pertenece a la especialidad seleccionada.");
        }

        AgendaMedica agenda = agendaMedicaRepository.findById(idAgenda)
                .orElseThrow(() -> new IllegalArgumentException("El horario no existe."));

        if (!DISPONIBLE.equals(agenda.getEstadoHorario())) {
            throw new IllegalStateException("El horario seleccionado no está disponible.");
        }

        if (!agenda.getMedico().getIdMedico().equals(medico.getIdMedico())) {
            throw new IllegalStateException("El horario no pertenece al médico seleccionado.");
        }

        if (citaMedicaRepository.existsByAgendaIdAgendaAndEstadoCitaNot(
                idAgenda,
                ANULADA
        )) {
            throw new IllegalStateException("Ya existe una cita registrada para este horario.");
        }

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseGet(() -> usuarioRepository.findByUsername("recepcionista")
                        .orElseThrow(() -> new IllegalStateException("Usuario de registro no encontrado.")));

        CitaMedica cita = new CitaMedica();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setEspecialidad(especialidad);
        cita.setAgenda(agenda);
        cita.setUsuario(usuario);
        cita.setFechaCita(agenda.getFechaAgenda());
        cita.setHoraCita(agenda.getHoraInicio());
        cita.setEstadoCita(PROGRAMADA);
        cita.setMotivoConsulta(motivoConsulta);

        agenda.setEstadoHorario(OCUPADO);
        agendaMedicaRepository.save(agenda);

        return citaMedicaRepository.save(cita);
    }

    @Override
    @Transactional
    public CitaMedica reprogramarCita(Integer idCita, Integer nuevoIdAgenda) {
        CitaMedica cita = citaMedicaRepository.findById(idCita)
                .orElseThrow(() -> new IllegalArgumentException("La cita no existe."));

        if (ANULADA.equals(cita.getEstadoCita())) {
            throw new IllegalStateException("No se puede reprogramar una cita anulada.");
        }

        AgendaMedica agendaAnterior = cita.getAgenda();
        AgendaMedica agendaNueva = agendaMedicaRepository.findById(nuevoIdAgenda)
                .orElseThrow(() -> new IllegalArgumentException("El nuevo horario no existe."));

        if (!DISPONIBLE.equals(agendaNueva.getEstadoHorario())) {
            throw new IllegalStateException("El nuevo horario no está disponible.");
        }

        if (!agendaNueva.getMedico().getIdMedico().equals(cita.getMedico().getIdMedico())) {
            throw new IllegalStateException("El nuevo horario debe pertenecer al mismo médico.");
        }

        agendaAnterior.setEstadoHorario(DISPONIBLE);
        agendaNueva.setEstadoHorario(OCUPADO);

        agendaMedicaRepository.save(agendaAnterior);
        agendaMedicaRepository.save(agendaNueva);

        cita.setAgenda(agendaNueva);
        cita.setFechaCita(agendaNueva.getFechaAgenda());
        cita.setHoraCita(agendaNueva.getHoraInicio());
        cita.setEstadoCita(REPROGRAMADA);

        return citaMedicaRepository.save(cita);
    }

    @Override
    @Transactional
    public void anularCita(Integer idCita) {
        CitaMedica cita = citaMedicaRepository.findById(idCita)
                .orElseThrow(() -> new IllegalArgumentException("La cita no existe."));

        if (ANULADA.equals(cita.getEstadoCita())) {
            return;
        }

        AgendaMedica agenda = cita.getAgenda();
        agenda.setEstadoHorario(DISPONIBLE);
        agendaMedicaRepository.save(agenda);

        cita.setEstadoCita(ANULADA);
        citaMedicaRepository.save(cita);
    }
}