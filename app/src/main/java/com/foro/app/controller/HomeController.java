package com.foro.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Inicio");
        model.addAttribute("currentPage", "home");
        model.addAttribute("publicaciones", List.of());
        model.addAttribute("subforos", List.of());
        return "index";
    }

    @GetMapping("/subforo")
    public String subforo(Model model) {
        model.addAttribute("pageTitle", "Videojuegos");
        model.addAttribute("currentPage", "subforo");
        model.addAttribute("currentSubforoId", null);
        model.addAttribute("usuarioLogueado", false);
        model.addAttribute("subforoAdulto", true);
        model.addAttribute("subforos", List.of());
        return "subforo";
    }
}