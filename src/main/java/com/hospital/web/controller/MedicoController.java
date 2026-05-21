package com.hospital.web.controller;

import com.hospital.web.repository.MedicoRepository;
import com.hospital.web.service.AtencionMedicaService;
import com.hospital.web.service.HistoriaClinicaService;
import com.hospital.web.service.MedicamentoService;
import com.hospital.web.service.RecetaMedicaService;
import com.hospital.web.service.TriajeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medico")
public class MedicoController {

    private final HistoriaClinicaService historiaClinicaService;
    private final AtencionMedicaService atencionMedicaService;
    private final TriajeService triajeService;
    private final RecetaMedicaService recetaMedicaService;
    private final MedicamentoService medicamentoService;
    private final MedicoRepository medicoRepository;

    public MedicoController(
            HistoriaClinicaService historiaClinicaService,
            AtencionMedicaService atencionMedicaService,
            TriajeService triajeService,
            RecetaMedicaService recetaMedicaService,
            MedicamentoService medicamentoService,
            MedicoRepository medicoRepository
    ) {
        this.historiaClinicaService = historiaClinicaService;
        this.atencionMedicaService = atencionMedicaService;
        this.triajeService = triajeService;
        this.recetaMedicaService = recetaMedicaService;
        this.medicamentoService = medicamentoService;
        this.medicoRepository = medicoRepository;
    }

    @GetMapping("/historias")
    public String listarHistorias(
            @RequestParam(required = false) String dni,
            Model model
    ) {
        if (dni != null && !dni.isBlank()) {
            var historia = historiaClinicaService.buscarPorDniPaciente(dni.trim());

            model.addAttribute(
                    "historias",
                    historia.map(java.util.List::of).orElse(java.util.List.of())
            );
            model.addAttribute("dniBuscado", dni.trim());
        } else {
            model.addAttribute("historias", historiaClinicaService.listarTodas());
        }

        return "medico/historias";
    }

    @GetMapping("/historias/{id}")
    public String detalleHistoria(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return historiaClinicaService.buscarPorId(id)
                .map(historia -> {
                    model.addAttribute("historia", historia);
                    model.addAttribute("triajes", triajeService.buscarPorHistoria(id));
                    model.addAttribute("atenciones", atencionMedicaService.buscarPorHistoria(id));
                    return "medico/historia-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute(
                            "error",
                            "La historia clínica no existe."
                    );
                    return "redirect:/medico/historias";
                });
    }

    @GetMapping("/atencion/nueva/{idHistoria}")
    public String nuevaAtencion(
            @PathVariable Integer idHistoria,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return historiaClinicaService.buscarPorId(idHistoria)
                .map(historia -> {
                    model.addAttribute("historia", historia);
                    model.addAttribute("medicos", medicoRepository.findAll());
                    return "medico/atencion";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute(
                            "error",
                            "La historia clínica no existe."
                    );
                    return "redirect:/medico/historias";
                });
    }

    @PostMapping("/atencion/guardar")
    public String guardarAtencion(
            @RequestParam Integer idHistoriaClinica,
            @RequestParam Integer idMedico,
            @RequestParam String diagnostico,
            @RequestParam(required = false) String tratamiento,
            @RequestParam(required = false) String recomendaciones,
            RedirectAttributes redirectAttributes
    ) {
        try {
            var atencion = atencionMedicaService.registrarAtencion(
                    idHistoriaClinica,
                    idMedico,
                    diagnostico,
                    tratamiento,
                    recomendaciones
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Atención médica registrada correctamente. Historia clínica actualizada."
            );

            return "redirect:/medico/atencion/" + atencion.getIdAtencion();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/medico/historias";
        }
    }

    @GetMapping("/atencion/{idAtencion}")
    public String detalleAtencion(
            @PathVariable Integer idAtencion,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return atencionMedicaService.buscarPorId(idAtencion)
                .map(atencion -> {
                    model.addAttribute("atencion", atencion);
                    model.addAttribute(
                            "receta",
                            recetaMedicaService.buscarPorAtencion(idAtencion).orElse(null)
                    );
                    return "medico/atencion-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute(
                            "error",
                            "La atención médica no existe."
                    );
                    return "redirect:/medico/historias";
                });
    }

    @PostMapping("/receta/generar")
    public String generarReceta(
            @RequestParam Integer idAtencion,
            RedirectAttributes redirectAttributes
    ) {
        try {
            var receta = recetaMedicaService.generarReceta(idAtencion);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Receta médica generada correctamente."
            );

            return "redirect:/medico/receta/" + receta.getIdReceta();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/medico/atencion/" + idAtencion;
        }
    }

    @GetMapping("/receta/{idReceta}")
    public String detalleReceta(
            @PathVariable Integer idReceta,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return recetaMedicaService.buscarPorId(idReceta)
                .map(receta -> {
                    model.addAttribute("receta", receta);
                    model.addAttribute("detalles", recetaMedicaService.listarDetalle(idReceta));
                    model.addAttribute("medicamentos", medicamentoService.listarTodos());
                    return "medico/receta";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute(
                            "error",
                            "La receta médica no existe."
                    );
                    return "redirect:/medico/historias";
                });
    }

    @PostMapping("/receta/{idReceta}/detalle")
    public String agregarDetalleReceta(
            @PathVariable Integer idReceta,
            @RequestParam Integer idMedicamento,
            @RequestParam String dosis,
            @RequestParam String frecuencia,
            @RequestParam String duracion,
            @RequestParam Integer cantidadIndicada,
            RedirectAttributes redirectAttributes
    ) {
        try {
            recetaMedicaService.agregarDetalle(
                    idReceta,
                    idMedicamento,
                    dosis,
                    frecuencia,
                    duracion,
                    cantidadIndicada
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Medicamento agregado correctamente a la receta."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/medico/receta/" + idReceta;
    }
}