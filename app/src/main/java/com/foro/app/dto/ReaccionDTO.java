package com.foro.app.dto;

public class ReaccionDTO {
    private Long id;
    private Long publicacionId;
    private String usuarioNickname;
    private String tipo; // "positivo", "negativo", "risa", "sorpresa"
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getPublicacionId() {
        return publicacionId;
    }
    public void setPublicacionId(Long publicacionId) {
        this.publicacionId = publicacionId;
    }
    public String getUsuarioNickname() {
        return usuarioNickname;
    }
    public void setUsuarioNickname(String usuarioNickname) {
        this.usuarioNickname = usuarioNickname;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    
}