package com.hospital.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnfermeriaController {

    @GetMapping("/enfermeria/fichas")
    public String consultarFichas(Model model) {
        model.addAttribute("titulo", "Fichas de Admisión");
        return "enfermeria/fichas";
    }

    @GetMapping("/enfermeria/triaje")
    public String triaje(Model model) {
        model.addAttribute("titulo", "Triaje de Paciente");
        return "enfermeria/triaje";
    }
}