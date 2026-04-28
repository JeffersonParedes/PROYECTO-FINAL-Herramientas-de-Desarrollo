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

// ── VISTAS DE TU EQUIPO (Thom_ y Leonardo) ──

    @GetMapping("/login")
    public String login(Model model) {
        // Le pasamos el título para la pestaña del navegador
        model.addAttribute("pageTitle", "Iniciar Sesión");
        
        // Retorna el nombre exacto del archivo HTML (sin el .html)
        return "login"; 
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("pageTitle", "Crear Cuenta");
        
        // Más adelante, cuando conecten MongoDB, aquí enviarán un objeto vacío:
        // model.addAttribute("usuario", new Usuario());
        
        return "registro";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("pageTitle", "Panel de Administración");
        
        // Pasamos listas vacías por ahora para que no falle el th:each de la tabla
        model.addAttribute("listaReportes", List.of());
        model.addAttribute("subforosPrincipales", List.of());
        
        return "admin";
    }
   
}  