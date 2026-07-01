package com.foro.app.mappers;

import com.foro.app.dto.Request.ComentarioCreateRequest;
import com.foro.app.dto.Response.ComentarioResponse;
import com.foro.app.entity.Comentario;
import org.springframework.stereotype.Component;

@Component
public class ComentarioMapper {

    public ComentarioResponse toResponse(Comentario entity) {
        if (entity == null) {
            return null;
        }
        ComentarioResponse response = new ComentarioResponse();
        response.setId(entity.getId());
        response.setTexto(entity.getTexto());
        if (entity.getAutor() != null) {
            response.setAutorNickname(entity.getAutor().getNickname());
            response.setAutorId(entity.getAutor().getId());
        }
        if (entity.getPublicacion() != null) {
            response.setPublicacionId(entity.getPublicacion().getId());
        }
        response.setFecha(entity.getFecha());
        return response;
    }

    public Comentario toEntity(ComentarioCreateRequest request) {
        if (request == null) {
            return null;
        }
        Comentario entity = new Comentario();
        entity.setTexto(request.getTexto());
        return entity;
    }
}
