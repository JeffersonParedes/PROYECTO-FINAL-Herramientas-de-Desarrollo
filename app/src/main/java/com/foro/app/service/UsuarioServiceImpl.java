package com.foro.app.service;

import com.foro.app.dto.Request.UsuarioRegisterRequest;
import com.foro.app.dto.Request.UsuarioUpdateRequest;
import com.foro.app.dto.Response.UsuarioResponse;
import com.foro.app.entity.Usuario;
import com.foro.app.exceptions.BadRequestException;
import com.foro.app.exceptions.ResourceNotFoundException;
import com.foro.app.exceptions.SuspendedUserException;
import com.foro.app.exceptions.UnauthorizedException;
import com.foro.app.mappers.UsuarioMapper;
import com.foro.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Override
    @Transactional
    public UsuarioResponse registrarUsuario(UsuarioRegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El correo electrónico ya está registrado.");
        }

        if (usuarioRepository.existsByNickname(request.getNickname())) {
            throw new BadRequestException("El nickname ya está registrado.");
        }

        validarEnlace(request.getEnlace());

        Usuario usuario = usuarioMapper.toEntity(request);
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse autenticarUsuario(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el correo provisto."));

        if (!usuario.getPassword().equals(password)) {
            throw new BadRequestException("Contraseña incorrecta.");
        }

        if (usuario.isSuspendido()) {
            throw new SuspendedUserException("Tu cuenta ha sido suspendida. No puedes iniciar sesión.");
        }

        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPerfilPublico(String nickname) {
        Usuario usuario = usuarioRepository.findByNickname(nickname)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el nickname provisto."));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponse actualizarPerfil(Long usuarioId, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        validarEnlace(request.getEnlace());

        usuarioMapper.updateEntity(request, usuario);
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public void suspenderUsuario(Long idEjecutor, Long idObjetivo) {
        Usuario admin = usuarioRepository.findById(idEjecutor)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador ejecutor no encontrado."));

        if (admin.getRol() != Usuario.Rol.admin) {
            throw new UnauthorizedException("No tienes permisos para suspender usuarios.");
        }

        Usuario usuario = usuarioRepository.findById(idObjetivo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario a suspender no encontrado."));

        usuario.setSuspendido(true);
        usuarioRepository.save(usuario);
    }

    private void validarEnlace(String enlace) {
        if (enlace == null || enlace.isBlank()) {
            return;
        }
        // Simple regex matching common URL formats
        String urlRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]$";
        if (!enlace.matches(urlRegex)) {
            throw new BadRequestException("El enlace debe ser una dirección URL válida.");
        }
    }
}