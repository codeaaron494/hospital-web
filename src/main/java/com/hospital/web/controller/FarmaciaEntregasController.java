package com.hospital.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FarmaciaEntregasController {

    @GetMapping("/farmacia/entregas/dashboard")
    public String dashboardEntregas(Model model) {
        model.addAttribute("titulo", "Dashboard Entrega de Medicamentos");
        return "farmacia/entregas/dashboard";
    }

    @GetMapping("/farmacia/entregas/recetas")
    public String consultarRecetas(Model model) {
        model.addAttribute("titulo", "Consultar Receta Médica");
        return "farmacia/entregas/recetas";
    }

    @GetMapping("/farmacia/entregas/despacho")
    public String registrarDespacho(Model model) {
        model.addAttribute("titulo", "Registrar Despacho de Medicamentos");
        return "farmacia/entregas/despacho";
    }

    @GetMapping("/farmacia/entregas/kardex")
    public String consultarKardexEntregas(Model model) {
        model.addAttribute("titulo", "Kardex de Entregas");
        return "farmacia/entregas/kardex";
    }
}