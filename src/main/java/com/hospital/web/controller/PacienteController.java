package com.hospital.web.controller;

import com.hospital.web.entity.Paciente;
import com.hospital.web.service.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/nuevo")
    public String nuevoPaciente(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "pacientes/form";
    }

    @PostMapping("/guardar")
    public String guardarPaciente(
            @ModelAttribute Paciente paciente,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (pacienteService.existeDni(paciente.getDniPaciente())) {
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Ya existe un paciente registrado con ese DNI."
                );
                return "redirect:/pacientes/nuevo";
            }

            pacienteService.guardar(paciente);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Paciente registrado correctamente."
            );

            return "redirect:/citas/nueva";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/pacientes/nuevo";
        }
    }

    @GetMapping("/buscar")
    public String buscarPacientePorDni(
            @RequestParam String dni,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return pacienteService.buscarPorDni(dni)
                .map(paciente -> {
                    model.addAttribute("paciente", paciente);
                    return "pacientes/detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute(
                            "error",
                            "No existe paciente con DNI " + dni + ". Registre al paciente."
                    );
                    return "redirect:/pacientes/nuevo";
                });
    }
}