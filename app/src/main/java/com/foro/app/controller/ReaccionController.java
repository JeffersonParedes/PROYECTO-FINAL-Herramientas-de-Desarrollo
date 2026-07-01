package com.foro.app.controller;

import com.foro.app.dto.Request.ReaccionRequest;
import com.foro.app.dto.Response.UsuarioResponse;
import com.foro.app.exceptions.UnauthorizedException;
import com.foro.app.service.ReaccionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reaccion")
public class ReaccionController {

    private final ReaccionService reaccionService;

    public ReaccionController(ReaccionService reaccionService) {
        this.reaccionService = reaccionService;
    }

    @PostMapping("/procesar")
    public String procesarReaccion(ReaccionRequest request,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null) {
            throw new UnauthorizedException("Debes iniciar sesión para reaccionar.");
        }

        reaccionService.procesarReaccion(loggedUser.getId(), request);
        redirectAttributes.addFlashAttribute("mensaje", "Reacción procesada.");
        return "redirect:/publicacion/" + request.getPublicacionId();
    }
}
