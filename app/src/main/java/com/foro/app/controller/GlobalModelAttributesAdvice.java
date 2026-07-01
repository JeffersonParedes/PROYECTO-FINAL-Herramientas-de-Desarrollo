package com.foro.app.controller;

import com.foro.app.dto.Response.UsuarioResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributesAdvice {

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        UsuarioResponse usuario = (UsuarioResponse) session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("usuarioLogueado", true);
            model.addAttribute("usuario", usuario);
            model.addAttribute("autorId", usuario.getId());
            model.addAttribute("usuarioId", usuario.getId());
        } else {
            model.addAttribute("usuarioLogueado", false);
            model.addAttribute("usuario", null);
            model.addAttribute("autorId", null);
            model.addAttribute("usuarioId", null);
        }
    }
}
