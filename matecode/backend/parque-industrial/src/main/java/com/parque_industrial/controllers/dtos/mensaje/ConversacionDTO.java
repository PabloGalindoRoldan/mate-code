package com.parque_industrial.controllers.dtos.mensaje;

import java.time.LocalDateTime;

public class ConversacionDTO {
    private String contactoUsername;
    private String ultimoMensaje;
    private LocalDateTime fechaUltimoMensaje;
    private int mensajesSinLeer;

    public ConversacionDTO(String contactoUsername, String ultimoMensaje, LocalDateTime fechaUltimoMensaje,
            int mensajesSinLeer) {
        this.contactoUsername = contactoUsername;
        this.ultimoMensaje = ultimoMensaje;
        this.fechaUltimoMensaje = fechaUltimoMensaje;
        this.mensajesSinLeer = mensajesSinLeer;
    }

    // Getters y Setters
    public String getContactoUsername() {
        return contactoUsername;
    }

    public void setContactoUsername(String contactoUsername) {
        this.contactoUsername = contactoUsername;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }

    public LocalDateTime getFechaUltimoMensaje() {
        return fechaUltimoMensaje;
    }

    public void setFechaUltimoMensaje(LocalDateTime fechaUltimoMensaje) {
        this.fechaUltimoMensaje = fechaUltimoMensaje;
    }

    public int getMensajesSinLeer() {
        return mensajesSinLeer;
    }

    public void setMensajesSinLeer(int mensajesSinLeer) {
        this.mensajesSinLeer = mensajesSinLeer;
    }
}