package com.parque_industrial.dto.inventario;

import com.parque_industrial.entities.RazonBajaCategoria;

public class BajaElementoRequestDTO {
    private RazonBajaCategoria razon;
    private String observacion;

    public BajaElementoRequestDTO() {
    }

    public BajaElementoRequestDTO(RazonBajaCategoria razon, String observacion) {
        this.razon = razon;
        this.observacion = observacion;
    }

    public RazonBajaCategoria getRazon() {
        return razon;
    }

    public void setRazon(RazonBajaCategoria razon) {
        this.razon = razon;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}