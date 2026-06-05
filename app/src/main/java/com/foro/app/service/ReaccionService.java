package com.foro.app.service;

import com.foro.app.entity.Publicacion;
import com.foro.app.entity.Reaccion;
import com.foro.app.entity.Reaccion.TipoReaccion;
import com.foro.app.entity.Usuario;
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
    public void procesarReaccion(Long usuarioId, Long publicacionId, String tipoEmoji) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (usuario.isSuspendido()) {
            throw new IllegalArgumentException("Cuenta suspendida");
        }
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));

        TipoReaccion nuevoTipo;
        try {
            nuevoTipo = TipoReaccion.valueOf(tipoEmoji.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de reacción inválido: " + tipoEmoji);
        }

        Optional<Reaccion> reaccionExistente = reaccionRepository
                .findByPublicacionIdAndUsuarioId(publicacionId, usuarioId);

        if (reaccionExistente.isPresent()) {
            Reaccion reaccion = reaccionExistente.get();
            reaccion.setTipo(nuevoTipo);
            reaccionRepository.save(reaccion);
        } else {
            long tiposDistintos = reaccionRepository.countDistinctTipoByPublicacionId(publicacionId);
            boolean tipoYaExiste = reaccionRepository.countByPublicacionIdAndTipo(publicacionId, nuevoTipo) > 0;

            if (tiposDistintos >= 4 && !tipoYaExiste) {
                throw new IllegalArgumentException("Límite de tipos de reacción alcanzado");
            }

            Reaccion nuevaReaccion = new Reaccion();
            nuevaReaccion.setUsuario(usuario);
            nuevaReaccion.setPublicacion(publicacion);
            nuevaReaccion.setTipo(nuevoTipo);
            reaccionRepository.save(nuevaReaccion);
        }

        calcularPredominancia(publicacionId);
    }

    @Transactional
    public void calcularPredominancia(Long publicacionId) {
        long votosPositivos = reaccionRepository.countByPublicacionIdAndTipo(publicacionId, TipoReaccion.positivo)
                + reaccionRepository.countByPublicacionIdAndTipo(publicacionId, TipoReaccion.risa);

        long votosNegativos = reaccionRepository.countByPublicacionIdAndTipo(publicacionId, TipoReaccion.negativo)
                + reaccionRepository.countByPublicacionIdAndTipo(publicacionId, TipoReaccion.sorpresa);

        long total = votosPositivos + votosNegativos;
        int nuevoEstado;

        if (total == 0) {
            nuevoEstado = 0;
        } else {
            double porcentajePositivo = (double) votosPositivos / total * 100;
            nuevoEstado = porcentajePositivo > 50 ? 2 : -2;
        }

        publicacionService.actualizarEstadoPredominante(publicacionId, nuevoEstado);
    }
}
