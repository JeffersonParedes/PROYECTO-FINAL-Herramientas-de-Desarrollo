package com.foro.app.controller;

import com.foro.app.dto.Request.SubforoCreateRequest;
import com.foro.app.dto.Response.SubforoJerarquiaResponse;
import com.foro.app.dto.Response.SubforoResponse;
import com.foro.app.dto.Response.UsuarioResponse;
import com.foro.app.exceptions.UnauthorizedException;
import com.foro.app.service.PublicacionService;
import com.foro.app.service.SubforoService;
import jakarta.servlet.http.HttpSession;
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
    private final PublicacionService publicacionService;

    public SubforoController(SubforoService subforoService, PublicacionService publicacionService) {
        this.subforoService = subforoService;
        this.publicacionService = publicacionService;
    }

    @GetMapping("/{id}")
    public String obtenerDetalleSubforo(@PathVariable Long id, Model model) {
        Map<String, Object> detalle = subforoService.obtenerDetalleSubforo(id);
        model.addAttribute("subforo", detalle.get("subforo"));
        model.addAttribute("breadcrumbs", detalle.get("breadcrumbs"));
        model.addAttribute("publicaciones", publicacionService.obtenerPublicacionesPorSubforo(id));
        model.addAttribute("subforos", subforoService.obtenerTodosSubforos());
        model.addAttribute("pageTitle", ((SubforoResponse) detalle.get("subforo")).getNombre());
        model.addAttribute("currentPage", "subforo");
        return "subforo";
    }

    @GetMapping("/jerarquia")
    public String obtenerJerarquiaCompleta(Model model) {
        List<SubforoJerarquiaResponse> arbol = subforoService.obtenerJerarquiaCompleta();
        model.addAttribute("subforosPrincipales", arbol);
        model.addAttribute("pageTitle", "Foros");
        model.addAttribute("currentPage", "jerarquia");
        return "index";
    }

    @PostMapping("/crear")
    public String crearSubforo(SubforoCreateRequest request,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null || !"admin".equalsIgnoreCase(loggedUser.getRol())) {
            throw new UnauthorizedException("Acceso denegado. Se requiere cuenta de administrador.");
        }

        SubforoResponse response = subforoService.crearSubforo(request, loggedUser.getId());
        redirectAttributes.addFlashAttribute("mensaje", "Subforo '" + response.getNombre() + "' creado con éxito.");
        return "redirect:/subforo/" + response.getId();
    }

    @PostMapping("/eliminar")
    public String eliminarSubforo(@RequestParam Long subforoId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null || !"admin".equalsIgnoreCase(loggedUser.getRol())) {
            throw new UnauthorizedException("Acceso denegado. Se requiere cuenta de administrador.");
        }

        subforoService.eliminarSubforo(loggedUser.getId(), subforoId);
        redirectAttributes.addFlashAttribute("mensaje", "Subforo y sus contenidos eliminados exitosamente.");
        return "redirect:/admin";
    }
}