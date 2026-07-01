package com.foro.app.dto.Response;

public class SubforoResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long parentId;
    private String parentNombre;

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

    public String getParentNombre() {
        return parentNombre;
    }

    public void setParentNombre(String parentNombre) {
        this.parentNombre = parentNombre;
    }
}
