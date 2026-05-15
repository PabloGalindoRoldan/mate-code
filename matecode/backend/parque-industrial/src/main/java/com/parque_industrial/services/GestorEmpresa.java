package com.parque_industrial.services;

import com.parque_industrial.persistence.dtos.ConsumosDTO;

import java.util.List;

public class GestorEmpresa {
    private DAOEmpresa dao;
    public GestorEmpresa(DAOEmpresa dao) {
        this.dao = dao;
    }

}
