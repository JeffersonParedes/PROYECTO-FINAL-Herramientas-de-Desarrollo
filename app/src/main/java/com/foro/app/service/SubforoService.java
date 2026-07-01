package com.foro.app.service;

import com.foro.app.dto.Request.SubforoCreateRequest;
import com.foro.app.dto.Response.SubforoJerarquiaResponse;
import com.foro.app.dto.Response.SubforoResponse;
import com.foro.app.entity.Subforo;
import com.foro.app.entity.Usuario;
import com.foro.app.exceptions.BadRequestException;
import com.foro.app.exceptions.ResourceNotFoundException;
import com.foro.app.exceptions.UnauthorizedException;
import com.foro.app.mappers.SubforoMapper;
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
    private final SubforoMapper subforoMapper;

    public SubforoService(SubforoRepository subforoRepository,
            UsuarioRepository usuarioRepository,
            SubforoMapper subforoMapper) {
        this.subforoRepository = subforoRepository;
        this.usuarioRepository = usuarioRepository;
        this.subforoMapper = subforoMapper;
    }

    @Transactional
    public SubforoResponse crearSubforo(SubforoCreateRequest request, Long ejecutorId) {
        Usuario ejecutor = usuarioRepository.findById(ejecutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario ejecutor no encontrado."));

        if (ejecutor.getRol() != Usuario.Rol.admin) {
            throw new UnauthorizedException("Acceso denegado. Se requieren permisos de administrador.");
        }

        if (subforoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new BadRequestException("Ya existe un foro con el nombre '" + request.getNombre() + "'.");
        }

        Subforo subforo = subforoMapper.toEntity(request);

        if (request.getParentId() != null) {
            Subforo padre = subforoRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("El foro padre especificado no existe."));

            int nivelPadre = calcularNivel(padre);
            if (nivelPadre >= 3) {
                throw new BadRequestException(
                        "No se pueden crear subforos en niveles más profundos (límite de 3 niveles).");
            }

            subforo.setParent(padre);
        }

        subforo = subforoRepository.save(subforo);
        return subforoMapper.toResponse(subforo);
    }

    @Transactional(readOnly = true)
    public List<SubforoJerarquiaResponse> obtenerJerarquiaCompleta() {
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

        List<SubforoJerarquiaResponse> resultado = new ArrayList<>();
        for (Subforo raiz : raices) {
            resultado.add(construirNodo(raiz, 1, hijosPorParentId, mapaPorId));
        }

        return resultado;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerDetalleSubforo(Long subforoId) {
        Subforo subforo = subforoRepository.findById(subforoId)
                .orElseThrow(() -> new ResourceNotFoundException("Subforo no encontrado."));

        SubforoResponse response = subforoMapper.toResponse(subforo);

        List<SubforoResponse> breadcrumbs = new ArrayList<>();
        Subforo actual = subforo;
        while (actual != null) {
            breadcrumbs.add(0, subforoMapper.toResponse(actual));
            actual = actual.getParent();
        }

        Map<String, Object> detalle = new HashMap<>();
        detalle.put("subforo", response);
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

    private SubforoJerarquiaResponse construirNodo(Subforo subforo, int nivel,
            Map<Long, List<Subforo>> hijosPorParentId,
            Map<Long, Subforo> mapaPorId) {
        SubforoJerarquiaResponse nodo = subforoMapper.toJerarquiaResponse(subforo, nivel);

        List<Subforo> hijos = hijosPorParentId.getOrDefault(subforo.getId(), Collections.emptyList());
        for (Subforo hijo : hijos) {
            nodo.getHijos().add(construirNodo(hijo, nivel + 1, hijosPorParentId, mapaPorId));
        }

        return nodo;
    }

    @Transactional(readOnly = true)
    public List<SubforoResponse> obtenerTodosSubforos() {
        return subforoRepository.findAll().stream()
                .map(subforoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubforoResponse> obtenerSubforosPrincipales() {
        return subforoRepository.findByParentIsNull().stream()
                .map(subforoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
