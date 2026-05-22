package com.hospital.web.controller;

import com.hospital.web.service.CitaService;
import com.hospital.web.service.FichaAdmisionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fichas")
public class FichaAdmisionController {

    private final FichaAdmisionService fichaAdmisionService;
    private final CitaService citaService;

    public FichaAdmisionController(
            FichaAdmisionService fichaAdmisionService,
            CitaService citaService
    ) {
        this.fichaAdmisionService = fichaAdmisionService;
        this.citaService = citaService;
    }

    @GetMapping
public String listarFichas(
        @RequestParam(required = false) String dni,
        Model model
) {
    if (dni != null && !dni.isBlank()) {
        model.addAttribute("fichas", fichaAdmisionService.buscarPorDniPaciente(dni.trim()));
        model.addAttribute("dniBuscado", dni.trim());
    } else {
        model.addAttribute("fichas", fichaAdmisionService.listarTodas());
    }

    return "fichas/index";
}

    @GetMapping("/nueva")
    public String nuevaFicha(Model model) {
        model.addAttribute("citas", citaService.listarTodas());
        return "fichas/form";
    }

    @PostMapping("/guardar")
    public String guardarFicha(
            @RequestParam Integer idCita,
            @RequestParam String tipoAdmision,
            @RequestParam String prioridad,
            RedirectAttributes redirectAttributes
    ) {
        try {
            var ficha = fichaAdmisionService.registrarFicha(
                    idCita,
                    tipoAdmision,
                    prioridad
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Ficha de admisión registrada correctamente."
            );

            return "redirect:/fichas/" + ficha.getIdFichaAdmision();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/fichas/nueva";
        }
    }

    @GetMapping("/{id}")
    public String detalleFicha(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return fichaAdmisionService.buscarPorId(id)
                .map(ficha -> {
                    model.addAttribute("ficha", ficha);
                    return "fichas/detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute(
                            "error",
                            "La ficha de admisión no existe."
                    );
                    return "redirect:/fichas/nueva";
                });
    }
}