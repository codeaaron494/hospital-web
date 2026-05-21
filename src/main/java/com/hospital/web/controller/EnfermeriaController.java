package com.hospital.web.controller;

import com.hospital.web.service.FichaAdmisionService;
import com.hospital.web.service.TriajeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/enfermeria")
public class EnfermeriaController {

    private final FichaAdmisionService fichaAdmisionService;
    private final TriajeService triajeService;

    public EnfermeriaController(
            FichaAdmisionService fichaAdmisionService,
            TriajeService triajeService
    ) {
        this.fichaAdmisionService = fichaAdmisionService;
        this.triajeService = triajeService;
    }

    @GetMapping("/fichas")
    public String listarFichas(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String estado,
            Model model
    ) {
        if (dni != null && !dni.isBlank()) {
            model.addAttribute("fichas", fichaAdmisionService.buscarPorDniPaciente(dni.trim()));
            model.addAttribute("dniBuscado", dni.trim());
        } else if (estado != null && !estado.isBlank()) {
            model.addAttribute("fichas", fichaAdmisionService.buscarPorEstado(estado.trim()));
            model.addAttribute("estadoBuscado", estado.trim());
        } else {
            model.addAttribute("fichas", fichaAdmisionService.listarTodas());
        }

        return "enfermeria/fichas";
    }

    @GetMapping("/triaje/{idFicha}")
    public String formularioTriaje(
            @PathVariable Integer idFicha,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return fichaAdmisionService.buscarPorId(idFicha)
                .map(ficha -> {
                    model.addAttribute("ficha", ficha);
                    model.addAttribute(
                            "triajeExistente",
                            triajeService.buscarPorFicha(idFicha).orElse(null)
                    );
                    return "enfermeria/triaje";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute(
                            "error",
                            "La ficha de admisión no existe."
                    );
                    return "redirect:/enfermeria/fichas";
                });
    }

    @PostMapping("/triaje/{idFicha}/guardar")
    public String guardarTriaje(
            @PathVariable Integer idFicha,
            @RequestParam BigDecimal peso,
            @RequestParam BigDecimal talla,
            @RequestParam BigDecimal temperatura,
            @RequestParam String presionArterial,
            @RequestParam Integer frecuenciaCardiaca,
            RedirectAttributes redirectAttributes
    ) {
        try {
            triajeService.registrarTriaje(
                    idFicha,
                    peso,
                    talla,
                    temperatura,
                    presionArterial,
                    frecuenciaCardiaca
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Triaje registrado correctamente. Historia clínica actualizada."
            );

            return "redirect:/enfermeria/fichas";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enfermeria/triaje/" + idFicha;
        }
    }
}