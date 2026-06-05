package com.foro.app.repository;

import com.foro.app.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    // Método para prevenir Spam de Reportes:
    // Verifica si un usuario ya reportó un contenido específico previamente
    boolean existsByUsuarioIdAndTipoContenidoAndContenidoId(
            Long usuarioId, 
            Reporte.TipoContenido tipoContenido, 
            Long contenidoId
    );

    // Método para el Dashboard de Administradores:
    // Obtiene todos los reportes ordenados del más antiguo al más reciente para atenderlos en orden de llegada
    List<Reporte> findAllByOrderByFechaAsc();
    
}