package com.foro.app.service;

import com.foro.app.dto.Request.PublicacionCreateRequest;
import com.foro.app.dto.Response.PublicacionResponse;
import com.foro.app.entity.Publicacion;
import com.foro.app.entity.Subforo;
import com.foro.app.entity.Usuario;
import com.foro.app.entity.Reporte;
import com.foro.app.exceptions.BadRequestException;
import com.foro.app.exceptions.ResourceNotFoundException;
import com.foro.app.exceptions.SuspendedUserException;
import com.foro.app.exceptions.UnauthorizedException;
import com.foro.app.mappers.PublicacionMapper;
import com.foro.app.repository.ComentarioRepository;
import com.foro.app.repository.PublicacionRepository;
import com.foro.app.repository.ReaccionRepository;
import com.foro.app.repository.ReporteRepository;
import com.foro.app.repository.SubforoRepository;
import com.foro.app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final SubforoRepository subforoRepository;
    private final PublicacionMapper publicacionMapper;
    private final MultimediaStorageService multimediaStorageService;
    private final ComentarioRepository comentarioRepository;
    private final ReaccionRepository reaccionRepository;
    private final ReporteRepository reporteRepository;

    public PublicacionService(PublicacionRepository publicacionRepository,
            UsuarioRepository usuarioRepository,
            SubforoRepository subforoRepository,
            PublicacionMapper publicacionMapper,
            MultimediaStorageService multimediaStorageService,
            ComentarioRepository comentarioRepository,
            ReaccionRepository reaccionRepository,
            ReporteRepository reporteRepository) {
        this.publicacionRepository = publicacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.subforoRepository = subforoRepository;
        this.publicacionMapper = publicacionMapper;
        this.multimediaStorageService = multimediaStorageService;
        this.comentarioRepository = comentarioRepository;
        this.reaccionRepository = reaccionRepository;
        this.reporteRepository = reporteRepository;
    }

    @Transactional
    public PublicacionResponse crearPublicacion(Long autorId, PublicacionCreateRequest request) {
        Usuario autor = usuarioRepository.findById(autorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        if (autor.isSuspendido()) {
            throw new SuspendedUserException("Cuenta suspendida. No puedes crear publicaciones.");
        }

        Subforo subforo = subforoRepository.findById(request.getSubforoId())
                .orElseThrow(() -> new ResourceNotFoundException("El subforo no existe."));

        // Store real files
        String imagenPath = multimediaStorageService.storeFile(request.getImagenFile(), "imagen");
        String videoPath = multimediaStorageService.storeFile(request.getVideoFile(), "video");
        String audioPath = multimediaStorageService.storeFile(request.getAudioFile(), "audio");

        Publicacion publicacion = publicacionMapper.toEntity(request);
        publicacion.setAutor(autor);
        publicacion.setSubforo(subforo);
        publicacion.setImagen(imagenPath);
        publicacion.setVideo(videoPath);
        publicacion.setAudio(audioPath);

        if (publicacion.getDescripcion() == null || publicacion.getDescripcion().isBlank()) {
            String content = publicacion.getContenido();
            publicacion.setDescripcion(
                    content != null && content.length() > 200 ? content.substring(0, 200) + "..." : content);
        }

        publicacion = publicacionRepository.save(publicacion);

        return publicacionMapper.toResponse(publicacion);
    }

    @Transactional(readOnly = true)
    public List<PublicacionResponse> obtenerPublicacionesPorSubforo(Long subforoId) {
        List<Publicacion> publicaciones = publicacionRepository
                .findBySubforoIdOrderByFechaCreacionDesc(subforoId);
        return publicaciones.stream().map(p -> {
            PublicacionResponse res = publicacionMapper.toResponse(p);
            if (res.getDescripcion() != null && res.getDescripcion().length() > 100) {
                res.setDescripcion(res.getDescripcion().substring(0, 100) + "...");
            }
            return res;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PublicacionResponse> obtenerTodasPublicaciones() {
        return publicacionRepository.findAll().stream()
                .map(publicacionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PublicacionResponse obtenerDetallePublicacion(Long publicacionId) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada."));
        return publicacionMapper.toResponse(publicacion);
    }

    @Transactional
    public void actualizarEstadoPredominante(Long publicacionId, Integer nuevoEstado) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada."));
        publicacion.setPuntuacion(nuevoEstado);
        publicacionRepository.save(publicacion);
    }

    @Transactional
    public void eliminarPublicacion(Long adminId, Long publicacionId) {
        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        if (admin.getRol() != Usuario.Rol.admin) {
            throw new UnauthorizedException("No tienes permisos para eliminar publicaciones.");
        }

        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada."));

        // Limpiar entidades relacionadas
        comentarioRepository.deleteByPublicacionId(publicacionId);
        reaccionRepository.deleteByPublicacionId(publicacionId);
        reporteRepository.deleteByTipoContenidoAndContenidoId(Reporte.TipoContenido.publicacion, publicacionId);

        // Eliminar publicación
        publicacionRepository.delete(publicacion);
    }
}
