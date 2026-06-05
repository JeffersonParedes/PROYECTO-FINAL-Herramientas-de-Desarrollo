package com.foro.app.service;

import com.foro.app.dto.PublicacionDTO;
import com.foro.app.entity.Publicacion;
import com.foro.app.entity.Subforo;
import com.foro.app.entity.Usuario;
import com.foro.app.repository.PublicacionRepository;
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

    public PublicacionService(PublicacionRepository publicacionRepository,
                              UsuarioRepository usuarioRepository,
                              SubforoRepository subforoRepository) {
        this.publicacionRepository = publicacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.subforoRepository = subforoRepository;
    }

    @Transactional
    public PublicacionDTO crearPublicacion(Long autorId, Long subforoId, String titulo,
                                           String descripcion, String imagen,
                                           String video, String audio) {
        Usuario autor = usuarioRepository.findById(autorId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (autor.isSuspendido()) {
            throw new IllegalArgumentException("Cuenta suspendida");
        }

        Subforo subforo = subforoRepository.findById(subforoId)
                .orElseThrow(() -> new IllegalArgumentException("El subforo no existe"));

        if (imagen != null && !imagen.isEmpty()) {
            validarMultimedia(imagen, "imagen");
        }
        if (video != null && !video.isEmpty()) {
            validarMultimedia(video, "video");
        }
        if (audio != null && !audio.isEmpty()) {
            validarMultimedia(audio, "audio");
        }

        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setAutor(autor);
        publicacion.setSubforo(subforo);
        publicacion.setImagen(imagen);
        publicacion.setVideo(video);
        publicacion.setAudio(audio);
        publicacion.setPuntuacion(0);

        publicacion = publicacionRepository.save(publicacion);

        return toDTO(publicacion);
    }

    @Transactional(readOnly = true)
    public List<PublicacionDTO> obtenerPublicacionesPorSubforo(Long subforoId) {
        List<Publicacion> publicaciones = publicacionRepository
                .findBySubforoIdOrderByFechaCreacionDesc(subforoId);
        return publicaciones.stream().map(p -> {
            PublicacionDTO dto = toDTO(p);
            if (dto.getDescripcion() != null && dto.getDescripcion().length() > 100) {
                dto.setDescripcion(dto.getDescripcion().substring(0, 100) + "...");
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PublicacionDTO obtenerDetallePublicacion(Long publicacionId) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));
        return toDTO(publicacion);
    }

    @Transactional
    public void actualizarEstadoPredominante(Long publicacionId, Integer nuevoEstado) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));
        publicacion.setPuntuacion(nuevoEstado);
        publicacionRepository.save(publicacion);
    }

    private void validarMultimedia(String url, String tipo) {
        String ext = url.substring(url.lastIndexOf('.') + 1).toLowerCase();
        switch (tipo) {
            case "imagen":
                if (!ext.matches("png|jpg|jpeg|gif|webp")) {
                    throw new IllegalArgumentException("Formato de imagen no válido: " + ext);
                }
                break;
            case "video":
                if (!ext.matches("mp4|webm|avi|mov")) {
                    throw new IllegalArgumentException("Formato de video no válido: " + ext);
                }
                break;
            case "audio":
                if (!ext.matches("mp3|wav|ogg|aac")) {
                    throw new IllegalArgumentException("Formato de audio no válido: " + ext);
                }
                break;
        }
    }

    private PublicacionDTO toDTO(Publicacion p) {
        PublicacionDTO dto = new PublicacionDTO();
        dto.setId(p.getId());
        dto.setTitulo(p.getTitulo());
        dto.setDescripcion(p.getDescripcion());
        dto.setContenido(p.getContenido());
        dto.setImagen(p.getImagen());
        dto.setVideo(p.getVideo());
        dto.setAudio(p.getAudio());
        dto.setAutorNickname(p.getAutor().getNickname());
        dto.setSubforoId(p.getSubforo().getId());
        dto.setSubforoNombre(p.getSubforo().getNombre());
        dto.setPuntuacion(p.getPuntuacion());
        dto.setFechaCreacion(p.getFechaCreacion());
        return dto;
    }
}
