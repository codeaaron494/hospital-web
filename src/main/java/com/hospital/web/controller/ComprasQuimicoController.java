package com.hospital.web.controller;

import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.OrdenCompraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/farmacia/compras/quimico")
public class ComprasQuimicoController {

    private final OrdenCompraService ordenCompraService;
    private final UsuarioRepository usuarioRepository;

    public ComprasQuimicoController(
            OrdenCompraService ordenCompraService,
            UsuarioRepository usuarioRepository
    ) {
        this.ordenCompraService = ordenCompraService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/dashboard")
    public String dashboardQuimico() {
        return "farmacia/compras/quimico/dashboard";
    }

    @GetMapping("/ordenes")
    public String listarOrdenes(
            @RequestParam(required = false) String estado,
            Model model
    ) {
        if (estado != null && !estado.isBlank()) {
            model.addAttribute("ordenes", ordenCompraService.listarPorEstado(estado.trim()));
            model.addAttribute("estadoBuscado", estado.trim());
        } else {
            model.addAttribute("ordenes", ordenCompraService.listarTodas());
        }

        return "farmacia/compras/quimico/ordenes";
    }

    @GetMapping("/ordenes/{id}")
    public String detalleOrden(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return ordenCompraService.buscarPorId(id)
                .map(orden -> {
                    model.addAttribute("orden", orden);
                    model.addAttribute("detalles", ordenCompraService.listarDetalle(id));
                    return "farmacia/compras/quimico/orden-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "La orden de compra no existe.");
                    return "redirect:/farmacia/compras/quimico/ordenes";
                });
    }

    @PostMapping("/ordenes/{id}/autorizar")
    public String autorizarOrden(
            @PathVariable Integer id,
            @RequestParam(required = false) String observacion,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idQuimico = obtenerIdUsuario(principal, "quimico");

            ordenCompraService.autorizarOrden(
                    id,
                    idQuimico,
                    observacion != null && !observacion.isBlank()
                            ? observacion
                            : "Orden autorizada por químico farmacéutico."
            );

            redirectAttributes.addFlashAttribute("success", "Orden autorizada correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/compras/quimico/ordenes/" + id;
    }

    @PostMapping("/ordenes/{id}/rechazar")
    public String rechazarOrden(
            @PathVariable Integer id,
            @RequestParam(required = false) String observacion,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idQuimico = obtenerIdUsuario(principal, "quimico");

            ordenCompraService.rechazarOrden(
                    id,
                    idQuimico,
                    observacion != null && !observacion.isBlank()
                            ? observacion
                            : "Orden rechazada por químico farmacéutico."
            );

            redirectAttributes.addFlashAttribute("success", "Orden rechazada correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/compras/quimico/ordenes/" + id;
    }

    private Integer obtenerIdUsuario(Principal principal, String usernameFallback) {
        String username = principal != null ? principal.getName() : usernameFallback;

        return usuarioRepository.findByUsername(username)
                .orElseGet(() -> usuarioRepository.findByUsername(usernameFallback)
                        .orElseThrow(() -> new IllegalStateException("Usuario no encontrado.")))
                .getIdUsuario();
    }
}