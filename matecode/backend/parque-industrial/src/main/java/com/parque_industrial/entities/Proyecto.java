package com.parque_industrial.entities;

import java.time.LocalDateTime;

public abstract class Proyecto {

    public static final String APROBADO = "aprobado";
    public static final String REVISION = "en_revision";
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
        if (!REVISION.equals(estado)) {
            throw new IllegalStateException(
                    "Solo se puede aprobar un proyecto pendiente");
        }
        this.estado = APROBADO;
    }

    public void rechazar() {
        if (!REVISION.equals(estado)) {
            throw new IllegalStateException(
                    "Solo se puede rechazar un proyecto pendiente");
        }
        this.estado = RECHAZADO;
    }

    public void rectificar() {
        if (!REVISION.equals(estado)) {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setActividadPrincipal(String actividadPrincipal) {
        this.actividadPrincipal = actividadPrincipal;
    }

    public String getActividadSecundaria() {
        return actividadSecundaria;
    }

    public void setActividadSecundaria(String actividadSecundaria) {
        this.actividadSecundaria = actividadSecundaria;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public String getPersonaReferente() {
        return personaReferente;
    }

    public void setPersonaReferente(String personaReferente) {
        this.personaReferente = personaReferente;
    }

    public String getMateriasPrimas() {
        return materiasPrimas;
    }

    public void setMateriasPrimas(String materiasPrimas) {
        this.materiasPrimas = materiasPrimas;
    }

    public String getDestinoProduccion() {
        return destinoProduccion;
    }

    public void setDestinoProduccion(String destinoProduccion) {
        this.destinoProduccion = destinoProduccion;
    }

    public void setSuperficieRequerida(Double superficieRequerida) {
        this.superficieRequerida = superficieRequerida;
    }

    public Double getSuperficieTrabajo() {
        return superficieTrabajo;
    }

    public void setSuperficieTrabajo(Double superficieTrabajo) {
        this.superficieTrabajo = superficieTrabajo;
    }

    public Double getSuperficieDeposito() {
        return superficieDeposito;
    }

    public void setSuperficieDeposito(Double superficieDeposito) {
        this.superficieDeposito = superficieDeposito;
    }

    public Double getSuperficieCubierta() {
        return superficieCubierta;
    }

    public void setSuperficieCubierta(Double superficieCubierta) {
        this.superficieCubierta = superficieCubierta;
    }

    public Double getSuperficieEstacionamiento() {
        return superficieEstacionamiento;
    }

    public void setSuperficieEstacionamiento(Double superficieEstacionamiento) {
        this.superficieEstacionamiento = superficieEstacionamiento;
    }

    public String getTienePlanos() {
        return tienePlanos;
    }

    public void setTienePlanos(String tienePlanos) {
        this.tienePlanos = tienePlanos;
    }

    public String getLinkPlanos() {
        return linkPlanos;
    }

    public void setLinkPlanos(String linkPlanos) {
        this.linkPlanos = linkPlanos;
    }

    public void setEnergiaRequerida(Double energiaRequerida) {
        this.energiaRequerida = energiaRequerida;
    }

    public void setPersonalAOcupar(Integer personalAOcupar) {
        this.personalAOcupar = personalAOcupar;
    }

    public String getTensionAlimentacion() {
        return tensionAlimentacion;
    }

    public void setTensionAlimentacion(String tensionAlimentacion) {
        this.tensionAlimentacion = tensionAlimentacion;
    }

    public Double getPotenciaInstalada() {
        return potenciaInstalada;
    }

    public void setPotenciaInstalada(Double potenciaInstalada) {
        this.potenciaInstalada = potenciaInstalada;
    }

    public Double getAguaMensual() {
        return aguaMensual;
    }

    public void setAguaMensual(Double aguaMensual) {
        this.aguaMensual = aguaMensual;
    }

    public Double getGasMensual() {
        return gasMensual;
    }

    public void setGasMensual(Double gasMensual) {
        this.gasMensual = gasMensual;
    }

    public Double getResiduosCantidad() {
        return residuosCantidad;
    }

    public void setResiduosCantidad(Double residuosCantidad) {
        this.residuosCantidad = residuosCantidad;
    }

    public String getResiduosTipo() {
        return residuosTipo;
    }

    public void setResiduosTipo(String residuosTipo) {
        this.residuosTipo = residuosTipo;
    }

    public String getTratamientoEfluentes() {
        return tratamientoEfluentes;
    }

    public void setTratamientoEfluentes(String tratamientoEfluentes) {
        this.tratamientoEfluentes = tratamientoEfluentes;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public String getPretensionTraslado() {
        return pretensionTraslado;
    }

    public void setPretensionTraslado(String pretensionTraslado) {
        this.pretensionTraslado = pretensionTraslado;
    }

    public String getEmplazamientoActual() {
        return emplazamientoActual;
    }

    public void setEmplazamientoActual(String emplazamientoActual) {
        this.emplazamientoActual = emplazamientoActual;
    }

    public String getTiempoRadicacion() {
        return tiempoRadicacion;
    }

    public void setTiempoRadicacion(String tiempoRadicacion) {
        this.tiempoRadicacion = tiempoRadicacion;
    }

    public String getBalanzaPublica() {
        return balanzaPublica;
    }

    public void setBalanzaPublica(String balanzaPublica) {
        this.balanzaPublica = balanzaPublica;
    }

    public String getComedor() {
        return comedor;
    }

    public void setComedor(String comedor) {
        this.comedor = comedor;
    }

    public String getSumCoworking() {
        return sumCoworking;
    }

    public void setSumCoworking(String sumCoworking) {
        this.sumCoworking = sumCoworking;
    }

    public String getCuitEmpresa() {
        return cuitEmpresa;
    }

    public void setCuitEmpresa(String cuitEmpresa) {
        this.cuitEmpresa = cuitEmpresa;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
