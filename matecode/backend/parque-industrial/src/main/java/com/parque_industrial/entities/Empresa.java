package com.parque_industrial.entities;

import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private String identificacion; // CUIT de la empresa (XX-XXXXXXXX-X)
    private String razonSocial;
    private boolean esRadicada;
    private List<Lote> lote; // Inicializada para evitar NullPointerException
    private Proyecto proyecto;

    // Constructor adaptado a las columnas reales de tu BDD
    public Empresa(String identificacion, String razonSocial, boolean esRadicada) {
        validarCuit(identificacion);
        validarRazonSocial(razonSocial);
        this.identificacion = identificacion;
        this.razonSocial = razonSocial;
        this.esRadicada = esRadicada;
        this.lote = new ArrayList<>(); // Asegura que la lista empiece vacía y no en null
    }

    private void validarRazonSocial(String razonSocial) {
        if (razonSocial == null || razonSocial.isBlank()) {
            throw new IllegalArgumentException("La razón social no puede estar vacía");
        }
    }

    private void validarCuit(String identificacion) {
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("La identificación no puede estar vacía");
        }
        String regexCuit = "^\\d{2}-\\d{8}-\\d{1}$";
        if (!identificacion.matches(regexCuit)) {
            throw new IllegalArgumentException("El formato del CUIT es inválido. Debe ser XX-XXXXXXXX-X.");
        }
    }

    public void asignarLote(Lote lote) {
        this.lote.add(lote);
    }

    public void crearProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    // Getters y Setters
    public String getIdentificacion() {
        return identificacion;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public boolean isEsRadicada() {
        return esRadicada;
    }

    public void setEsRadicada(boolean esRadicada) {
        this.esRadicada = esRadicada;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public List<Lote> getLote() {
        return lote;
    }
}