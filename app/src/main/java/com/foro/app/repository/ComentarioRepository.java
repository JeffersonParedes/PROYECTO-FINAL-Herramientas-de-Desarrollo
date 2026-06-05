package com.foro.app.repository;

import com.foro.app.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // Método CLAVE para obtenerComentariosPorPublicacion():
    // Busca todos los comentarios de una publicación y los ordena cronológicamente (el más viejo primero)
    List<Comentario> findByPublicacionIdOrderByFechaAsc(Long publicacionId);
    
}