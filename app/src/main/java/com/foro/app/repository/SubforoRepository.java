package com.foro.app.repository;

import com.foro.app.entity.Subforo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubforoRepository extends JpaRepository<Subforo, Long> {

    // Verifica si ya existe un subforo con ese nombre (Regla que definimos en el Service)
    boolean existsByNombreIgnoreCase(String nombre);

    // Método CLAVE para obtenerJerarquiaCompleta(): 
    // Busca todos los foros principales (los que NO tienen padre, es decir, parent_id es NULL)
    List<Subforo> findByParentIsNull();

    List<Subforo> findByParentId(Long parentId);
}
