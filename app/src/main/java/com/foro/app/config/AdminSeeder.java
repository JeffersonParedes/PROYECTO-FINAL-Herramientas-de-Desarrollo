package com.foro.app.config;

import com.foro.app.entity.Usuario;
import com.foro.app.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration

public class AdminSeeder {
    @Bean
    CommandLineRunner initAdmin(UsuarioRepository repository) {
        return args -> {
            // Verificamos si ya existe para no crear duplicados cada vez que arranques
            if (!repository.existsByEmail("admin@nexoforo.com")) {

                Usuario admin = new Usuario();
                admin.setNickname("AdminMaster");
                admin.setEmail("admin@nexoforo.com");
                admin.setPassword("123456"); // Temporal hasta implementar BCrypt
                admin.setDescripcion("Administrador principal del sistema.");
                admin.setEnlace("https://nexoforo.com");
                admin.setFechaRegistro(LocalDateTime.now());
                admin.setSuspendido(false);

                // Aquí aplicamos el rol de administrador
                admin.setRol(Usuario.Rol.admin);

                repository.save(admin);
                System.out.println("✅ Administrador inicial creado correctamente.");
            }
        };
    }
}
