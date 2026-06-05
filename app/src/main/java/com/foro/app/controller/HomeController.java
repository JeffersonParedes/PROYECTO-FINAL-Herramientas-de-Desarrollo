package com.foro.app.controller;

import com.foro.app.service.SubforoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class HomeController {

    private final SubforoService subforoService;

    public HomeController(SubforoService subforoService) {
        this.subforoService = subforoService;
    }

   @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Inicio");
        model.addAttribute("publicaciones", List.of());
        model.addAttribute("subforos", List.of());
        model.addAttribute("currentPage", "index");
        return "index";
    }

@GetMapping("/")
    public String rootRedirect() {
        return "redirect:/index";
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

    @GetMapping("/tendencias")
    public String tendencias(Model model) {
        model.addAttribute("pageTitle", "Tendencias");
        model.addAttribute("currentPage", "tendencias");
        model.addAttribute("publicaciones", List.of());
        model.addAttribute("subforos", List.of());
        return "index";
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

    @GetMapping("/crear")
    public String crear(Model model) {
        model.addAttribute("pageTitle", "Crear Publicación");
        model.addAttribute("subforos", subforoService.obtenerTodosSubforos());
        model.addAttribute("autorId", 1L);
        return "crear";
    }

    @GetMapping("/publicacion")
    public String publicacion(Model model) {
        model.addAttribute("pageTitle", "Publicación");
        return "publicacion";
    }

    @GetMapping("/perfil")
    public String perfil(Model model) {
        model.addAttribute("pageTitle", "Perfil");
        return "perfil";
    }
    
   
}  