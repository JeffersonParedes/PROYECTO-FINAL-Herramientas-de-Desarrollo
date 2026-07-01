package com.foro.app.dto.Response;

import java.time.LocalDateTime;

public class ComentarioResponse {
    private Long id;
    private String texto;
    private String autorNickname;
    private Long autorId;
    private Long publicacionId;
    private LocalDateTime fecha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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

    public Long getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(Long publicacionId) {
        this.publicacionId = publicacionId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
