package com.hospital.web.controller;

import com.hospital.web.repository.MedicamentoRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.GuiaRemisionService;
import com.hospital.web.service.OrdenCompraService;
import com.hospital.web.service.ProveedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/farmacia/compras")
public class ComprasAlmaceneroController {

    private final OrdenCompraService ordenCompraService;
    private final GuiaRemisionService guiaRemisionService;
    private final ProveedorService proveedorService;
    private final MedicamentoRepository medicamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public ComprasAlmaceneroController(
            OrdenCompraService ordenCompraService,
            GuiaRemisionService guiaRemisionService,
            ProveedorService proveedorService,
            MedicamentoRepository medicamentoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.ordenCompraService = ordenCompraService;
        this.guiaRemisionService = guiaRemisionService;
        this.proveedorService = proveedorService;
        this.medicamentoRepository = medicamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ELIMINADO: @GetMapping("/dashboard") - Ahora se usa DashboardController

    @GetMapping("/kardex")
    public String consultarKardex(Model model) {
        model.addAttribute("medicamentos", medicamentoRepository.findAll());
        return "farmacia/compras/kardex";
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

        return "farmacia/compras/ordenes";
    }

    @GetMapping("/ordenes/nueva")
    public String nuevaOrden(Model model) {
        model.addAttribute("proveedores", proveedorService.listarActivos());
        return "farmacia/compras/orden-form";
    }

    @PostMapping("/ordenes/guardar")
    public String guardarOrden(
            @RequestParam Integer idProveedor,
            @RequestParam(required = false) String observacion,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idUsuarioAlmacenero = obtenerIdUsuario(principal, "almacenero");

            var orden = ordenCompraService.generarOrden(
                    idProveedor,
                    idUsuarioAlmacenero,
                    observacion
            );

            redirectAttributes.addFlashAttribute("success", "Orden de compra generada correctamente.");

            return "redirect:/farmacia/compras/ordenes/" + orden.getIdOrdenCompra();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/farmacia/compras/ordenes/nueva";
        }
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
                    model.addAttribute("medicamentos", medicamentoRepository.findAll());
                    model.addAttribute("guias", guiaRemisionService.buscarPorOrden(id));
                    return "farmacia/compras/orden-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "La orden de compra no existe.");
                    return "redirect:/farmacia/compras/ordenes";
                });
    }

    @PostMapping("/ordenes/{id}/detalle")
    public String agregarDetalleOrden(
            @PathVariable Integer id,
            @RequestParam Integer idMedicamento,
            @RequestParam Integer cantidadSolicitada,
            @RequestParam BigDecimal precioReferencial,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ordenCompraService.agregarDetalle(
                    id,
                    idMedicamento,
                    cantidadSolicitada,
                    precioReferencial
            );

            redirectAttributes.addFlashAttribute("success", "Medicamento agregado a la orden.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/compras/ordenes/" + id;
    }

    @PostMapping("/ordenes/{id}/enviar-revision")
    public String enviarRevision(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ordenCompraService.enviarRevision(id);
            redirectAttributes.addFlashAttribute("success", "Orden enviada a revisión del químico farmacéutico.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/compras/ordenes/" + id;
    }

    @GetMapping("/guias")
    public String listarGuias(Model model) {
        model.addAttribute("guias", guiaRemisionService.listarTodas());
        return "farmacia/compras/guias";
    }

    @GetMapping("/guias/nueva")
    public String nuevaGuia(Model model) {
        model.addAttribute("ordenes", ordenCompraService.listarPorEstado("AUTORIZADA"));
        return "farmacia/compras/guia-form";
    }

    @PostMapping("/guias/guardar")
    public String guardarGuia(
            @RequestParam Integer idOrdenCompra,
            @RequestParam String numeroGuia,
            @RequestParam String fechaEmision,
            @RequestParam(required = false) String observacion,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idUsuarioAlmacenero = obtenerIdUsuario(principal, "almacenero");

            var guia = guiaRemisionService.registrarGuia(
                    idOrdenCompra,
                    idUsuarioAlmacenero,
                    numeroGuia,
                    LocalDate.parse(fechaEmision),
                    observacion
            );

            redirectAttributes.addFlashAttribute("success", "Guía de remisión registrada correctamente.");

            return "redirect:/farmacia/compras/guias/" + guia.getIdGuiaRemision();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/farmacia/compras/guias/nueva";
        }
    }

    @GetMapping("/guias/{id}")
    public String detalleGuia(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return guiaRemisionService.buscarPorId(id)
                .map(guia -> {
                    model.addAttribute("guia", guia);
                    model.addAttribute("detalles", guiaRemisionService.listarDetalle(id));
                    model.addAttribute("medicamentos", medicamentoRepository.findAll());
                    return "farmacia/compras/guia-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "La guía de remisión no existe.");
                    return "redirect:/farmacia/compras/guias";
                });
    }

    @PostMapping("/guias/{id}/detalle")
    public String agregarDetalleGuia(
            @PathVariable Integer id,
            @RequestParam Integer idMedicamento,
            @RequestParam Integer cantidadRecibida,
            @RequestParam(required = false) String observacion,
            RedirectAttributes redirectAttributes
    ) {
        try {
            guiaRemisionService.agregarDetalle(
                    id,
                    idMedicamento,
                    cantidadRecibida,
                    observacion
            );

            redirectAttributes.addFlashAttribute("success", "Medicamento recibido agregado a la guía.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/compras/guias/" + id;
    }

    private Integer obtenerIdUsuario(Principal principal, String usernameFallback) {
        String username = principal != null ? principal.getName() : usernameFallback;

        return usuarioRepository.findByUsername(username)
                .orElseGet(() -> usuarioRepository.findByUsername(usernameFallback)
                        .orElseThrow(() -> new IllegalStateException("Usuario no encontrado.")))
                .getIdUsuario();
    }
}