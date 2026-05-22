package com.parque_industrial.dto.presupuesto;

import java.math.BigDecimal;

public class RegistroGastoRequest {
    private int presupuestoId;
    private String tipoComprobante;
    private String nroComprobante;
    private String descripcion;
    private String fase;
    private BigDecimal monto;

    // Getters y Setters
    public int getPresupuestoId() {
        return presupuestoId;
    }

    public void setPresupuestoId(int presupuestoId) {
        this.presupuestoId = presupuestoId;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getNroComprobante() {
        return nroComprobante;
    }

    public void setNroComprobante(String nroComprobante) {
        this.nroComprobante = nroComprobante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}