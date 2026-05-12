package com.parque_industrial.services;

import com.parque_industrial.entities.Elemento;

import java.util.List;

public interface DAOInventario {
    public void agregarElemento(Elemento elemento);
    public void eliminarElemento(Elemento elemento);
    public List<Elemento> elementos();
    public void cambiarDisponibilidad(Elemento elemento);
}
// la interfaz es para invertir la dependencia, despues en jdbc tendriamos que implementarla
