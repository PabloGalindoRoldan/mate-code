package com.parque_industrial.dto.proyecto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class ProyectoDefinitivoRequest {

    @NotBlank(message = "La identificación es obligatoria")
    private String identificacion;

    @NotBlank(message = "La actividad principal es obligatoria")
    private String actividadPrincipal;

    @NotBlank(message = "El referente es obligatorio")
    private String referente;

    @Positive(message = "La superficie requerida debe ser mayor a cero")
    private int superficieRequerida;

    @Positive(message = "La energía requerida debe ser mayor a cero")
    private double energiaRequerida;

    @Positive(message = "El personal a ocupar debe ser mayor a cero")
    private int personalAOcupar;

    @NotNull(message = "La fecha de inicio de obra es obligatoria")
    private LocalDate fechaInicioObra;

    @NotNull(message = "La fecha de fin de obra es obligatoria")
    private LocalDate fechaFinObra;

    private boolean viabilidadFinanciera;

    @NotBlank(message = "El informe ambiental es obligatorio")
    private String informeAmbiental;

    public ProyectoDefinitivoRequest() {
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(
            String identificacion) {

        this.identificacion =
                identificacion;
    }

    public String getActividadPrincipal() {
        return actividadPrincipal;
    }

    public void setActividadPrincipal(
            String actividadPrincipal) {

        this.actividadPrincipal =
                actividadPrincipal;
    }

    public String getReferente() {
        return referente;
    }

    public void setReferente(
            String referente) {

        this.referente = referente;
    }

    public int getSuperficieRequerida() {
        return superficieRequerida;
    }

    public void setSuperficieRequerida(
            int superficieRequerida) {

        this.superficieRequerida =
                superficieRequerida;
    }

    public double getEnergiaRequerida() {
        return energiaRequerida;
    }

    public void setEnergiaRequerida(
            double energiaRequerida) {

        this.energiaRequerida =
                energiaRequerida;
    }

    public int getPersonalAOcupar() {
        return personalAOcupar;
    }

    public void setPersonalAOcupar(
            int personalAOcupar) {

        this.personalAOcupar =
                personalAOcupar;
    }

    public LocalDate getFechaInicioObra() {
        return fechaInicioObra;
    }

    public void setFechaInicioObra(
            LocalDate fechaInicioObra) {

        this.fechaInicioObra =
                fechaInicioObra;
    }

    public LocalDate getFechaFinObra() {
        return fechaFinObra;
    }

    public void setFechaFinObra(
            LocalDate fechaFinObra) {

        this.fechaFinObra =
                fechaFinObra;
    }

    public boolean isViabilidadFinanciera() {
        return viabilidadFinanciera;
    }

    public void setViabilidadFinanciera(
            boolean viabilidadFinanciera) {

        this.viabilidadFinanciera =
                viabilidadFinanciera;
    }

    public String getInformeAmbiental() {
        return informeAmbiental;
    }

    public void setInformeAmbiental(
            String informeAmbiental) {

        this.informeAmbiental =
                informeAmbiental;
    }
}