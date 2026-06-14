package com.hospital.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping({"/", "/dashboard"})
    public String index(Model model) {
        model.addAttribute("titulo", "Dashboard Hospitalario");
        return "dashboard/index";
    }

    @GetMapping("/enfermeria/dashboard")
    public String dashboardEnfermeria(Model model) {
        model.addAttribute("titulo", "Dashboard Enfermería");
        return "dashboard/enfermeria";
    }

    @GetMapping("/medico/dashboard")
    public String dashboardMedico(Model model) {
        model.addAttribute("titulo", "Dashboard Médico");
        return "dashboard/medico";
    }

    @GetMapping("/admin/dashboard")
    public String dashboardAdmin(Model model) {
        model.addAttribute("titulo", "Dashboard Administrador");
        return "dashboard/admin";
    }

    @GetMapping("/farmacia/almacen/dashboard")
    public String dashboardAlmacenero(Model model) {
        model.addAttribute("titulo", "Dashboard de Almacén e Inventario");
        return "farmacia/almacen/dashboard";
    }
}