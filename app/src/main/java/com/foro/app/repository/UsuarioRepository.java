package com.foro.app.repository;

import com.foro.app.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca un usuario por su correo electrónico (útil para el Login)
    Optional<Usuario> findByEmail(String email);

    // Busca un usuario por su nickname (útil para mostrar el perfil público)
    Optional<Usuario> findByNickname(String nickname);

    // Verifica si un correo ya está registrado (útil para validar en el Registro)
    boolean existsByEmail(String email);

    // Verifica si un nickname ya está ocupado
    boolean existsByNickname(String nickname);
} 
