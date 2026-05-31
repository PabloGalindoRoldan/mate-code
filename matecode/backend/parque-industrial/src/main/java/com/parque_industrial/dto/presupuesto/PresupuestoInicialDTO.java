package com.parque_industrial.dto.presupuesto;

public class PresupuestoInicialDTO {
    private Integer partidaId;
    private Double monto;
    private String fuenteFinanciamiento;
    private Integer ejercicioFiscal;

    public PresupuestoInicialDTO() {
    }

    public PresupuestoInicialDTO(Integer partidaId, Double monto, String fuenteFinanciamiento,
            Integer ejercicioFiscal) {
        this.partidaId = partidaId;
        this.monto = monto;
        this.fuenteFinanciamiento = fuenteFinanciamiento;
        this.ejercicioFiscal = ejercicioFiscal;
    }

    // Getters y Setters
    public Integer getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(Integer partidaId) {
        this.partidaId = partidaId;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getFuenteFinanciamiento() {
        return fuenteFinanciamiento;
    }

    public void setFuenteFinanciamiento(String fuenteFinanciamiento) {
        this.fuenteFinanciamiento = fuenteFinanciamiento;
    }

    public Integer getEjercicioFiscal() {
        return ejercicioFiscal;
    }

    public void setEjercicioFiscal(Integer ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }
}