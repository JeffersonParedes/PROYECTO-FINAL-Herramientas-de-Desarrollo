package com.foro.app.service;

import com.foro.app.dto.UsuarioDTO;
import com.foro.app.entity.Usuario;
import com.foro.app.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario registrarUsuario(UsuarioDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }

        if (usuarioRepository.existsByNickname(dto.getNickname())) {
            throw new RuntimeException("Nickname ya registrado");
        }

        Usuario usuario = new Usuario();

        usuario.setNickname(dto.getNickname());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario autenticarUsuario(String email, String password) {

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (usuario.isSuspendido()) {
            throw new RuntimeException("Cuenta suspendida");
        }

        return usuario;
    }

    @Override
    public UsuarioDTO obtenerPerfilPublico(String nickname) {

        Usuario usuario = usuarioRepository
                .findByNickname(nickname)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(usuario.getId());
        dto.setNickname(usuario.getNickname());
        dto.setDescripcion(usuario.getDescripcion());
        dto.setEnlace(usuario.getEnlace());
        dto.setRol(usuario.getRol().name());

        return dto;
    }

    @Override
    public Usuario actualizarPerfil(Long usuarioId, UsuarioDTO dto) {

        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        usuario.setDescripcion(dto.getDescripcion());
        usuario.setEnlace(dto.getEnlace());

        return usuarioRepository.save(usuario);
    }

    @Override
    public void suspenderUsuario(Long idEjecutor, Long idObjetivo) {

        Usuario admin = usuarioRepository
                .findById(idEjecutor)
                .orElseThrow();

        if (admin.getRol() != Usuario.Rol.admin) {
            throw new RuntimeException("No tienes permisos");
        }

        Usuario usuario = usuarioRepository
                .findById(idObjetivo)
                .orElseThrow();

        usuario.setSuspendido(true);

        usuarioRepository.save(usuario);
    }
}