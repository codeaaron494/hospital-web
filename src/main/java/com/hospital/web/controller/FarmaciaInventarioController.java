package com.hospital.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FarmaciaInventarioController {

    @GetMapping("/farmacia/inventario/dashboard")
    public String dashboardInventarioAlmacen(Model model) {
        model.addAttribute("titulo", "Dashboard Inventario - Almacén");
        return "farmacia/inventario/dashboard";
    }

    @GetMapping("/farmacia/inventario/quimico/dashboard")
    public String dashboardInventarioQuimico(Model model) {
        model.addAttribute("titulo", "Dashboard Inventario - Químico Farmacéutico");
        return "farmacia/inventario/quimico-dashboard";
    }

    @GetMapping("/farmacia/inventario/medicamentos/nuevo")
    public String registrarMedicamento(Model model) {
        model.addAttribute("titulo", "Registrar Medicamento");
        return "farmacia/inventario/medicamento-form";
    }

    @GetMapping("/farmacia/inventario/kardex")
    public String consultarKardex(Model model) {
        model.addAttribute("titulo", "Consultar Kardex");
        return "farmacia/inventario/kardex";
    }

    @GetMapping("/farmacia/inventario/balance")
    public String balanceMensual(Model model) {
        model.addAttribute("titulo", "Balance Mensual");
        return "farmacia/inventario/balance";
    }

    @GetMapping("/farmacia/inventario/quimico/auditoria")
    public String auditoriaKardex(Model model) {
        model.addAttribute("titulo", "Auditoría de Kardex");
        return "farmacia/inventario/auditoria";
    }

    @GetMapping("/farmacia/inventario/quimico/exportar")
    public String exportarBalanceDigemid(Model model) {
        model.addAttribute("titulo", "Exportar Balance DIGEMID");
        return "farmacia/inventario/exportar-digemid";
    }
}