package com.foro.app.controller;

import com.foro.app.dto.UsuarioDTO;
import com.foro.app.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public String registrar(UsuarioDTO dto) {

        usuarioService.registrarUsuario(dto);

        return "redirect:/login";
        
    }
    @PostMapping("/login")
public String login(String email,
                    String password) {

    usuarioService.autenticarUsuario(email, password);

    return "redirect:/index";
                    }
    @GetMapping("/perfil/{nickname}")
public String verPerfil(@PathVariable String nickname,
                        Model model) {

    UsuarioDTO usuario =
            usuarioService.obtenerPerfilPublico(nickname);

    model.addAttribute("usuario", usuario);

    return "perfil";
}
@PostMapping("/perfil/actualizar")
public String actualizarPerfil(UsuarioDTO dto) {

    Long usuarioId = 1L;

    usuarioService.actualizarPerfil(usuarioId, dto);

    return "redirect:/perfil";
}
@PostMapping("/admin/suspender")
public String suspenderUsuario(Long usuarioId) {

    Long adminId = 1L;

    usuarioService.suspenderUsuario(
            adminId,
            usuarioId);

    return "redirect:/admin";
}
}