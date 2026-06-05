package com.foro.app.service;

import com.foro.app.entity.Comentario;
import com.foro.app.entity.Publicacion;
import com.foro.app.entity.Usuario;
import com.foro.app.repository.ComentarioRepository;
import com.foro.app.repository.PublicacionRepository;
import com.foro.app.repository.UsuarioRepository;
import com.foro.app.dto.ComentarioDTO;
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

    public ComentarioService(ComentarioRepository comentarioRepository,
                             PublicacionRepository publicacionRepository,
                             UsuarioRepository usuarioRepository) {
        this.comentarioRepository = comentarioRepository;
        this.publicacionRepository = publicacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ComentarioDTO agregarComentario(Long autorId, Long publicacionId, String texto) {
        Usuario autor = usuarioRepository.findById(autorId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (autor.isSuspendido()) {
            throw new IllegalArgumentException("Cuenta suspendida");
        }
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new IllegalArgumentException("La publicación ya no existe"));

        String textoLimpio = HtmlUtils.htmlEscape(texto.trim());

        Comentario comentario = new Comentario();
        comentario.setAutor(autor);
        comentario.setPublicacion(publicacion);
        comentario.setTexto(textoLimpio);

        comentario = comentarioRepository.save(comentario);

        ComentarioDTO dto = new ComentarioDTO();
        dto.setId(comentario.getId());
        dto.setTexto(comentario.getTexto());
        dto.setAutorNickname(comentario.getAutor().getNickname());
        dto.setPublicacionId(comentario.getPublicacion().getId());
        dto.setFecha(comentario.getFecha());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<ComentarioDTO> obtenerComentariosPorPublicacion(Long publicacionId) {
        List<Comentario> comentarios = comentarioRepository
                .findByPublicacionIdOrderByFechaAsc(publicacionId);
        return comentarios.stream().map(c -> {
            ComentarioDTO dto = new ComentarioDTO();
            dto.setId(c.getId());
            dto.setTexto(c.getTexto());
            dto.setAutorNickname(c.getAutor().getNickname());
            dto.setPublicacionId(c.getPublicacion().getId());
            dto.setFecha(c.getFecha());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void eliminarComentario(Long usuarioId, Long comentarioId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        boolean esPropietario = comentario.getAutor().getId().equals(usuarioId);
        boolean esAdmin = usuario.getRol() == Usuario.Rol.admin;

        if (!esPropietario && !esAdmin) {
            throw new IllegalArgumentException("No tienes permiso para eliminar este comentario");
        }

        comentarioRepository.delete(comentario);
    }
}
