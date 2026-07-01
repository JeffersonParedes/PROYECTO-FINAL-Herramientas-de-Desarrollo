package com.foro.app.mappers;

import com.foro.app.dto.Request.UsuarioRegisterRequest;
import com.foro.app.dto.Request.UsuarioUpdateRequest;
import com.foro.app.dto.Response.UsuarioResponse;
import com.foro.app.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(Usuario entity) {
        if (entity == null) {
            return null;
        }
        UsuarioResponse response = new UsuarioResponse();
        response.setId(entity.getId());
        response.setNickname(entity.getNickname());
        response.setEmail(entity.getEmail());
        response.setDescripcion(entity.getDescripcion());
        response.setEnlace(entity.getEnlace());
        response.setRol(entity.getRol() != null ? entity.getRol().name() : null);
        response.setSuspendido(entity.isSuspendido());
        response.setFechaRegistro(entity.getFechaRegistro());
        return response;
    }

    public Usuario toEntity(UsuarioRegisterRequest request) {
        if (request == null) {
            return null;
        }
        Usuario entity = new Usuario();
        entity.setNickname(request.getNickname());
        entity.setEmail(request.getEmail());
        entity.setPassword(request.getPassword());
        entity.setDescripcion(request.getDescripcion());
        entity.setEnlace(request.getEnlace());
        entity.setRol(Usuario.Rol.user);
        entity.setSuspendido(false);
        return entity;
    }

    public void updateEntity(UsuarioUpdateRequest request, Usuario entity) {
        if (request == null || entity == null) {
            return;
        }
        entity.setDescripcion(request.getDescripcion());
        entity.setEnlace(request.getEnlace());
    }
}
