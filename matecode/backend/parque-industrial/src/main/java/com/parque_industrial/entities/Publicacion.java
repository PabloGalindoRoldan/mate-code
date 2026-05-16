package com.parque_industrial.entities;

import java.time.LocalDateTime;

public class Publicacion {
    private Long id;
    private String titulo;
    private String imagen;
    private String alt;
    private String contenido;
    private LocalDateTime fechaCreacion;

    // Constructors
    public Publicacion() {
    }

    public Publicacion(Long id, String titulo, String imagen, String alt, String contenido,
            LocalDateTime fechaCreacion) {
        this.id = id;
        this.titulo = titulo;
        this.imagen = imagen;
        this.alt = alt;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters and Setters
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}