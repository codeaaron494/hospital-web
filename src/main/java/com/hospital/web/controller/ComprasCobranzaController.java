package com.hospital.web.controller;

import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.ComprobantePagoService;
import com.hospital.web.service.OrdenCompraService;
import com.hospital.web.service.PagoComprobanteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/farmacia/compras/cobranza")
public class ComprasCobranzaController {

    private final ComprobantePagoService comprobantePagoService;
    private final PagoComprobanteService pagoComprobanteService;
    private final OrdenCompraService ordenCompraService;
    private final UsuarioRepository usuarioRepository;

    public ComprasCobranzaController(
            ComprobantePagoService comprobantePagoService,
            PagoComprobanteService pagoComprobanteService,
            OrdenCompraService ordenCompraService,
            UsuarioRepository usuarioRepository
    ) {
        this.comprobantePagoService = comprobantePagoService;
        this.pagoComprobanteService = pagoComprobanteService;
        this.ordenCompraService = ordenCompraService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/dashboard")
    public String dashboardCobranza() {
        return "farmacia/compras/cobranza/dashboard";
    }

    @GetMapping("/comprobantes")
    public String listarComprobantes(
            @RequestParam(required = false) String estado,
            Model model
    ) {
        if (estado != null && !estado.isBlank()) {
            model.addAttribute("comprobantes", comprobantePagoService.listarPorEstado(estado.trim()));
            model.addAttribute("estadoBuscado", estado.trim());
        } else {
            model.addAttribute("comprobantes", comprobantePagoService.listarTodos());
        }

        return "farmacia/compras/cobranza/comprobantes";
    }

    @GetMapping("/comprobantes/nuevo")
    public String nuevoComprobante(Model model) {
        model.addAttribute("ordenes", ordenCompraService.listarTodas());
        return "farmacia/compras/cobranza/comprobante-form";
    }

    @PostMapping("/comprobantes/guardar")
    public String guardarComprobante(
            @RequestParam Integer idOrdenCompra,
            @RequestParam String numeroComprobante,
            @RequestParam String tipoComprobante,
            @RequestParam String fechaEmision,
            @RequestParam BigDecimal subtotal,
            @RequestParam BigDecimal igv,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idCobranza = obtenerIdUsuario(principal, "cobranza");

            var comprobante = comprobantePagoService.registrarComprobante(
                    idOrdenCompra,
                    idCobranza,
                    numeroComprobante,
                    tipoComprobante,
                    LocalDate.parse(fechaEmision),
                    subtotal,
                    igv
            );

            redirectAttributes.addFlashAttribute("success", "Comprobante registrado correctamente.");

            return "redirect:/farmacia/compras/cobranza/comprobantes/" + comprobante.getIdComprobantePago();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/farmacia/compras/cobranza/comprobantes/nuevo";
        }
    }

    @GetMapping("/comprobantes/{id}")
    public String detalleComprobante(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return comprobantePagoService.buscarPorId(id)
                .map(comprobante -> {
                    model.addAttribute("comprobante", comprobante);
                    model.addAttribute("pagos", pagoComprobanteService.listarPorComprobante(id));
                    return "farmacia/compras/cobranza/comprobante-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "El comprobante no existe.");
                    return "redirect:/farmacia/compras/cobranza/comprobantes";
                });
    }

    @PostMapping("/comprobantes/{id}/pagar")
    public String pagarComprobante(
            @PathVariable Integer id,
            @RequestParam BigDecimal montoPagado,
            @RequestParam String medioPago,
            @RequestParam(required = false) String observacion,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idCobranza = obtenerIdUsuario(principal, "cobranza");

            pagoComprobanteService.pagarComprobante(
                    id,
                    idCobranza,
                    montoPagado,
                    medioPago,
                    observacion
            );

            redirectAttributes.addFlashAttribute("success", "Comprobante pagado correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/compras/cobranza/comprobantes/" + id;
    }

    @PostMapping("/comprobantes/{id}/anular")
    public String anularComprobante(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            comprobantePagoService.anularComprobante(id);
            redirectAttributes.addFlashAttribute("success", "Comprobante anulado correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/compras/cobranza/comprobantes/" + id;
    }

    private Integer obtenerIdUsuario(Principal principal, String usernameFallback) {
        String username = principal != null ? principal.getName() : usernameFallback;

        return usuarioRepository.findByUsername(username)
                .orElseGet(() -> usuarioRepository.findByUsername(usernameFallback)
                        .orElseThrow(() -> new IllegalStateException("Usuario no encontrado.")))
                .getIdUsuario();
    }
}