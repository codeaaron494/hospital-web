package com.hospital.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MedicoController {

    @GetMapping("/medico/historias")
    public String consultarHistorias(Model model) {
        model.addAttribute("titulo", "Historias Clínicas");
        return "medico/historias";
    }

    @GetMapping("/medico/atencion")
    public String atencionMedica(Model model) {
        model.addAttribute("titulo", "Atención Médica");
        return "medico/atencion";
    }

    @GetMapping("/medico/receta")
    public String recetaMedica(Model model) {
        model.addAttribute("titulo", "Generar Receta Médica");
        return "medico/receta";
    }
}