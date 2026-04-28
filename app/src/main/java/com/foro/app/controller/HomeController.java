package com.foro.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/publicacion")
    public String publicacion(Model model) {
        model.addAttribute("pageTitle", "Publicacion");
        model.addAttribute("currentPage", "publicacion");
        model.addAttribute("subforos", List.of());
        model.addAttribute("publicacion", Map.of(
                "subforo", "Herramientas de Desarrollo",
                "adulto", false,
                "titulo", "Como organizar ramas en GitHub para un proyecto en equipo",
                "autor", "Sandy",
                "fecha", "27 abr 2026",
                "descripcion", "Estamos trabajando en un foro universitario y queriamos dejar bien claro como dividir ramas, fusionar cambios y documentar conflictos sin perder el orden del proyecto.",
                "mediaTitulo", "Espacio multimedia",
                "mediaDescripcion", "Imagen, video o audio adjunto a la publicacion.",
                "puntuacion", "+2"
        ));
        model.addAttribute("comentarios", List.of(
                Map.of("autor", "JeanFranco", "fecha", "27/04/2026", "texto", "Me gusta que el flujo de ramas quede claro porque nos ayudara a evitar conflictos graves."),
                Map.of("autor", "Angie", "fecha", "27/04/2026", "texto", "Tambien deberiamos mostrar en la expo un ejemplo controlado de merge conflict para que la miss vea el trabajo colaborativo."),
                Map.of("autor", "Leonardo", "fecha", "27/04/2026", "texto", "La documentacion en Word va a cerrar bien si explicamos el problema, la solucion y la decision final del equipo.")
        ));
        return "publicacion";
    }

    @GetMapping("/crear")
    public String crear(Model model) {
        model.addAttribute("pageTitle", "Crear publicacion");
        model.addAttribute("currentPage", "crear");
        model.addAttribute("subforos", List.of("Herramientas de Desarrollo", "Programacion Web", "Base de Datos", "Vida universitaria"));
        model.addAttribute("publicacionesRestantes", 2);
        return "crear";
    }

    @GetMapping("/perfil")
    public String perfil(Model model) {
        model.addAttribute("pageTitle", "Perfil");
        model.addAttribute("currentPage", "perfil");
        model.addAttribute("subforos", List.of());
        model.addAttribute("perfil", Map.of(
                "nickname", "Sandy",
                "descripcion", "Estudiante interesada en desarrollo web, organizacion de proyectos colaborativos y presentaciones visuales para exponer mejor las ideas del equipo.",
                "link", "#",
                "totalPublicaciones", 8,
                "totalComentarios", 21
        ));
        model.addAttribute("actividad", List.of(
                Map.of("tipo", "Publicacion", "texto", "Como organizar ramas en GitHub para un proyecto en equipo", "fecha", "Hace 2 horas"),
                Map.of("tipo", "Comentario", "texto", "La parte creativa del foro ya quedo definida con nombre, colores y estilo base.", "fecha", "Hace 5 horas"),
                Map.of("tipo", "Publicacion", "texto", "Ideas para documentar conflictos controlados en la expo", "fecha", "Ayer")
        ));
        return "perfil";
    }
}
