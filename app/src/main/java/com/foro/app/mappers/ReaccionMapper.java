package com.foro.app.mappers;

import com.foro.app.dto.Request.ReaccionRequest;
import com.foro.app.dto.Response.ReaccionResponse;
import com.foro.app.entity.Reaccion;
import org.springframework.stereotype.Component;

@Component
public class ReaccionMapper {

    public ReaccionResponse toResponse(Reaccion entity) {
        if (entity == null) {
            return null;
        }
        ReaccionResponse response = new ReaccionResponse();
        response.setId(entity.getId());
        if (entity.getPublicacion() != null) {
            response.setPublicacionId(entity.getPublicacion().getId());
        }
        if (entity.getUsuario() != null) {
            response.setUsuarioNickname(entity.getUsuario().getNickname());
            response.setUsuarioId(entity.getUsuario().getId());
        }
        response.setTipo(entity.getTipo() != null ? entity.getTipo().name() : null);
        return response;
    }

    public Reaccion toEntity(ReaccionRequest request) {
        if (request == null) {
            return null;
        }
        Reaccion entity = new Reaccion();
        if (request.getTipo() != null) {
            entity.setTipo(Reaccion.TipoReaccion.valueOf(request.getTipo().toLowerCase()));
        }
        return entity;
    }
}
