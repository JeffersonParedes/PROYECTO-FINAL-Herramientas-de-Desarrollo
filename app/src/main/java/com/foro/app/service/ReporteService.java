package com.foro.app.service;

import com.foro.app.dto.Request.ReporteRequest;
import com.foro.app.dto.Response.ReporteResponse;

import java.util.List;

public interface ReporteService {

    ReporteResponse crearReporte(Long usuarioId, ReporteRequest request);

    List<ReporteResponse> obtenerReportes(Long usuarioId);

    void eliminarReporte(Long usuarioId, Long reporteId);
}