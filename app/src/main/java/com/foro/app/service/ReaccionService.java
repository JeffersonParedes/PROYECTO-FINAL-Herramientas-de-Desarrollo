package com.foro.app.service;

import com.foro.app.dto.Request.ReaccionRequest;
import com.foro.app.entity.Publicacion;
import com.foro.app.entity.Reaccion;
import com.foro.app.entity.Reaccion.TipoReaccion;
import com.foro.app.entity.Usuario;
import com.foro.app.exceptions.BadRequestException;
import com.foro.app.exceptions.ResourceNotFoundException;
import com.foro.app.exceptions.SuspendedUserException;
import com.foro.app.repository.PublicacionRepository;
import com.foro.app.repository.ReaccionRepository;
import com.foro.app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReaccionService {

    private final ReaccionRepository reaccionRepository;
    private final PublicacionRepository publicacionRepository;
    private final PublicacionService publicacionService;
    private final UsuarioRepository usuarioRepository;

    public ReaccionService(ReaccionRepository reaccionRepository,
            PublicacionRepository publicacionRepository,
            PublicacionService publicacionService,
            UsuarioRepository usuarioRepository) {
        this.reaccionRepository = reaccionRepository;
        this.publicacionRepository = publicacionRepository;
        this.publicacionService = publicacionService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void procesarReaccion(Long usuarioId, ReaccionRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        if (usuario.isSuspendido()) {
            throw new SuspendedUserException("Cuenta suspendida. No puedes reaccionar.");
        }

        Publicacion publicacion = publicacionRepository.findById(request.getPublicacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada."));

        TipoReaccion nuevoTipo;
        try {
            nuevoTipo = TipoReaccion.valueOf(request.getTipo().toLowerCase());
        } catch (Exception e) {
            throw new BadRequestException("Tipo de reacción inválido: " + request.getTipo());
        }

        Optional<Reaccion> reaccionExistente = reaccionRepository
                .findByPublicacionIdAndUsuarioId(request.getPublicacionId(), usuarioId);

        if (reaccionExistente.isPresent()) {
            Reaccion reaccion = reaccionExistente.get();
            // If user clicks the exact same reaction, remove it (toggle behavior)
            if (reaccion.getTipo() == nuevoTipo) {
                reaccionRepository.delete(reaccion);
            } else {
                reaccion.setTipo(nuevoTipo);
                reaccionRepository.save(reaccion);
            }
        } else {
            long tiposDistintos = reaccionRepository.countDistinctTipoByPublicacionId(request.getPublicacionId());
            boolean tipoYaExiste = reaccionRepository.countByPublicacionIdAndTipo(request.getPublicacionId(),
                    nuevoTipo) > 0;

            if (tiposDistintos >= 4 && !tipoYaExiste) {
                throw new BadRequestException(
                        "Límite de tipos de reacción alcanzado (máximo 4 emojis distintos por publicación).");
            }

            Reaccion nuevaReaccion = new Reaccion();
            nuevaReaccion.setUsuario(usuario);
            nuevaReaccion.setPublicacion(publicacion);
            nuevaReaccion.setTipo(nuevoTipo);
            reaccionRepository.save(nuevaReaccion);
        }

        calcularPredominancia(request.getPublicacionId());
    }

    @Transactional
    public void calcularPredominancia(Long publicacionId) {
        long votosPositivos = reaccionRepository.countByPublicacionIdAndTipo(publicacionId, TipoReaccion.positivo)
                + reaccionRepository.countByPublicacionIdAndTipo(publicacionId, TipoReaccion.risa);

        long votosNegativos = reaccionRepository.countByPublicacionIdAndTipo(publicacionId, TipoReaccion.negativo)
                + reaccionRepository.countByPublicacionIdAndTipo(publicacionId, TipoReaccion.sorpresa);

        int nuevoEstado;
        if (votosPositivos > votosNegativos) {
            nuevoEstado = 2;
        } else if (votosNegativos > votosPositivos) {
            nuevoEstado = -2;
        } else {
            nuevoEstado = 0;
        }

        publicacionService.actualizarEstadoPredominante(publicacionId, nuevoEstado);
    }
}
