package com.parque_industrial.dto.inventario;

import com.parque_industrial.entities.CategoriaInventario;

public class ElementoRequestDTO {
    private String nombre;
    private CategoriaInventario categoria;

    public ElementoRequestDTO() {
    }

    public ElementoRequestDTO(String nombre, CategoriaInventario categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
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
}