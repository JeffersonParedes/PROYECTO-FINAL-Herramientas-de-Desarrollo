package com.foro.app.controller;

import com.foro.app.dto.Request.ComentarioCreateRequest;
import com.foro.app.dto.Response.ComentarioResponse;
import com.foro.app.dto.Response.UsuarioResponse;
import com.foro.app.exceptions.UnauthorizedException;
import com.foro.app.service.ComentarioService;
import jakarta.servlet.http.HttpSession;
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
    public String agregarComentario(ComentarioCreateRequest request,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null) {
            throw new UnauthorizedException("Debes iniciar sesión para comentar.");
        }

        ComentarioResponse response = comentarioService.agregarComentario(loggedUser.getId(), request);
        redirectAttributes.addFlashAttribute("mensaje", "Comentario agregado exitosamente.");
        return "redirect:/publicacion/" + request.getPublicacionId();
    }

    @GetMapping("/publicacion/{publicacionId}")
    public String obtenerComentariosPorPublicacion(@PathVariable Long publicacionId, Model model) {
        List<ComentarioResponse> comentarios = comentarioService.obtenerComentariosPorPublicacion(publicacionId);
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("pageTitle", "Comentarios");
        model.addAttribute("currentPage", "comentarios");
        return "publicacion";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarComentario(@PathVariable Long id,
            @RequestParam Long publicacionId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null) {
            throw new UnauthorizedException("Debes iniciar sesión para eliminar comentarios.");
        }

        comentarioService.eliminarComentario(loggedUser.getId(), id);
        redirectAttributes.addFlashAttribute("mensaje", "Comentario eliminado.");
        return "redirect:/publicacion/" + publicacionId;
    }
}
