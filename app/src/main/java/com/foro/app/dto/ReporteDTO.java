package com.foro.app.dto;

import java.time.LocalDateTime;

public class ReporteDTO {
    private Long id;
    private String usuarioNickname;
    private String tipoContenido; // "publicacion" o "comentario"
    private Long contenidoId;
    private String motivo;
    private LocalDateTime fecha;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsuarioNickname() {
        return usuarioNickname;
    }
    public void setUsuarioNickname(String usuarioNickname) {
        this.usuarioNickname = usuarioNickname;
    }
    public String getTipoContenido() {
        return tipoContenido;
    }
    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = tipoContenido;
    }
    public Long getContenidoId() {
        return contenidoId;
    }
    public void setContenidoId(Long contenidoId) {
        this.contenidoId = contenidoId;
    }
    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }


}