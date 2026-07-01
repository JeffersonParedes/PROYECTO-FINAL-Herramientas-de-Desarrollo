package com.foro.app.controller;

import com.foro.app.dto.Request.ReporteRequest;
import com.foro.app.dto.Response.ReporteResponse;
import com.foro.app.dto.Response.UsuarioResponse;
import com.foro.app.exceptions.UnauthorizedException;
import com.foro.app.service.ReporteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @PostMapping("/reporte")
    public String crearReporte(ReporteRequest request,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null) {
            throw new UnauthorizedException("Debes iniciar sesión para reportar contenido.");
        }

        reporteService.crearReporte(loggedUser.getId(), request);
        redirectAttributes.addFlashAttribute("mensaje", "Reporte enviado. Los administradores lo revisarán.");
        return "redirect:/publicacion/" + request.getContenidoId();
    }

    @GetMapping("/admin/reportes")
    public String listarReportes(Model model, HttpSession session) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null || !"admin".equalsIgnoreCase(loggedUser.getRol())) {
            throw new UnauthorizedException("Acceso denegado. Se requiere cuenta de administrador.");
        }

        List<ReporteResponse> reportes = reporteService.obtenerReportes(loggedUser.getId());
        model.addAttribute("listaReportes", reportes);
        model.addAttribute("pageTitle", "Reportes Pendientes");
        return "admin";
    }

    @PostMapping("/admin/reportes/eliminar")
    public String eliminarReporte(@RequestParam Long reporteId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null || !"admin".equalsIgnoreCase(loggedUser.getRol())) {
            throw new UnauthorizedException("Acceso denegado. Se requiere cuenta de administrador.");
        }

        reporteService.eliminarReporte(loggedUser.getId(), reporteId);
        redirectAttributes.addFlashAttribute("mensaje", "Reporte eliminado exitosamente.");
        return "redirect:/admin";
    }
}