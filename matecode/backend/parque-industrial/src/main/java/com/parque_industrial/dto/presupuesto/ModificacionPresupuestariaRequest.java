package com.parque_industrial.dto.presupuesto;

import java.math.BigDecimal;

public class ModificacionPresupuestariaRequest {
    private int presupuestoId;
    private String tipo;
    private BigDecimal monto;
    private String justificacion;

    // Getters y Setters
    public int getPresupuestoId() {
        return presupuestoId;
    }

    public void setPresupuestoId(int presupuestoId) {
        this.presupuestoId = presupuestoId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }
}