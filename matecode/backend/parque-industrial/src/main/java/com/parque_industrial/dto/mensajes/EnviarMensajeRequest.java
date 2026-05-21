package com.parque_industrial.dto.mensajes;

public class EnviarMensajeRequest {
    private String receptorUsername;
    private String contenido;

    public EnviarMensajeRequest() {
    }

    public EnviarMensajeRequest(String receptorUsername, String contenido) {
        this.receptorUsername = receptorUsername;
        this.contenido = contenido;
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
}