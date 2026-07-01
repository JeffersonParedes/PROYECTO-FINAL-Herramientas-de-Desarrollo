package com.foro.app.dto.Request;

import org.springframework.web.multipart.MultipartFile;

public class PublicacionCreateRequest {
    private String titulo;
    private String descripcion;
    private String contenido;
    private Long subforoId;
    private MultipartFile imagenFile;
    private MultipartFile videoFile;
    private MultipartFile audioFile;

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

    public Long getSubforoId() {
        return subforoId;
    }

    public void setSubforoId(Long subforoId) {
        this.subforoId = subforoId;
    }

    public MultipartFile getImagenFile() {
        return imagenFile;
    }

    public void setImagenFile(MultipartFile imagenFile) {
        this.imagenFile = imagenFile;
    }

    public MultipartFile getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(MultipartFile videoFile) {
        this.videoFile = videoFile;
    }

    public MultipartFile getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(MultipartFile audioFile) {
        this.audioFile = audioFile;
    }
}
