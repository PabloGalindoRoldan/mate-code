package com.parque_industrial.services;

import com.parque_industrial.persistence.inventario.InventarioDAO;

public class Inventario {
    private final InventarioDAO dao;

    public Inventario(InventarioDAO dao) {
        this.dao = dao;
    }

    public InventarioDAO getDao() {
        return dao;
    }
}
