package com.parque_industrial.dto.proyecto;

public class CambiarEstadoDTO {

    private Integer proyectoId;

    private String estado;

    public Integer getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Integer proyectoId) {
        this.proyectoId = proyectoId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
