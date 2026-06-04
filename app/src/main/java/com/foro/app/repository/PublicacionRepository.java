package com.foro.app.repository;

import com.foro.app.entity.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    // Método CLAVE para obtenerPublicacionesPorSubforo():
    // Busca todas las publicaciones de un subforo específico y las ordena desde la más nueva a la más antigua
    List<Publicacion> findBySubforoIdOrderByFechaCreacionDesc(Long subforoId);

    // Opcional pero útil: Cuenta cuántas publicaciones ha hecho un usuario en total
    long countByAutorId(Long autorId);
}