package com.foro.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping({"/", "/index"})
    public String home(Model model) {
        model.addAttribute("publicaciones", List.of());
        model.addAttribute("subforos", List.of());
        return "index";
    }

    @GetMapping("/subforo")
    public String subforo(Model model) {
        model.addAttribute("usuarioLogueado", false);
        model.addAttribute("subforoAdulto", true);
        return "subforo";
    }

    @GetMapping("/subforo")
    public String subforo() {
        return "subforo"; // carga subforo.html
    }
}