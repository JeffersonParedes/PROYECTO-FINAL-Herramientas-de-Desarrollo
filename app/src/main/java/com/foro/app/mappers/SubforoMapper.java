package com.foro.app.mappers;

import com.foro.app.dto.Request.SubforoCreateRequest;
import com.foro.app.dto.Response.SubforoJerarquiaResponse;
import com.foro.app.dto.Response.SubforoResponse;
import com.foro.app.entity.Subforo;
import org.springframework.stereotype.Component;

@Component
public class SubforoMapper {

    public SubforoResponse toResponse(Subforo entity) {
        if (entity == null) {
            return null;
        }
        SubforoResponse response = new SubforoResponse();
        response.setId(entity.getId());
        response.setNombre(entity.getNombre());
        response.setDescripcion(entity.getDescripcion());
        if (entity.getParent() != null) {
            response.setParentId(entity.getParent().getId());
            response.setParentNombre(entity.getParent().getNombre());
        }
        return response;
    }

    public SubforoJerarquiaResponse toJerarquiaResponse(Subforo entity, int nivel) {
        if (entity == null) {
            return null;
        }
        SubforoJerarquiaResponse response = new SubforoJerarquiaResponse();
        response.setId(entity.getId());
        response.setNombre(entity.getNombre());
        response.setDescripcion(entity.getDescripcion());
        response.setNivel(nivel);
        if (entity.getParent() != null) {
            response.setParentId(entity.getParent().getId());
        }
        return response;
    }

    public Subforo toEntity(SubforoCreateRequest request) {
        if (request == null) {
            return null;
        }
        Subforo entity = new Subforo();
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        return entity;
    }
}
