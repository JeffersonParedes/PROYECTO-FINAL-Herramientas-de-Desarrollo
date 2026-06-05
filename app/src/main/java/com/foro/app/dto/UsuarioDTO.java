package com.foro.app.dto;

public class UsuarioDTO {
    private Long id;
    private String nickname;
    private String email;
    private String descripcion;
    private String enlace;
    private String rol;
    private boolean suspendido;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getEnlace() {
        return enlace;
    }
    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public boolean isSuspendido() {
        return suspendido;
    }
    public void setSuspendido(boolean suspendido) {
        this.suspendido = suspendido;
    }

    
    

}