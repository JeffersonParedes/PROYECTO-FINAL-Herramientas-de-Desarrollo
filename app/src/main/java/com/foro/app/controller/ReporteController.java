package com.foro.app.controller;

import com.foro.app.service.ReporteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @PostMapping("/reporte")
    public String crearReporte(
            @RequestParam Long usuarioId,
            @RequestParam String tipoContenido,
            @RequestParam Long contenidoId,
            @RequestParam String motivo) {

        reporteService.crearReporte(
                usuarioId,
                tipoContenido,
                contenidoId,
                motivo
        );

        return "redirect:/index";
    }

    @GetMapping("/admin/reportes")
    public String listarReportes(Model model) {

        Long adminId = 1L;

        model.addAttribute(
                "listaReportes",
                reporteService.obtenerReportes(adminId)
        );

        return "admin";
    }

    @PostMapping("/admin/reportes/eliminar")
    public String eliminarReporte(
            @RequestParam Long reporteId) {

        Long adminId = 1L;

        reporteService.eliminarReporte(
                adminId,
                reporteId
        );

        return "redirect:/admin/reportes";
    }
}