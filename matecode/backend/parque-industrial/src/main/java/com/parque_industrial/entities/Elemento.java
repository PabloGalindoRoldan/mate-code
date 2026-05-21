package com.parque_industrial.entities;

public class Elemento {
    private Integer id;
    private String nombre;
    private CategoriaInventario categoria;
    private boolean activo;
    private RazonBajaCategoria bajaRazonCategoria;
    private String bajaObservacion;

    // Constructor vacío para que los RowMappers de JDBC armen el objeto paso a paso
    public Elemento() {
    }

    // Constructor completo para instanciar elementos recuperados con historial de
    // baja
    public Elemento(Integer id, String nombre, CategoriaInventario categoria, boolean activo,
            RazonBajaCategoria bajaRazonCategoria, String bajaObservacion) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.activo = activo;
        this.bajaRazonCategoria = bajaRazonCategoria;
        this.bajaObservacion = bajaObservacion;
    }

    // Constructor intermedio útil para dar de alta nuevos elementos del inventario
    public Elemento(String nombre, CategoriaInventario categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.activo = true; // Por defecto nace activo
        this.bajaRazonCategoria = null;
        this.bajaObservacion = null;
    }

    // Método semántico para dar de baja lógica respetando las reglas del negocio
    public void darDeBaja(RazonBajaCategoria razon, String observacion) {
        this.activo = false;
        this.bajaRazonCategoria = razon;
        this.bajaObservacion = observacion;
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

    public CategoriaInventario getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaInventario categoria) {
        this.categoria = categoria;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public RazonBajaCategoria getBajaRazonCategoria() {
        return bajaRazonCategoria;
    }

    public void setBajaRazonCategoria(RazonBajaCategoria bajaRazonCategoria) {
        this.bajaRazonCategoria = bajaRazonCategoria;
    }

    public String getBajaObservacion() {
        return bajaObservacion;
    }

    public void setBajaObservacion(String bajaObservacion) {
        this.bajaObservacion = bajaObservacion;
    }
}