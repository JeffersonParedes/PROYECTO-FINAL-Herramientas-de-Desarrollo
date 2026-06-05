package com.foro.app.controller;

import com.foro.app.dto.SubforoDTO;
import com.foro.app.dto.SubforoJerarquiaDTO;
import com.foro.app.service.SubforoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/subforo")
public class SubforoController {

    private final SubforoService subforoService;

    public SubforoController(SubforoService subforoService) {
        this.subforoService = subforoService;
    }

    @GetMapping("/{id}")
    public String obtenerDetalleSubforo(@PathVariable Long id, Model model) {
        Map<String, Object> detalle = subforoService.obtenerDetalleSubforo(id);
        model.addAttribute("subforo", detalle.get("subforo"));
        model.addAttribute("breadcrumbs", detalle.get("breadcrumbs"));
        model.addAttribute("pageTitle", ((SubforoDTO) detalle.get("subforo")).getNombre());
        model.addAttribute("currentPage", "subforo");
        return "subforo";
    }

    @GetMapping("/jerarquia")
    public String obtenerJerarquiaCompleta(Model model) {
        List<SubforoJerarquiaDTO> arbol = subforoService.obtenerJerarquiaCompleta();
        model.addAttribute("subforosPrincipales", arbol);
        model.addAttribute("pageTitle", "Foros");
        model.addAttribute("currentPage", "jerarquia");
        return "index";
    }

    @PostMapping("/crear")
    public String crearSubforo(@RequestParam String nombre,
                               @RequestParam(required = false) String descripcion,
                               @RequestParam(required = false) Long parentId,
                               @RequestParam Long ejecutorId,
                               RedirectAttributes redirectAttributes) {
        try {
            SubforoDTO dto = subforoService.crearSubforo(nombre, descripcion, parentId, ejecutorId);
            redirectAttributes.addFlashAttribute("mensaje", "Foro '" + dto.getNombre() + "' creado");
            return "redirect:/subforo/" + dto.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }
    }
}
