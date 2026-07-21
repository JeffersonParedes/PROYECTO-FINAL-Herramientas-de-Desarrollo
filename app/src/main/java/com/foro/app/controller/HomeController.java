package com.foro.app.controller;

import com.foro.app.dto.Response.PublicacionResponse;
import com.foro.app.dto.Response.ReporteResponse;
import com.foro.app.dto.Response.SubforoResponse;
import com.foro.app.dto.Response.UsuarioResponse;
import com.foro.app.exceptions.UnauthorizedException;
import com.foro.app.service.PublicacionService;
import com.foro.app.service.ReporteService;
import com.foro.app.service.SubforoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final SubforoService subforoService;
    private final PublicacionService publicacionService;
    private final ReporteService reporteService;

    public HomeController(SubforoService subforoService,
            PublicacionService publicacionService,
            ReporteService reporteService) {
        this.subforoService = subforoService;
        this.publicacionService = publicacionService;
        this.reporteService = reporteService;
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<PublicacionResponse> publicaciones = publicacionService.obtenerTodasPublicaciones();
        List<SubforoResponse> subforos = subforoService.obtenerTodosSubforos();

        model.addAttribute("pageTitle", "Inicio | Nexo Foro");
        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("subforos", subforos);
        model.addAttribute("currentPage", "index");
        return "index";
    }

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/index";
    }

    @GetMapping("/subforo")
    public String subforo(Model model) {
        List<SubforoResponse> subforos = subforoService.obtenerTodosSubforos();
        model.addAttribute("pageTitle", "Subforos");
        model.addAttribute("currentPage", "subforo");
        model.addAttribute("currentSubforoId", null);
        model.addAttribute("subforoAdulto", false);
        model.addAttribute("subforos", subforos);
        return "subforo";
    }

    @GetMapping("/tendencias")
    public String tendencias(Model model) {
        // Sort publications by score descending
        List<PublicacionResponse> publicaciones = publicacionService.obtenerTodasPublicaciones();
        publicaciones.sort((p1, p2) -> p2.getPuntuacion().compareTo(p1.getPuntuacion()));

        List<SubforoResponse> subforos = subforoService.obtenerTodosSubforos();

        model.addAttribute("pageTitle", "Tendencias | Nexo Foro");
        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("subforos", subforos);
        model.addAttribute("currentPage", "tendencias");
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return "redirect:/index";
        }
        model.addAttribute("pageTitle", "Iniciar Sesión");
        return "login";
    }

    @GetMapping("/registro")
    public String registro(Model model, HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return "redirect:/index";
        }
        model.addAttribute("pageTitle", "Crear Cuenta");
        return "registro";
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null || !"admin".equalsIgnoreCase(loggedUser.getRol())) {
            throw new UnauthorizedException("Acceso denegado. Se requiere cuenta de administrador.");
        }

        List<ReporteResponse> reportes = reporteService.obtenerReportes(loggedUser.getId());
        List<SubforoResponse> subforosPrincipales = subforoService.obtenerSubforosPrincipales();
        List<PublicacionResponse> publicaciones = publicacionService.obtenerTodasPublicaciones();
        List<SubforoResponse> todosSubforos = subforoService.obtenerTodosSubforos();

        model.addAttribute("pageTitle", "Panel de Administración");
        model.addAttribute("listaReportes", reportes);
        model.addAttribute("subforosPrincipales", subforosPrincipales);
        model.addAttribute("listaPublicaciones", publicaciones);
        model.addAttribute("todosSubforos", todosSubforos);
        return "admin";
    }

    @GetMapping("/crear")
    public String crear(Model model, HttpSession session) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null) {
            throw new UnauthorizedException("Debes iniciar sesión para crear una publicación.");
        }

        model.addAttribute("pageTitle", "Crear Publicación");
        model.addAttribute("subforos", subforoService.obtenerTodosSubforos());
        return "crear";
    }

    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null) {
            throw new UnauthorizedException("Debes iniciar sesión para ver tu perfil.");
        }
        return "redirect:/perfil/" + loggedUser.getNickname();
    }
}