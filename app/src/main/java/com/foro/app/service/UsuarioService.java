package com.foro.app.service;

import com.foro.app.dto.UsuarioDTO;
import com.foro.app.entity.Usuario;

public interface UsuarioService {

    Usuario registrarUsuario(UsuarioDTO dto);

    Usuario autenticarUsuario(String email, String password);

    UsuarioDTO obtenerPerfilPublico(String nickname);

    Usuario actualizarPerfil(Long usuarioId, UsuarioDTO dto);

    void suspenderUsuario(Long idEjecutor, Long idObjetivo);
}