package com.parque_industrial.entities;

import java.time.LocalDateTime;

public abstract class Proyecto {

    public static final String APROBADO = "aprobado";
    public static final String PENDIENTE = "pendiente";
    public static final String RECHAZADO = "rechazado";
    public static final String RECTIFICADO = "rectificado";
    public static final String BORRADOR = "borrador";

    protected Long id;
    protected String usuarioNombre;
    protected String nombre;
    protected String descripcion;
    protected String actividadPrincipal;
    protected String actividadSecundaria;
    protected String telefono;
    protected String rubro;
    protected String descripcionServicio;
    protected String personaReferente;
    protected String materiasPrimas;
    protected String destinoProduccion;
    protected Double superficieRequerida;
    protected Double superficieTrabajo;
    protected Double superficieDeposito;
    protected Double superficieCubierta;
    protected Double superficieEstacionamiento;
    protected String tienePlanos;
    protected String linkPlanos;
    protected Double energiaRequerida;
    protected Integer personalAOcupar;
    protected String tensionAlimentacion;
    protected Double potenciaInstalada;
    protected Double aguaMensual;
    protected Double gasMensual;
    protected String residuosTipo;
    protected Double residuosCantidad;
    protected String tratamientoEfluentes;
    protected String tipoEmpresa;
    protected String direccion;
    protected String pretensionTraslado;
    protected String emplazamientoActual;
    protected String tiempoRadicacion;
    protected String balanzaPublica;
    protected String comedor;
    protected String sumCoworking;
    protected String estado;
    protected String cuitEmpresa;
    protected LocalDateTime fechaCreacion;
    protected LocalDateTime fechaActualizacion;

    public Proyecto() {
        this.estado = BORRADOR;
    }

    public void aprobar() {
        if (!PENDIENTE.equals(estado)) {
            throw new IllegalStateException(
                    "Solo se puede aprobar un proyecto pendiente");
        }
        this.estado = APROBADO;
    }

    public void rechazar() {
        if (!PENDIENTE.equals(estado)) {
            throw new IllegalStateException(
                    "Solo se puede rechazar un proyecto pendiente");
        }
        this.estado = RECHAZADO;
    }

    public void rectificar() {
        if (!PENDIENTE.equals(estado)) {
            throw new IllegalStateException(
                    "Solo se puede rectificar un proyecto pendiente");
        }
        this.estado = RECTIFICADO;
    }

    public void validar() throws Exception {

        if (cuitEmpresa == null || cuitEmpresa.isBlank()) {
            throw new Exception("El CUIT no puede estar vacío");
        }

        validarCuit(cuitEmpresa);

        if (actividadPrincipal == null
                || actividadPrincipal.isBlank()) {
            throw new Exception(
                    "La actividad principal no puede estar vacía");
        }

        if (personaReferente == null
                || personaReferente.isBlank()) {
            throw new Exception(
                    "La persona referente no puede estar vacía");
        }

        if (superficieRequerida == null
                || superficieRequerida <= 0) {
            throw new Exception(
                    "La superficie requerida debe ser mayor a cero");
        }

        if (energiaRequerida == null
                || energiaRequerida <= 0) {
            throw new Exception(
                    "La energía requerida debe ser mayor a cero");
        }

        if (personalAOcupar == null
                || personalAOcupar <= 0) {
            throw new Exception(
                    "El personal a ocupar debe ser mayor a cero");
        }
    }

    private void validarCuit(String cuit) {

        String regexCuit = "^\\d{2}-\\d{8}-\\d{1}$";

        if (!cuit.matches(regexCuit)) {
            throw new IllegalArgumentException(
                    "Formato de CUIT inválido. Debe ser XX-XXXXXXXX-X");
        }
    }

    public String getCuitEmpresaAsociada() {
        return cuitEmpresa;
    }

    public String getActividadPrincipal() {
        return actividadPrincipal;
    }

    public String getReferente() {
        return personaReferente;
    }

    public Double getSuperficieRequerida() {
        return superficieRequerida;
    }


    public Double getEnergiaRequerida() {
        return energiaRequerida;
    }


    public Integer getPersonalAOcupar() {
        return personalAOcupar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
