package com.foro.app.controller;

import com.foro.app.dto.ComentarioDTO;
import com.foro.app.dto.PublicacionDTO;
import com.foro.app.service.ComentarioService;
import com.foro.app.service.PublicacionService;
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

    public PublicacionController(PublicacionService publicacionService,
                                 ComentarioService comentarioService) {
        this.publicacionService = publicacionService;
        this.comentarioService = comentarioService;
    }

    @GetMapping("/{id}")
    public String obtenerDetallePublicacion(@PathVariable Long id, Model model) {
        PublicacionDTO publicacion = publicacionService.obtenerDetallePublicacion(id);
        List<ComentarioDTO> comentarios = comentarioService.obtenerComentariosPorPublicacion(id);

        model.addAttribute("publicacion", publicacion);
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("pageTitle", publicacion.getTitulo());
        model.addAttribute("currentPage", "publicacion");
        return "publicacion";
    }

    @GetMapping("/subforo/{subforoId}")
    public String obtenerPublicacionesPorSubforo(@PathVariable Long subforoId, Model model) {
        List<PublicacionDTO> publicaciones = publicacionService.obtenerPublicacionesPorSubforo(subforoId);
        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("pageTitle", "Publicaciones");
        model.addAttribute("currentPage", "subforo");
        return "index";
    }

    @PostMapping("/crear")
    public String crearPublicacion(@RequestParam Long autorId,
                                   @RequestParam Long subforoId,
                                   @RequestParam String titulo,
                                   @RequestParam String contenido,
                                   @RequestParam(required = false) String descripcion,
                                   @RequestParam(required = false) String imagen,
                                   @RequestParam(required = false) String video,
                                   @RequestParam(required = false) String audio,
                                   RedirectAttributes redirectAttributes) {
        try {
            PublicacionDTO dto = publicacionService.crearPublicacion(
                    autorId, subforoId, titulo, contenido, descripcion, imagen, video, audio);
            redirectAttributes.addFlashAttribute("mensaje", "Publicación creada");
            return "redirect:/publicacion/" + dto.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/crear";
        }
    }
}
