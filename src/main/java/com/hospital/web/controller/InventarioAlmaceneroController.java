package com.hospital.web.controller;

import com.hospital.web.repository.MedicamentoRepository;
import com.hospital.web.repository.UsuarioRepository;
import com.hospital.web.service.AjusteInventarioService;
import com.hospital.web.service.BalanceMensualService;
import com.hospital.web.service.ConteoFisicoService;
import com.hospital.web.service.KardexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.hospital.web.repository.CategoriaMedicamentoRepository;
import com.hospital.web.service.InventarioMedicamentoService;

import java.security.Principal;

@Controller
@RequestMapping("/farmacia/inventario")
public class InventarioAlmaceneroController {

    private final KardexService kardexService;
    private final ConteoFisicoService conteoFisicoService;
    private final AjusteInventarioService ajusteInventarioService;
    private final BalanceMensualService balanceMensualService;
    private final MedicamentoRepository medicamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final InventarioMedicamentoService inventarioMedicamentoService;
    private final CategoriaMedicamentoRepository categoriaMedicamentoRepository;

    public InventarioAlmaceneroController(
            KardexService kardexService,
            ConteoFisicoService conteoFisicoService,
            AjusteInventarioService ajusteInventarioService,
            BalanceMensualService balanceMensualService,
            MedicamentoRepository medicamentoRepository,
            UsuarioRepository usuarioRepository,
            InventarioMedicamentoService inventarioMedicamentoService,
            CategoriaMedicamentoRepository categoriaMedicamentoRepository
    ) {
        this.kardexService = kardexService;
        this.conteoFisicoService = conteoFisicoService;
        this.ajusteInventarioService = ajusteInventarioService;
        this.balanceMensualService = balanceMensualService;
        this.medicamentoRepository = medicamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.inventarioMedicamentoService = inventarioMedicamentoService;
        this.categoriaMedicamentoRepository = categoriaMedicamentoRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "farmacia/inventario/dashboard";
    }

    @GetMapping("/medicamentos")
    public String listarMedicamentos(Model model) {
        model.addAttribute("medicamentos", inventarioMedicamentoService.listarTodos());
        return "farmacia/inventario/medicamentos";
    }

    @GetMapping("/medicamentos/nuevo")
public String nuevoMedicamento(Model model) {
    model.addAttribute("categorias", categoriaMedicamentoRepository.findAll());
    return "farmacia/inventario/medicamento-form";
}

@PostMapping("/medicamentos/guardar")
public String guardarMedicamento(
        @RequestParam String nombreMedicamento,
        @RequestParam String concentracion,
        @RequestParam String presentacion,
        @RequestParam Integer idCategoria,
        @RequestParam Integer stockActual,
        @RequestParam Integer stockMinimo,
        Principal principal,
        RedirectAttributes redirectAttributes
) {
    try {
        Integer idAlmacenero = obtenerIdUsuario(principal, "almacenero");

        var medicamento = inventarioMedicamentoService.registrarMedicamentoConKardex(
                nombreMedicamento,
                concentracion,
                presentacion,
                idCategoria,
                stockActual,
                stockMinimo,
                idAlmacenero
        );

        redirectAttributes.addFlashAttribute(
                "success",
                "Medicamento registrado correctamente y Kardex inicial creado."
        );

        return "redirect:/farmacia/inventario/medicamentos";

    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/farmacia/inventario/medicamentos/nuevo";
    }
}

    @GetMapping("/kardex")
    public String listarKardex(Model model) {
        model.addAttribute("kardexList", kardexService.listarTodos());
        return "farmacia/inventario/kardex";
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
                    return "farmacia/inventario/kardex-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "El Kardex no existe.");
                    return "redirect:/farmacia/inventario/kardex";
                });
    }

    @PostMapping("/kardex/{id}/movimiento")
    public String registrarMovimiento(
            @PathVariable Integer id,
            @RequestParam String tipoMovimiento,
            @RequestParam Integer cantidad,
            @RequestParam(required = false) String motivo,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idUsuario = obtenerIdUsuario(principal, "almacenero");

            kardexService.registrarMovimiento(
                    id,
                    tipoMovimiento,
                    cantidad,
                    motivo,
                    idUsuario
            );

            redirectAttributes.addFlashAttribute("success", "Movimiento de Kardex registrado correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/inventario/kardex/" + id;
    }

    @GetMapping("/conteos")
    public String listarConteos(
            @RequestParam(required = false) String estado,
            Model model
    ) {
        if (estado != null && !estado.isBlank()) {
            model.addAttribute("conteos", conteoFisicoService.listarPorEstado(estado.trim()));
            model.addAttribute("estadoBuscado", estado.trim());
        } else {
            model.addAttribute("conteos", conteoFisicoService.listarTodos());
        }

        return "farmacia/inventario/conteos";
    }

    @GetMapping("/conteos/nuevo")
    public String nuevoConteo() {
        return "farmacia/inventario/conteo-form";
    }

    @PostMapping("/conteos/guardar")
    public String guardarConteo(
            @RequestParam(required = false) String observacion,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idAlmacenero = obtenerIdUsuario(principal, "almacenero");

            var conteo = conteoFisicoService.registrarConteo(
                    idAlmacenero,
                    observacion
            );

            redirectAttributes.addFlashAttribute("success", "Conteo físico registrado correctamente.");

            return "redirect:/farmacia/inventario/conteos/" + conteo.getIdConteoFisico();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/farmacia/inventario/conteos/nuevo";
        }
    }

    @GetMapping("/conteos/{id}")
    public String detalleConteo(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return conteoFisicoService.buscarPorId(id)
                .map(conteo -> {
                    model.addAttribute("conteo", conteo);
                    model.addAttribute("detalles", conteoFisicoService.listarDetalle(id));
                    model.addAttribute("medicamentos", medicamentoRepository.findAll());
                    return "farmacia/inventario/conteo-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "El conteo físico no existe.");
                    return "redirect:/farmacia/inventario/conteos";
                });
    }

    @PostMapping("/conteos/{id}/detalle")
    public String agregarDetalleConteo(
            @PathVariable Integer id,
            @RequestParam Integer idMedicamento,
            @RequestParam Integer stockFisico,
            @RequestParam(required = false) String observacion,
            RedirectAttributes redirectAttributes
    ) {
        try {
            conteoFisicoService.agregarDetalleConteo(
                    id,
                    idMedicamento,
                    stockFisico,
                    observacion
            );

            redirectAttributes.addFlashAttribute("success", "Medicamento agregado al conteo físico.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/inventario/conteos/" + id;
    }

    @PostMapping("/conteos/{id}/enviar-quimico")
    public String enviarConteoQuimico(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            conteoFisicoService.enviarAQuimico(id);
            redirectAttributes.addFlashAttribute("success", "Conteo físico enviado al químico farmacéutico.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/inventario/conteos/" + id;
    }

    @PostMapping("/conteos/detalle/{idDetalle}/ajustar")
    public String registrarAjuste(
            @PathVariable Integer idDetalle,
            @RequestParam String motivo,
            @RequestParam Integer idConteoFisico,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idAlmacenero = obtenerIdUsuario(principal, "almacenero");

            ajusteInventarioService.registrarAjuste(
                    idDetalle,
                    idAlmacenero,
                    motivo
            );

            redirectAttributes.addFlashAttribute("success", "Ajuste de inventario registrado correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/inventario/conteos/" + idConteoFisico;
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

        return "farmacia/inventario/balances";
    }

    @GetMapping("/balances/nuevo")
    public String nuevoBalance() {
        return "farmacia/inventario/balance-form";
    }

    @PostMapping("/balances/guardar")
    public String guardarBalance(
            @RequestParam String periodo,
            @RequestParam(required = false) String observacion,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer idAlmacenero = obtenerIdUsuario(principal, "almacenero");

            var balance = balanceMensualService.generarBalance(
                    periodo,
                    idAlmacenero,
                    observacion
            );

            redirectAttributes.addFlashAttribute("success", "Balance mensual generado correctamente.");

            return "redirect:/farmacia/inventario/balances/" + balance.getIdBalanceMensual();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/farmacia/inventario/balances/nuevo";
        }
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
                    return "farmacia/inventario/balance-detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "El balance mensual no existe.");
                    return "redirect:/farmacia/inventario/balances";
                });
    }

    @PostMapping("/balances/{id}/enviar-quimico")
    public String enviarBalanceQuimico(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            balanceMensualService.enviarAQuimico(id);
            redirectAttributes.addFlashAttribute("success", "Balance mensual enviado al químico farmacéutico.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/inventario/balances/" + id;
    }

    @PostMapping("/observaciones/{id}/subsanar")
    public String subsanarObservacion(
            @PathVariable Integer id,
            @RequestParam Integer idBalanceMensual,
            RedirectAttributes redirectAttributes
    ) {
        try {
            balanceMensualService.subsanarObservacion(id);
            redirectAttributes.addFlashAttribute("success", "Observación marcada como subsanada.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/farmacia/inventario/balances/" + idBalanceMensual;
    }

    private Integer obtenerIdUsuario(Principal principal, String usernameFallback) {
        String username = principal != null ? principal.getName() : usernameFallback;

        return usuarioRepository.findByUsername(username)
                .orElseGet(() -> usuarioRepository.findByUsername(usernameFallback)
                        .orElseThrow(() -> new IllegalStateException("Usuario no encontrado.")))
                .getIdUsuario();
    }
}