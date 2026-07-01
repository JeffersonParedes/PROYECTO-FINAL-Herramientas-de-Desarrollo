package com.foro.app.service;

import com.foro.app.dto.Request.ReporteRequest;
import com.foro.app.dto.Response.ReporteResponse;
import com.foro.app.entity.Comentario;
import com.foro.app.entity.Publicacion;
import com.foro.app.entity.Reporte;
import com.foro.app.entity.Usuario;
import com.foro.app.exceptions.BadRequestException;
import com.foro.app.exceptions.ResourceNotFoundException;
import com.foro.app.exceptions.SuspendedUserException;
import com.foro.app.exceptions.UnauthorizedException;
import com.foro.app.mappers.ReporteMapper;
import com.foro.app.repository.ComentarioRepository;
import com.foro.app.repository.PublicacionRepository;
import com.foro.app.repository.ReporteRepository;
import com.foro.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ReporteMapper reporteMapper;

    @Override
    @Transactional
    public ReporteResponse crearReporte(Long usuarioId, ReporteRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        if (usuario.isSuspendido()) {
            throw new SuspendedUserException("Usuario suspendido. No puedes reportar contenido.");
        }

        Reporte.TipoContenido tipo;
        try {
            tipo = Reporte.TipoContenido.valueOf(request.getTipoContenido().toLowerCase());
        } catch (Exception e) {
            throw new BadRequestException("Tipo de contenido inválido: " + request.getTipoContenido());
        }

        if (tipo == Reporte.TipoContenido.publicacion) {
            Publicacion publicacion = publicacionRepository.findById(request.getContenidoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada."));
        } else {
            Comentario comentario = comentarioRepository.findById(request.getContenidoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado."));
        }

        boolean existe = reporteRepository.existsByUsuarioIdAndTipoContenidoAndContenidoId(
                usuarioId,
                tipo,
                request.getContenidoId());

        if (existe) {
            throw new BadRequestException("Ya has reportado este contenido.");
        }

        Reporte reporte = reporteMapper.toEntity(request);
        reporte.setUsuario(usuario);

        try {
            reporte.setMotivo(Reporte.MotivoReporte.valueOf(request.getMotivo().toLowerCase()));
        } catch (Exception e) {
            throw new BadRequestException("Motivo de reporte inválido: " + request.getMotivo());
        }

        reporte = reporteRepository.save(reporte);
        return reporteMapper.toResponse(reporte);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> obtenerReportes(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        if (usuario.getRol() != Usuario.Rol.admin) {
            throw new UnauthorizedException("Acceso denegado. No tienes permisos de administrador.");
        }

        return reporteRepository.findAllByOrderByFechaAsc().stream()
                .map(reporteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarReporte(Long usuarioId, Long reporteId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        if (usuario.getRol() != Usuario.Rol.admin) {
            throw new UnauthorizedException("Acceso denegado. No tienes permisos de administrador.");
        }

        if (!reporteRepository.existsById(reporteId)) {
            throw new ResourceNotFoundException("Reporte no encontrado.");
        }

        reporteRepository.deleteById(reporteId);
    }
}