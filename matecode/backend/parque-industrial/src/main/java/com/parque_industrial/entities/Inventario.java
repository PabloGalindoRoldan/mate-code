package com.parque_industrial.entities;

import java.util.List;

public interface Inventario {
    public void agregarElemento(Elemento elemento);
    public void eliminarElemento(Elemento elemento);
    public List<Elemento> elementos();
    public void cambiarDisponibilidad(Elemento elemento);
}
