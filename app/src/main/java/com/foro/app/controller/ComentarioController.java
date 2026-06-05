package com.foro.app.controller;

import com.foro.app.dto.ComentarioDTO;
import com.foro.app.service.ComentarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/comentario")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PostMapping("/agregar")
    public String agregarComentario(@RequestParam Long autorId,
                                    @RequestParam Long publicacionId,
                                    @RequestParam String texto,
                                    RedirectAttributes redirectAttributes) {
        try {
            ComentarioDTO dto = comentarioService.agregarComentario(autorId, publicacionId, texto);
            redirectAttributes.addFlashAttribute("mensaje", "Comentario agregado");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/publicacion?id=" + publicacionId;
    }

    @GetMapping("/publicacion/{publicacionId}")
    public String obtenerComentariosPorPublicacion(@PathVariable Long publicacionId, Model model) {
        List<ComentarioDTO> comentarios = comentarioService.obtenerComentariosPorPublicacion(publicacionId);
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("pageTitle", "Comentarios");
        model.addAttribute("currentPage", "comentarios");
        return "publicacion";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarComentario(@PathVariable Long id,
                                     @RequestParam Long usuarioId,
                                     @RequestParam Long publicacionId,
                                     RedirectAttributes redirectAttributes) {
        try {
            comentarioService.eliminarComentario(usuarioId, id);
            redirectAttributes.addFlashAttribute("mensaje", "Comentario eliminado");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/publicacion?id=" + publicacionId;
    }
}
