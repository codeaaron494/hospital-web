package com.hospital.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FarmaciaComprasController {

    @GetMapping("/farmacia/compras/dashboard")
    public String dashboardAlmacenero(Model model) {
        model.addAttribute("titulo", "Dashboard Compras - Almacén");
        return "farmacia/compras/dashboard";
    }

    @GetMapping("/farmacia/compras/quimico/dashboard")
    public String dashboardQuimico(Model model) {
        model.addAttribute("titulo", "Dashboard Compras - Químico Farmacéutico");
        return "farmacia/compras/quimico-dashboard";
    }

    @GetMapping("/farmacia/compras/cobranza/dashboard")
    public String dashboardCobranza(Model model) {
        model.addAttribute("titulo", "Dashboard Compras - Cobranza");
        return "farmacia/compras/cobranza-dashboard";
    }

    @GetMapping("/farmacia/compras/inventario")
    public String consultarInventario(Model model) {
        model.addAttribute("titulo", "Consultar Inventario");
        return "farmacia/compras/inventario";
    }

    @GetMapping("/farmacia/compras/ordenes/nueva")
    public String nuevaOrdenCompra(Model model) {
        model.addAttribute("titulo", "Generar Orden de Compra");
        return "farmacia/compras/orden-nueva";
    }

    @GetMapping("/farmacia/compras/ordenes/revision")
    public String revisionOrdenes(Model model) {
        model.addAttribute("titulo", "Revisión de Órdenes de Compra");
        return "farmacia/compras/ordenes-revision";
    }

    @GetMapping("/farmacia/compras/recepcion")
    public String recepcionMercaderia(Model model) {
        model.addAttribute("titulo", "Recepción de Mercadería");
        return "farmacia/compras/recepcion";
    }

    @GetMapping("/farmacia/compras/comprobantes")
    public String comprobantesPago(Model model) {
        model.addAttribute("titulo", "Comprobantes de Pago");
        return "farmacia/compras/comprobantes";
    }

    @GetMapping("/farmacia/compras/pagos")
    public String pagosProveedor(Model model) {
        model.addAttribute("titulo", "Pagos a Proveedor");
        return "farmacia/compras/pagos";
    }
}