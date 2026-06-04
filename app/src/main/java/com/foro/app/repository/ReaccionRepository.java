package com.foro.app.repository;

import com.foro.app.entity.Reaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReaccionRepository extends JpaRepository<Reaccion, Long> {

    // Método para la Regla de Voto Único:
    // Busca si un usuario en específico ya dejó una reacción en una publicación específica
    Optional<Reaccion> findByPublicacionIdAndUsuarioId(Long publicacionId, Long usuarioId);

    // Método para el Filtro Discord (Límite de 4 Emojis):
    // Hace una consulta personalizada para contar cuántos TIPOS distintos de emojis hay en un hilo
    @Query("SELECT COUNT(DISTINCT r.tipo) FROM Reaccion r WHERE r.publicacion.id = :publicacionId")
    long countDistinctTipoByPublicacionId(@Param("publicacionId") Long publicacionId);

    // Método para calcular la Predominancia matemática (+2 / -2):
    // Cuenta cuántos votos tiene una publicación filtrados por tipo de emoji
    long countByPublicacionIdAndTipo(Long publicacionId, Reaccion.TipoReaccion tipo);
    
}