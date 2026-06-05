package com.foro.app.controller;

import com.foro.app.service.ReaccionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reaccion")
public class ReaccionController {

    private final ReaccionService reaccionService;

    public ReaccionController(ReaccionService reaccionService) {
        this.reaccionService = reaccionService;
    }

    @PostMapping("/procesar")
    public String procesarReaccion(@RequestParam Long usuarioId,
                                   @RequestParam Long publicacionId,
                                   @RequestParam String tipo,
                                   RedirectAttributes redirectAttributes) {
        try {
            reaccionService.procesarReaccion(usuarioId, publicacionId, tipo);
            redirectAttributes.addFlashAttribute("mensaje", "Reacción registrada");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/publicacion?id=" + publicacionId;
    }
}
