package com.foro.app.service;

import com.foro.app.dto.Request.ComentarioCreateRequest;
import com.foro.app.dto.Response.ComentarioResponse;
import com.foro.app.entity.Comentario;
import com.foro.app.entity.Publicacion;
import com.foro.app.entity.Usuario;
import com.foro.app.exceptions.BadRequestException;
import com.foro.app.exceptions.ResourceNotFoundException;
import com.foro.app.exceptions.SuspendedUserException;
import com.foro.app.exceptions.UnauthorizedException;
import com.foro.app.mappers.ComentarioMapper;
import com.foro.app.repository.ComentarioRepository;
import com.foro.app.repository.PublicacionRepository;
import com.foro.app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComentarioMapper comentarioMapper;

    public ComentarioService(ComentarioRepository comentarioRepository,
            PublicacionRepository publicacionRepository,
            UsuarioRepository usuarioRepository,
            ComentarioMapper comentarioMapper) {
        this.comentarioRepository = comentarioRepository;
        this.publicacionRepository = publicacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.comentarioMapper = comentarioMapper;
    }

    @Transactional
    public ComentarioResponse agregarComentario(Long autorId, ComentarioCreateRequest request) {
        Usuario autor = usuarioRepository.findById(autorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        if (autor.isSuspendido()) {
            throw new SuspendedUserException("Cuenta suspendida. No puedes comentar.");
        }

        Publicacion publicacion = publicacionRepository.findById(request.getPublicacionId())
                .orElseThrow(() -> new ResourceNotFoundException("La publicación ya no existe."));

        String textoLimpio = HtmlUtils.htmlEscape(request.getTexto().trim());

        Comentario comentario = comentarioMapper.toEntity(request);
        comentario.setAutor(autor);
        comentario.setPublicacion(publicacion);
        comentario.setTexto(textoLimpio);

        comentario = comentarioRepository.save(comentario);
        return comentarioMapper.toResponse(comentario);
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponse> obtenerComentariosPorPublicacion(Long publicacionId) {
        List<Comentario> comentarios = comentarioRepository
                .findByPublicacionIdOrderByFechaAsc(publicacionId);
        return comentarios.stream()
                .map(comentarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminarComentario(Long usuarioId, Long comentarioId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado."));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        boolean esPropietario = comentario.getAutor().getId().equals(usuarioId);
        boolean esAdmin = usuario.getRol() == Usuario.Rol.admin;

        if (!esPropietario && !esAdmin) {
            throw new UnauthorizedException("No tienes permiso para eliminar este comentario.");
        }

        comentarioRepository.delete(comentario);
    }
}
