package com.parque_industrial.dto.inventario;

import com.parque_industrial.entities.Elemento;

public class ElementoResponseDTO {
    private Integer id;
    private String nombre;
    private String categoria;
    private String detalle;
    private boolean activo;
    private String bajaRazonCategoria;
    private String bajaObservacion;

    public ElementoResponseDTO() {
    }

    // Constructor de mapeo directo para simplificar la conversión en la capa de
    // servicio
    public ElementoResponseDTO(Elemento elemento) {
        this.id = elemento.getId();
        this.nombre = elemento.getNombre();
        this.categoria = elemento.getCategoria().name();
        this.detalle = elemento.getDetalle();
        this.activo = elemento.isActivo();
        this.bajaRazonCategoria = (elemento.getBajaRazonCategoria() != null) ? elemento.getBajaRazonCategoria().name()
                : null;
        this.bajaObservacion = elemento.getBajaObservacion();
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getBajaRazonCategoria() {
        return bajaRazonCategoria;
    }

    public void setBajaRazonCategoria(String bajaRazonCategoria) {
        this.bajaRazonCategoria = bajaRazonCategoria;
    }

    public String getBajaObservacion() {
        return bajaObservacion;
    }

    public void setBajaObservacion(String bajaObservacion) {
        this.bajaObservacion = bajaObservacion;
    }
}