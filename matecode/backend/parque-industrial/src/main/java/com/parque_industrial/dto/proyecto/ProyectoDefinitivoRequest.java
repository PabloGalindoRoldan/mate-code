package com.parque_industrial.dto.proyecto;

import java.time.LocalDate;

public class ProyectoDefinitivoRequest {

    private String identificacion;
    private String actividadPrincipal;
    private String referente;
    private int superficieRequerida;
    private double energiaRequerida;
    private int personalAOcupar;

    private LocalDate fechaInicioObra;
    private LocalDate fechaFinObra;
    private boolean viabilidadFinanciera;
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