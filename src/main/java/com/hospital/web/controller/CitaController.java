package com.hospital.web.controller;

import com.hospital.web.entity.CitaMedica;
import com.hospital.web.service.AgendaMedicaService;
import com.hospital.web.service.CitaService;
import com.hospital.web.service.PacienteService;
import com.hospital.web.repository.EspecialidadRepository;
import com.hospital.web.repository.MedicoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaService citaService;
    private final PacienteService pacienteService;
    private final AgendaMedicaService agendaMedicaService;
    private final EspecialidadRepository especialidadRepository;
    private final MedicoRepository medicoRepository;

    public CitaController(
            CitaService citaService,
            PacienteService pacienteService,
            AgendaMedicaService agendaMedicaService,
            EspecialidadRepository especialidadRepository,
            MedicoRepository medicoRepository
    ) {
        this.citaService = citaService;
        this.pacienteService = pacienteService;
        this.agendaMedicaService = agendaMedicaService;
        this.especialidadRepository = especialidadRepository;
        this.medicoRepository = medicoRepository;
    }

    @GetMapping
    public String listarCitas(
            @RequestParam(required = false) String dni,
            Model model
    ) {
        List<CitaMedica> citas;

        if (dni != null && !dni.isBlank()) {
            citas = citaService.buscarPorDniPaciente(dni.trim());
            model.addAttribute("dniBuscado", dni.trim());
        } else {
            citas = citaService.listarTodas();
        }

        model.addAttribute("citas", citas);
        return "citas/index";
    }

    @GetMapping("/nueva")
    public String nuevaCita(Model model) {
        model.addAttribute("pacientes", pacienteService.listarTodos());
        model.addAttribute("especialidades", especialidadRepository.findAll());
        model.addAttribute("medicos", medicoRepository.findAll());
        model.addAttribute("agendasDisponibles", agendaMedicaService.listarDisponibles());
        return "citas/form";
    }

    @PostMapping("/guardar")
    public String guardarCita(
            @RequestParam Integer idPaciente,
            @RequestParam Integer idMedico,
            @RequestParam Integer idEspecialidad,
            @RequestParam Integer idAgenda,
            @RequestParam(required = false) String motivoConsulta,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            String username = principal != null ? principal.getName() : "recepcionista";

            CitaMedica cita = citaService.registrarCita(
                    idPaciente,
                    idMedico,
                    idEspecialidad,
                    idAgenda,
                    motivoConsulta,
                    username
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Cita registrada correctamente. Código: " + cita.getIdCita()
            );

            return "redirect:/citas/" + cita.getIdCita();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/citas/nueva";
        }
    }

    @GetMapping("/{id}")
    public String detalleCita(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return citaService.buscarPorId(id)
                .map(cita -> {
                    model.addAttribute("cita", cita);
                    return "citas/detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "La cita no existe.");
                    return "redirect:/citas";
                });
    }

    @GetMapping("/{id}/reprogramar")
    public String formularioReprogramar(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return citaService.buscarPorId(id)
                .map(cita -> {
                    model.addAttribute("cita", cita);
                    model.addAttribute(
                            "agendasDisponibles",
                            agendaMedicaService.listarDisponiblesPorMedico(
                                    cita.getMedico().getIdMedico()
                            )
                    );
                    return "citas/reprogramar";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "La cita no existe.");
                    return "redirect:/citas";
                });
    }

    @PostMapping("/{id}/reprogramar")
    public String reprogramarCita(
            @PathVariable Integer id,
            @RequestParam Integer nuevoIdAgenda,
            RedirectAttributes redirectAttributes
    ) {
        try {
            CitaMedica cita = citaService.reprogramarCita(id, nuevoIdAgenda);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Cita reprogramada correctamente."
            );

            return "redirect:/citas/" + cita.getIdCita();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/citas/" + id + "/reprogramar";
        }
    }

    @GetMapping("/{id}/anular")
    public String formularioAnular(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return citaService.buscarPorId(id)
                .map(cita -> {
                    model.addAttribute("cita", cita);
                    return "citas/anular";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "La cita no existe.");
                    return "redirect:/citas";
                });
    }

    @PostMapping("/{id}/anular")
    public String anularCita(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            citaService.anularCita(id);
            redirectAttributes.addFlashAttribute("success", "Cita anulada correctamente.");
            return "redirect:/citas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/citas/" + id + "/anular";
        }
    }
}