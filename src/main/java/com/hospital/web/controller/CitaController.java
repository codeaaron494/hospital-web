package com.hospital.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CitaController {

    @GetMapping("/citas")
    public String consultarCitas(Model model) {
        model.addAttribute("titulo", "Consultar Citas");
        return "citas/index";
    }

    @GetMapping("/citas/nueva")
    public String nuevaCita(Model model) {
        model.addAttribute("titulo", "Registrar Nueva Cita");
        return "citas/form";
    }

    @GetMapping("/citas/reprogramar")
    public String reprogramarCita(Model model) {
        model.addAttribute("titulo", "Reprogramar Cita");
        return "citas/reprogramar";
    }

    @GetMapping("/citas/anular")
    public String anularCita(Model model) {
        model.addAttribute("titulo", "Anular Cita");
        return "citas/anular";
    }

    @GetMapping("/citas/detalle")
    public String detalleCita(Model model) {
        model.addAttribute("titulo", "Detalle de Cita");
        return "citas/detalle";
    }
}