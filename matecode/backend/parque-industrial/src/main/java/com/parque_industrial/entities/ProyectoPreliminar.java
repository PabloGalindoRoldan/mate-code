package com.parque_industrial.entities;


public class ProyectoPreliminar extends Proyecto {

    public ProyectoPreliminar() {
        super();
    }

    public void listoParaRevision() throws Exception {
        validar();
        this.estado = PENDIENTE;
    }
}