package com.foro.app.controller;

import com.foro.app.dto.Request.PublicacionCreateRequest;
import com.foro.app.dto.Response.ComentarioResponse;
import com.foro.app.dto.Response.PublicacionResponse;
import com.foro.app.dto.Response.UsuarioResponse;
import com.foro.app.exceptions.UnauthorizedException;
import com.foro.app.service.ComentarioService;
import com.foro.app.service.PublicacionService;
import com.foro.app.service.SubforoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/publicacion")
public class PublicacionController {

    private final PublicacionService publicacionService;
    private final ComentarioService comentarioService;
    private final SubforoService subforoService;

    public PublicacionController(PublicacionService publicacionService,
            ComentarioService comentarioService,
            SubforoService subforoService) {
        this.publicacionService = publicacionService;
        this.comentarioService = comentarioService;
        this.subforoService = subforoService;
    }

    @GetMapping("/{id}")
    public String obtenerDetallePublicacion(@PathVariable Long id, Model model) {
        PublicacionResponse publicacion = publicacionService.obtenerDetallePublicacion(id);
        List<ComentarioResponse> comentarios = comentarioService.obtenerComentariosPorPublicacion(id);

        model.addAttribute("publicacion", publicacion);
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("subforos", subforoService.obtenerTodosSubforos());
        model.addAttribute("pageTitle", publicacion.getTitulo());
        model.addAttribute("currentPage", "publicacion");
        return "publicacion";
    }

    @GetMapping("/subforo/{subforoId}")
    public String obtenerPublicacionesPorSubforo(@PathVariable Long subforoId, Model model) {
        List<PublicacionResponse> publicaciones = publicacionService.obtenerPublicacionesPorSubforo(subforoId);
        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("pageTitle", "Publicaciones");
        model.addAttribute("currentPage", "subforo");
        return "index";
    }

    @PostMapping("/crear")
    public String crearPublicacion(PublicacionCreateRequest request,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null) {
            throw new UnauthorizedException("Debes iniciar sesión para crear una publicación.");
        }

        PublicacionResponse response = publicacionService.crearPublicacion(loggedUser.getId(), request);
        redirectAttributes.addFlashAttribute("mensaje", "Publicación creada con éxito.");
        return "redirect:/publicacion/" + response.getId();
    }
}