package com.hospital.web.controller;

import com.hospital.web.repository.KardexRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.SuministroMedicamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/farmacia/entregas")
public class FarmaciaEntregasController {

    private final SuministroMedicamentoService suministroMedicamentoService;
    private final KardexRepository kardexRepository;
    private final UsuarioRepository usuarioRepository;

    public FarmaciaEntregasController(
            SuministroMedicamentoService suministroMedicamentoService,
            KardexRepository kardexRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.suministroMedicamentoService = suministroMedicamentoService;
        this.kardexRepository = kardexRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "farmacia/entregas/dashboard";
    }

    @GetMapping("/recetas")
    public String listarRecetas(
            @RequestParam(required = false) String estado,
            Model model
    ) {
        if (estado != null && !estado.isBlank()) {
            model.addAttribute("recetas", suministroMedicamentoService.listarRecetasPorEstado(estado.trim()));
            model.addAttribute("estadoBuscado", estado.trim());
        } else {
            model.addAttribute("recetas", suministroMedicamentoService.listarRecetas());
        }

        return "farmacia/entregas/recetas";
    }

    @GetMapping("/recetas/{id}")
    public String detalleReceta(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return suministroMedicamentoService.buscarRecetaPorId(id)
                .map(receta -> {
                    model.addAttribute("receta", receta);
                    model.addAttribute("detalles", suministroMedicamentoService.listarDetalleReceta(id));
                    model.addAttribute("despacho", suministroMedicamentoService.buscarDespachoPorReceta(id).orElse(null));
                    model.addAttribute("yaDespachada", suministroMedicamentoService.recetaYaDespachada(id));
                    model.addAttribute("stockSuficiente", suministroMedicamentoService.recetaTieneStockSuficiente(id));
                    model.addAttribute("kardexList", kardexRepository.findAll());
                    return "farmacia/entregas/receta-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "La receta médica no existe.");
                    return "redirect:/farmacia/entregas/recetas";
                });
    }

    @PostMapping("/recetas/{id}/despachar")
    public String despacharReceta(
            @PathVariable Integer id,
            @RequestParam(required = false) String observacion,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idTecnico = obtenerIdUsuario(principal, "tecnico");

            var despacho = suministroMedicamentoService.registrarDespachoAtendido(
                    id,
                    idTecnico,
                    observacion
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Despacho registrado correctamente. Stock actualizado en Kardex."
            );

            return "redirect:/farmacia/entregas/despachos/" + despacho.getIdDespachoMedicamento();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/farmacia/entregas/recetas/" + id;
        }
    }

    @PostMapping("/recetas/{id}/rechazar")
    public String rechazarReceta(
            @PathVariable Integer id,
            @RequestParam String estadoDespacho,
            @RequestParam String observacion,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idTecnico = obtenerIdUsuario(principal, "tecnico");

            var despacho = suministroMedicamentoService.registrarDespachoRechazado(
                    id,
                    idTecnico,
                    estadoDespacho,
                    observacion
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Despacho registrado como " + estadoDespacho + "."
            );

            return "redirect:/farmacia/entregas/despachos/" + despacho.getIdDespachoMedicamento();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/farmacia/entregas/recetas/" + id;
        }
    }

    @GetMapping("/despachos")
    public String listarDespachos(
            @RequestParam(required = false) String estado,
            Model model
    ) {
        if (estado != null && !estado.isBlank()) {
            model.addAttribute("despachos", suministroMedicamentoService.listarDespachosPorEstado(estado.trim()));
            model.addAttribute("estadoBuscado", estado.trim());
        } else {
            model.addAttribute("despachos", suministroMedicamentoService.listarDespachos());
        }

        return "farmacia/entregas/despachos";
    }

    @GetMapping("/despachos/{id}")
    public String detalleDespacho(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return suministroMedicamentoService.buscarDespachoPorId(id)
                .map(despacho -> {
                    model.addAttribute("despacho", despacho);
                    model.addAttribute("detalles", suministroMedicamentoService.listarDetalleDespacho(id));
                    return "farmacia/entregas/despacho-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "El despacho no existe.");
                    return "redirect:/farmacia/entregas/despachos";
                });
    }

    private Integer obtenerIdUsuario(Principal principal, String usernameFallback) {
        String username = principal != null ? principal.getName() : usernameFallback;

        return usuarioRepository.findByUsername(username)
                .orElseGet(() -> usuarioRepository.findByUsername(usernameFallback)
                        .orElseThrow(() -> new IllegalStateException("Usuario no encontrado.")))
                .getIdUsuario();
    }
}