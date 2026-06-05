package com.foro.app.dto;

import java.util.ArrayList;
import java.util.List;

public class SubforoJerarquiaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long parentId;
    private int nivel;
    private List<SubforoJerarquiaDTO> hijos;

    public SubforoJerarquiaDTO() {
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

    public List<SubforoJerarquiaDTO> getHijos() {
        return hijos;
    }

    public void setHijos(List<SubforoJerarquiaDTO> hijos) {
        this.hijos = hijos;
    }
}
