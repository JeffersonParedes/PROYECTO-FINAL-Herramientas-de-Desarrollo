package com.foro.app.service;

import com.foro.app.entity.Reporte;

import java.util.List;

public interface ReporteService {

    Reporte crearReporte(
            Long usuarioId,
            String tipoContenido,
            Long contenidoId,
            String motivo
    );

    List<Reporte> obtenerReportes(Long usuarioId);

    void eliminarReporte(Long usuarioId,
                         Long reporteId);
}