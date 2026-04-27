package com.foro.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        // Por ahora lista vacía, luego conectas la base de datos
        model.addAttribute("publicaciones", List.of());
        model.addAttribute("subforos", List.of());
        return "index";   // ← busca templates/index.html automáticamente
    }
}