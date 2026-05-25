package com.parque_industrial.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProyectoDefinitivo {

    private Long id;
    private String usuarioNombre;
    private String nombre;
    private String descripcion;
    private String actividadPrincipal;
    private String actividadSecundaria;
    private String telefono;
    private String rubro;
    private String descripcionServicio;
    private String personaReferente;
    private String materiasPrimas;
    private String destinoProduccion;
    private Double superficieRequerida;
    private Double superficieTrabajo;
    private Double superficieDeposito;
    private Double superficieCubierta;
    private Double superficieEstacionamiento;
    private String tienePlanos;
    private String linkPlanos;
    private Double energiaRequerida;
    private Integer personalAOcupar;
    private String tensionAlimentacion;
    private Double potenciaInstalada;
    private Double aguaMensual;
    private Double gasMensual;
    private String residuosTipo;
    private Double residuosCantidad;
    private String tratamientoEfluentes;
    private String tipoEmpresa;
    private String direccion;
    private String pretensionTraslado;
    private String emplazamientoActual;
    private String tiempoRadicacion;
    private String balanzaPublica;
    private String comedor;
    private String sumCoworking;
    private String estado;
    private String cuitEmpresa;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String linkViabilidadFinanciera;
    private String linkEstudioMercado;
    private String linkImpactoAmbiental;
    private String linkHabilitacionMunicipal;
    private String CertificadoInhibiciones;

    private void validarFechas(LocalDate inicio, LocalDate fin) throws Exception {
        if (inicio == null) {
            throw new Exception("La fecha de inicio de obra no puede ser nula.");
        }
        if (fin == null) {
            throw new Exception("La fecha de fin de obra no puede ser nula.");
        }
        if (inicio.isBefore(LocalDate.now())) {
            throw new Exception("La fecha de inicio de obra no puede ser anterior a la actualidad.");
        }
        if (fin.isBefore(inicio)) {
            throw new Exception("La fecha de fin de obra no puede ser anterior a la de inicio.");
        }
    }

//    @Override
//    public void listoParaRevision() throws Exception {
//        this.estado = PENDIENTE;
//    }

    public LocalDate getFechaInicioObra() {
        return fechaInicioObra;
    }

    public LocalDate getFechaFinObra() {
        return fechaFinObra;
    }

    public String getInformeAmbiental() {
        return informeAmbiental;
    }

    public boolean isViabilidadFinanciera(){
        return viabilidadFinanciera;
    }
}
