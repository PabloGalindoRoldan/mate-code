package com.parque_industrial.entities;

import java.time.LocalDateTime;

public class Mensaje {
    private Long id;
    private String emisorUsername;
    private String receptorUsername;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private boolean leido;

    public Mensaje() {
    }

    public Mensaje(String emisorUsername, String receptorUsername, String contenido, LocalDateTime fechaEnvio,
            boolean leido) {
        this.emisorUsername = emisorUsername;
        this.receptorUsername = receptorUsername;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
        this.leido = leido;
    }

    public Mensaje(Long id, String emisorUsername, String receptorUsername, String contenido, LocalDateTime fechaEnvio,
            boolean leido) {
        this.id = id;
        this.emisorUsername = emisorUsername;
        this.receptorUsername = receptorUsername;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
        this.leido = leido;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmisorUsername() {
        return emisorUsername;
    }

    public void setEmisorUsername(String emisorUsername) {
        this.emisorUsername = emisorUsername;
    }

    public String getReceptorUsername() {
        return receptorUsername;
    }

    public void setReceptorUsername(String receptorUsername) {
        this.receptorUsername = receptorUsername;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }
}