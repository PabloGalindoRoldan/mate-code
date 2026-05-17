package com.parque_industrial.dto.proyecto;

public class ProyectoPreliminarRequest {

    private String identificacion;
    private String actividadPrincipal;
    private String referente;
    private int superficieRequerida;
    private double energiaRequerida;
    private int personalAOcupar;

    public ProyectoPreliminarRequest() {
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(
            String identificacion) {

        this.identificacion = identificacion;
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
}