package com.foro.app.service;

import com.foro.app.entity.Comentario;
import com.foro.app.entity.Publicacion;
import com.foro.app.entity.Reporte;
import com.foro.app.entity.Usuario;

import com.foro.app.repository.ComentarioRepository;
import com.foro.app.repository.PublicacionRepository;
import com.foro.app.repository.ReporteRepository;
import com.foro.app.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Reporte crearReporte(
            Long usuarioId,
            String tipoContenido,
            Long contenidoId,
            String motivo) {

        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        if (usuario.isSuspendido()) {
            throw new RuntimeException("Usuario suspendido");
        }

        Reporte.TipoContenido tipo =
                Reporte.TipoContenido.valueOf(tipoContenido);

        if (tipo == Reporte.TipoContenido.publicacion) {

            Publicacion publicacion =
                    publicacionRepository
                            .findById(contenidoId)
                            .orElseThrow(() ->
                                    new RuntimeException("Publicación no encontrada"));

        } else {

            Comentario comentario =
                    comentarioRepository
                            .findById(contenidoId)
                            .orElseThrow(() ->
                                    new RuntimeException("Comentario no encontrado"));
        }

        boolean existe =
                reporteRepository
                        .existsByUsuarioIdAndTipoContenidoAndContenidoId(
                                usuarioId,
                                tipo,
                                contenidoId
                        );

        if (existe) {
            throw new RuntimeException(
                    "Ya reportaste este contenido");
        }

        Reporte reporte = new Reporte();

        reporte.setUsuario(usuario);
        reporte.setTipoContenido(tipo);
        reporte.setContenidoId(contenidoId);

        reporte.setMotivo(
                Reporte.MotivoReporte.valueOf(motivo)
        );

        return reporteRepository.save(reporte);
    }

    @Override
    public List<Reporte> obtenerReportes(Long usuarioId) {

        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow();

        if (usuario.getRol() != Usuario.Rol.admin) {
            throw new RuntimeException("Acceso denegado");
        }

        return reporteRepository
                .findAllByOrderByFechaAsc();
    }

    @Override
    public void eliminarReporte(Long usuarioId,
                                Long reporteId) {

        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow();

        if (usuario.getRol() != Usuario.Rol.admin) {
            throw new RuntimeException("Acceso denegado");
        }

        reporteRepository.deleteById(reporteId);
    }
}