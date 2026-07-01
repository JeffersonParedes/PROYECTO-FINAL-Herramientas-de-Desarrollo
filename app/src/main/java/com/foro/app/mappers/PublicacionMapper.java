package com.foro.app.mappers;

import com.foro.app.dto.Request.PublicacionCreateRequest;
import com.foro.app.dto.Response.PublicacionResponse;
import com.foro.app.entity.Publicacion;
import org.springframework.stereotype.Component;

@Component
public class PublicacionMapper {

    public PublicacionResponse toResponse(Publicacion entity) {
        if (entity == null) {
            return null;
        }
        PublicacionResponse response = new PublicacionResponse();
        response.setId(entity.getId());
        response.setTitulo(entity.getTitulo());
        response.setDescripcion(entity.getDescripcion());
        response.setContenido(entity.getContenido());
        response.setImagen(entity.getImagen());
        response.setVideo(entity.getVideo());
        response.setAudio(entity.getAudio());
        if (entity.getAutor() != null) {
            response.setAutorNickname(entity.getAutor().getNickname());
            response.setAutorId(entity.getAutor().getId());
        }
        if (entity.getSubforo() != null) {
            response.setSubforoId(entity.getSubforo().getId());
            response.setSubforoNombre(entity.getSubforo().getNombre());
        }
        response.setPuntuacion(entity.getPuntuacion());
        response.setFechaCreacion(entity.getFechaCreacion());
        return response;
    }

    public Publicacion toEntity(PublicacionCreateRequest request) {
        if (request == null) {
            return null;
        }
        Publicacion entity = new Publicacion();
        entity.setTitulo(request.getTitulo());
        entity.setDescripcion(request.getDescripcion());
        entity.setContenido(request.getContenido());
        // Note: files will be uploaded and their path set in service layer
        entity.setPuntuacion(0);
        return entity;
    }
}
