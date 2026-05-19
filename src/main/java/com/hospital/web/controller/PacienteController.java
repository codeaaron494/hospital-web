package com.hospital.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PacienteController {

    @GetMapping("/pacientes/nuevo")
    public String nuevoPaciente(Model model) {
        model.addAttribute("titulo", "Registrar Paciente");
        return "pacientes/form";
    }
}