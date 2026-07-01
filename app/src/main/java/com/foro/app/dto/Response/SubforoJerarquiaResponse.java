package com.foro.app.dto.Response;

import java.util.ArrayList;
import java.util.List;

public class SubforoJerarquiaResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long parentId;
    private int nivel;
    private List<SubforoJerarquiaResponse> hijos;

    public SubforoJerarquiaResponse() {
        this.hijos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public List<SubforoJerarquiaResponse> getHijos() {
        return hijos;
    }

    public void setHijos(List<SubforoJerarquiaResponse> hijos) {
        this.hijos = hijos;
    }
}
