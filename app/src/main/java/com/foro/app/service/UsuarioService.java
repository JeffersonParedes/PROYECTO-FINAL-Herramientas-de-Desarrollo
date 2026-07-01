package com.foro.app.service;

import com.foro.app.dto.Request.UsuarioRegisterRequest;
import com.foro.app.dto.Request.UsuarioUpdateRequest;
import com.foro.app.dto.Response.UsuarioResponse;

public interface UsuarioService {

    UsuarioResponse registrarUsuario(UsuarioRegisterRequest request);

    UsuarioResponse autenticarUsuario(String email, String password);

    UsuarioResponse obtenerPerfilPublico(String nickname);

    UsuarioResponse actualizarPerfil(Long usuarioId, UsuarioUpdateRequest request);

    void suspenderUsuario(Long idEjecutor, Long idObjetivo);
}