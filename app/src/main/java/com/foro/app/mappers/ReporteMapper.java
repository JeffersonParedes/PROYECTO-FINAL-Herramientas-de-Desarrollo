package com.foro.app.mappers;

import com.foro.app.dto.Request.ReporteRequest;
import com.foro.app.dto.Response.ReporteResponse;
import com.foro.app.entity.Reporte;
import org.springframework.stereotype.Component;

@Component
public class ReporteMapper {

    public ReporteResponse toResponse(Reporte entity) {
        if (entity == null) {
            return null;
        }
        ReporteResponse response = new ReporteResponse();
        response.setId(entity.getId());
        if (entity.getUsuario() != null) {
            response.setUsuarioNickname(entity.getUsuario().getNickname());
            response.setUsuarioId(entity.getUsuario().getId());
        }
        response.setTipoContenido(entity.getTipoContenido() != null ? entity.getTipoContenido().name() : null);
        response.setContenidoId(entity.getContenidoId());
        response.setMotivo(entity.getMotivo() != null ? entity.getMotivo().name() : null);
        response.setFecha(entity.getFecha());
        return response;
    }

    public Reporte toEntity(ReporteRequest request) {
        if (request == null) {
            return null;
        }
        Reporte entity = new Reporte();
        if (request.getTipoContenido() != null) {
            entity.setTipoContenido(Reporte.TipoContenido.valueOf(request.getTipoContenido().toLowerCase()));
        }
        entity.setContenidoId(request.getContenidoId());
        if (request.getMotivo() != null) {
            entity.setMotivo(Reporte.MotivoReporte.valueOf(request.getMotivo().toLowerCase()));
        }
        return entity;
    }
}
