package com.foro.app.service;

import com.foro.app.dto.SubforoDTO;
import com.foro.app.dto.SubforoJerarquiaDTO;
import com.foro.app.entity.Subforo;
import com.foro.app.entity.Usuario;
import com.foro.app.repository.SubforoRepository;
import com.foro.app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubforoService {

    private final SubforoRepository subforoRepository;
    private final UsuarioRepository usuarioRepository;

    public SubforoService(SubforoRepository subforoRepository,
                          UsuarioRepository usuarioRepository) {
        this.subforoRepository = subforoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public SubforoDTO crearSubforo(String nombre, String descripcion, Long parentId, Long ejecutorId) {
        Usuario ejecutor = usuarioRepository.findById(ejecutorId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (ejecutor.getRol() != Usuario.Rol.admin) {
            throw new IllegalArgumentException("No tienes permisos de administrador");
        }

        if (subforoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("Ya existe un foro con ese nombre");
        }

        Subforo subforo = new Subforo();
        subforo.setNombre(nombre);
        subforo.setDescripcion(descripcion);

        if (parentId != null) {
            Subforo padre = subforoRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("El foro padre no existe"));

            int nivelPadre = calcularNivel(padre);
            if (nivelPadre >= 3) {
                throw new IllegalArgumentException("No se pueden crear más subniveles");
            }

            subforo.setParent(padre);
        }

        subforo = subforoRepository.save(subforo);

        SubforoDTO dto = new SubforoDTO();
        dto.setId(subforo.getId());
        dto.setNombre(subforo.getNombre());
        dto.setDescripcion(subforo.getDescripcion());
        dto.setParentId(subforo.getParent() != null ? subforo.getParent().getId() : null);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<SubforoJerarquiaDTO> obtenerJerarquiaCompleta() {
        List<Subforo> todos = subforoRepository.findAll();

        Map<Long, Subforo> mapaPorId = todos.stream()
                .collect(Collectors.toMap(Subforo::getId, s -> s));

        Map<Long, List<Subforo>> hijosPorParentId = new HashMap<>();
        for (Subforo s : todos) {
            if (s.getParent() != null) {
                hijosPorParentId
                        .computeIfAbsent(s.getParent().getId(), k -> new ArrayList<>())
                        .add(s);
            }
        }

        List<Subforo> raices = todos.stream()
                .filter(s -> s.getParent() == null)
                .collect(Collectors.toList());

        List<SubforoJerarquiaDTO> resultado = new ArrayList<>();
        for (Subforo raiz : raices) {
            resultado.add(construirNodo(raiz, 1, hijosPorParentId, mapaPorId));
        }

        return resultado;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerDetalleSubforo(Long subforoId) {
        Subforo subforo = subforoRepository.findById(subforoId)
                .orElseThrow(() -> new IllegalArgumentException("Subforo no encontrado"));

        SubforoDTO dto = new SubforoDTO();
        dto.setId(subforo.getId());
        dto.setNombre(subforo.getNombre());
        dto.setDescripcion(subforo.getDescripcion());
        dto.setParentId(subforo.getParent() != null ? subforo.getParent().getId() : null);

        List<SubforoDTO> breadcrumbs = new ArrayList<>();
        Subforo actual = subforo;
        while (actual != null) {
            SubforoDTO crumb = new SubforoDTO();
            crumb.setId(actual.getId());
            crumb.setNombre(actual.getNombre());
            crumb.setDescripcion(actual.getDescripcion());
            crumb.setParentId(actual.getParent() != null ? actual.getParent().getId() : null);
            breadcrumbs.add(0, crumb);
            actual = actual.getParent();
        }

        Map<String, Object> detalle = new HashMap<>();
        detalle.put("subforo", dto);
        detalle.put("breadcrumbs", breadcrumbs);
        return detalle;
    }

    private int calcularNivel(Subforo subforo) {
        int nivel = 1;
        Subforo actual = subforo;
        while (actual.getParent() != null) {
            nivel++;
            actual = actual.getParent();
        }
        return nivel;
    }

    private SubforoJerarquiaDTO construirNodo(Subforo subforo, int nivel,
                                              Map<Long, List<Subforo>> hijosPorParentId,
                                              Map<Long, Subforo> mapaPorId) {
        SubforoJerarquiaDTO nodo = new SubforoJerarquiaDTO();
        nodo.setId(subforo.getId());
        nodo.setNombre(subforo.getNombre());
        nodo.setDescripcion(subforo.getDescripcion());
        nodo.setParentId(subforo.getParent() != null ? subforo.getParent().getId() : null);
        nodo.setNivel(nivel);

        List<Subforo> hijos = hijosPorParentId.getOrDefault(subforo.getId(), Collections.emptyList());
        for (Subforo hijo : hijos) {
            nodo.getHijos().add(construirNodo(hijo, nivel + 1, hijosPorParentId, mapaPorId));
        }

        return nodo;
    }

    @Transactional(readOnly = true)
    public List<SubforoDTO> obtenerTodosSubforos() {
        return subforoRepository.findAll().stream().map(s -> {
            SubforoDTO dto = new SubforoDTO();
            dto.setId(s.getId());
            dto.setNombre(s.getNombre());
            dto.setDescripcion(s.getDescripcion());
            dto.setParentId(s.getParent() != null ? s.getParent().getId() : null);
            return dto;
        }).collect(Collectors.toList());
    }
}
