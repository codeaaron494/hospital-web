package com.hospital.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FichaAdmisionController {

    @GetMapping("/fichas-admision/nueva")
    public String nuevaFichaAdmision(Model model) {
        model.addAttribute("titulo", "Registrar Ficha de Admisión");
        return "fichas/form";
    }
}