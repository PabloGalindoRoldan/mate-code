package com.parque_industrial.dto.inventario;

import com.parque_industrial.entities.CategoriaInventario;

public class ElementoRequestDTO {
    private String nombre;
    private CategoriaInventario categoria;
    private String detalle;

    public ElementoRequestDTO() {
    }

    public ElementoRequestDTO(String nombre, CategoriaInventario categoria, String detalle) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.detalle = detalle;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CategoriaInventario getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaInventario categoria) {
        this.categoria = categoria;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}