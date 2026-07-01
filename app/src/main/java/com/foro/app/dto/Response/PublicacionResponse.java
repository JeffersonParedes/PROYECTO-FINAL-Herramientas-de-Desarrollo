package com.foro.app.dto.Response;

import java.time.LocalDateTime;

public class PublicacionResponse {
    private Long id;
    private String titulo;
    private String descripcion;
    private String contenido;
    private String imagen;
    private String video;
    private String audio;
    private String autorNickname;
    private Long autorId;
    private Long subforoId;
    private String subforoNombre;
    private Integer puntuacion;
    private LocalDateTime fechaCreacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAutorNickname() {
        return autorNickname;
    }

    public void setAutorNickname(String autorNickname) {
        this.autorNickname = autorNickname;
    }

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    public Long getSubforoId() {
        return subforoId;
    }

    public void setSubforoId(Long subforoId) {
        this.subforoId = subforoId;
    }

    public String getSubforoNombre() {
        return subforoNombre;
    }

    public void setSubforoNombre(String subforoNombre) {
        this.subforoNombre = subforoNombre;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
