package com.foro.app.controller;

import com.foro.app.dto.Request.UsuarioRegisterRequest;
import com.foro.app.dto.Request.UsuarioUpdateRequest;
import com.foro.app.dto.Response.UsuarioResponse;
import com.foro.app.exceptions.UnauthorizedException;
import com.foro.app.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public String registrar(UsuarioRegisterRequest request, RedirectAttributes redirectAttributes) {
        usuarioService.registrarUsuario(request);
        redirectAttributes.addFlashAttribute("mensaje", "Registro completado con éxito. Ahora puedes iniciar sesión.");
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UsuarioResponse usuario = usuarioService.autenticarUsuario(email, password);
        session.setAttribute("usuario", usuario);
        redirectAttributes.addFlashAttribute("mensaje", "Bienvenido de nuevo, " + usuario.getNickname() + "!");
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("mensaje", "Has cerrado sesión exitosamente.");
        return "redirect:/index";
    }

    @GetMapping("/perfil/{nickname}")
    public String verPerfil(@PathVariable String nickname, Model model) {
        UsuarioResponse usuario = usuarioService.obtenerPerfilPublico(nickname);
        model.addAttribute("usuarioPerfil", usuario);
        model.addAttribute("pageTitle", "Perfil de " + usuario.getNickname());
        model.addAttribute("currentPage", "perfil");
        return "perfil";
    }

    @PostMapping("/perfil/actualizar")
    public String actualizarPerfil(UsuarioUpdateRequest request, HttpSession session,
            RedirectAttributes redirectAttributes) {
        UsuarioResponse loggedUser = (UsuarioResponse) session.getAttribute("usuario");
        if (loggedUser == null) {
            throw new UnauthorizedException("Debes iniciar sesión para actualizar tu perfil.");
        }

        UsuarioResponse updated = usuarioService.actualizarPerfil(loggedUser.getId(), request);
        session.setAttribute("usuario", updated);
        redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado con éxito.");
        return "redirect:/perfil/" + updated.getNickname();
    }

    @PostMapping("/admin/suspender")
    public String suspenderUsuario(@RequestParam Long usuarioId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        UsuarioResponse admin = (UsuarioResponse) session.getAttribute("usuario");
        if (admin == null) {
            throw new UnauthorizedException("Debes iniciar sesión como administrador.");
        }

        usuarioService.suspenderUsuario(admin.getId(), usuarioId);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario suspendido exitosamente.");
        return "redirect:/admin";
    }
}