package com.hospital.web.controller;

import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.BalanceMensualService;
import com.hospital.web.service.KardexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/farmacia/inventario/quimico")
public class InventarioQuimicoController {

    private final KardexService kardexService;
    private final BalanceMensualService balanceMensualService;
    private final UsuarioRepository usuarioRepository;

    public InventarioQuimicoController(
            KardexService kardexService,
            BalanceMensualService balanceMensualService,
            UsuarioRepository usuarioRepository
    ) {
        this.kardexService = kardexService;
        this.balanceMensualService = balanceMensualService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/kardex")
    public String listarKardex(Model model) {
        model.addAttribute("kardexList", kardexService.listarTodos());
        return "farmacia/inventario/quimico/kardex";
    }

    @GetMapping("/kardex/{id}")
    public String detalleKardex(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return kardexService.buscarPorId(id)
                .map(kardex -> {
                    model.addAttribute("kardex", kardex);
                    model.addAttribute("movimientos", kardexService.listarMovimientos(id));
                    return "farmacia/inventario/quimico/kardex-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "El Kardex no existe.");
                    return "redirect:/farmacia/inventario/quimico/kardex";
                });
    }

    @GetMapping("/balances")
    public String listarBalances(
            @RequestParam(required = false) String estado,
            Model model
    ) {
        if (estado != null && !estado.isBlank()) {
            model.addAttribute("balances", balanceMensualService.listarPorEstado(estado.trim()));
            model.addAttribute("estadoBuscado", estado.trim());
        } else {
            model.addAttribute("balances", balanceMensualService.listarTodos());
        }

        return "farmacia/inventario/quimico/balances";
    }

    @GetMapping("/balances/{id}")
    public String detalleBalance(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return balanceMensualService.buscarPorId(id)
                .map(balance -> {
                    model.addAttribute("balance", balance);
                    model.addAttribute("detalles", balanceMensualService.listarDetalle(id));
                    model.addAttribute("observaciones", balanceMensualService.listarObservaciones(id));
                    return "farmacia/inventario/quimico/balance-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "El balance mensual no existe.");
                    return "redirect:/farmacia/inventario/quimico/balances";
                });
    }

    @PostMapping("/balances/{id}/aprobar")
    public String aprobarBalance(
            @PathVariable Integer id,
            @RequestParam(required = false) String observacion,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idQuimico = obtenerIdUsuario(principal, "quimico");
            balanceMensualService.aprobarBalance(
                    id,
                    idQuimico,
                    observacion != null && !observacion.isBlank()
                            ? observacion
                            : "Balance aprobado por químico farmacéutico."
            );
            redirectAttributes.addFlashAttribute("success", "Balance aprobado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/farmacia/inventario/quimico/balances/" + id;
    }

    @PostMapping("/balances/{id}/observar")
    public String observarBalance(
            @PathVariable Integer id,
            @RequestParam String descripcion,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idQuimico = obtenerIdUsuario(principal, "quimico");
            balanceMensualService.observarBalance(id, idQuimico, descripcion);
            redirectAttributes.addFlashAttribute("success", "Balance observado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/farmacia/inventario/quimico/balances/" + id;
    }

    @PostMapping("/balances/{id}/exportar-digemid")
    public String exportarDigemid(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            balanceMensualService.exportarDigemid(id);
            redirectAttributes.addFlashAttribute("success", "Balance exportado a DIGEMID simulado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/farmacia/inventario/quimico/balances/" + id;
    }

    @PostMapping("/balances/{id}/digemid-conforme")
    public String conformidadDigemid(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            balanceMensualService.registrarConformidadDigemid(id);
            redirectAttributes.addFlashAttribute("success", "Conformidad DIGEMID registrada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/farmacia/inventario/quimico/balances/" + id;
    }

    @PostMapping("/balances/{id}/digemid-observar")
    public String observacionDigemid(
            @PathVariable Integer id,
            @RequestParam String descripcion,
            RedirectAttributes redirectAttributes
    ) {
        try {
            balanceMensualService.registrarObservacionDigemid(id, descripcion);
            redirectAttributes.addFlashAttribute("success", "Observación DIGEMID simulada registrada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/farmacia/inventario/quimico/balances/" + id;
    }

    // --- NUEVO: RUTA ESPECÍFICA PARA QUE EL QUÍMICO SUBSANE ---
    @PostMapping("/balances/observaciones/{id}/subsanar")
    public String subsanarObservacion(
            @PathVariable Integer id,
            @RequestParam Integer idBalanceMensual,
            RedirectAttributes redirectAttributes
    ) {
        try {
            balanceMensualService.subsanarObservacion(id);
            redirectAttributes.addFlashAttribute("success", "Observación marcada como subsanada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        // Redirige a la vista del Químico (evitando el 403)
        return "redirect:/farmacia/inventario/quimico/balances/" + idBalanceMensual;
    }

    private Integer obtenerIdUsuario(Principal principal, String usernameFallback) {
        String username = principal != null ? principal.getName() : usernameFallback;
        return usuarioRepository.findByUsername(username)
                .orElseGet(() -> usuarioRepository.findByUsername(usernameFallback)
                        .orElseThrow(() -> new IllegalStateException("Usuario no encontrado.")))
                .getIdUsuario();
    }
}