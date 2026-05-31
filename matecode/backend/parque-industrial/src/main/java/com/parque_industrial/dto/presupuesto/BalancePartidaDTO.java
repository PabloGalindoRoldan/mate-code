package com.parque_industrial.dto.presupuesto;

import java.math.BigDecimal;

public class BalancePartidaDTO {
    private int presupuestoId;
    private String codigo;
    private String nombre;
    private String fuenteFinanciamiento;
    private BigDecimal creditoOriginal;
    private BigDecimal creditoVigente;
    private BigDecimal comprometido;
    private BigDecimal devengado;
    private BigDecimal pagado;
    private BigDecimal saldoDisponible;

    public BalancePartidaDTO() {
    }

    public BalancePartidaDTO(int presupuestoId, String codigo, String nombre, String fuenteFinanciamiento,
            BigDecimal creditoOriginal, BigDecimal creditoVigente,
            BigDecimal comprometido, BigDecimal devengado, BigDecimal pagado,
            BigDecimal saldoDisponible) {
        this.presupuestoId = presupuestoId;
        this.codigo = codigo;
        this.nombre = nombre;
        this.fuenteFinanciamiento = fuenteFinanciamiento;
        this.creditoOriginal = creditoOriginal;
        this.creditoVigente = creditoVigente;
        this.comprometido = comprometido;
        this.devengado = devengado;
        this.pagado = pagado;
        this.saldoDisponible = saldoDisponible;
    }

    public int getPresupuestoId() {
        return presupuestoId;
    }

    public void setPresupuestoId(int presupuestoId) {
        this.presupuestoId = presupuestoId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFuenteFinanciamiento() {
        return fuenteFinanciamiento;
    }

    public void setFuenteFinanciamiento(String fuenteFinanciamiento) {
        this.fuenteFinanciamiento = fuenteFinanciamiento;
    }

    public BigDecimal getCreditoOriginal() {
        return creditoOriginal;
    }

    public void setCreditoOriginal(BigDecimal creditoOriginal) {
        this.creditoOriginal = creditoOriginal;
    }

    public BigDecimal getCreditoVigente() {
        return creditoVigente;
    }

    public void setCreditoVigente(BigDecimal creditoVigente) {
        this.creditoVigente = creditoVigente;
    }

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public BigDecimal getDevengado() {
        return devengado;
    }

    public void setDevengado(BigDecimal devengado) {
        this.devengado = devengado;
    }

    public BigDecimal getPagado() {
        return pagado;
    }

    public void setPagado(BigDecimal pagado) {
        this.pagado = pagado;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }
}