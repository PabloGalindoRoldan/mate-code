package com.parque_industrial.dto.proyecto;

public class CrearRequestDTO {

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

    // Constructor vacío
    public CrearRequestDTO() {
    }

    // Getters y Setters (necesarios para que el framework de serialización JSON los
    // mapee)
    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActividadPrincipal() {
        return actividadPrincipal;
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

    public Double getSuperficieRequerida() {
        return superficieRequerida;
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

    public Double getEnergiaRequerida() {
        return energiaRequerida;
    }

    public void setEnergiaRequerida(Double energiaRequerida) {
        this.energiaRequerida = energiaRequerida;
    }

    public Integer getPersonalAOcupar() {
        return personalAOcupar;
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

    public String getResiduosTipo() {
        return residuosTipo;
    }

    public void setResiduosTipo(String residuosTipo) {
        this.residuosTipo = residuosTipo;
    }

    public Double getResiduosCantidad() {
        return residuosCantidad;
    }

    public void setResiduosCantidad(Double residuosCantidad) {
        this.residuosCantidad = residuosCantidad;
    }

    public String getTratamientoEfluentes() {
        return tratamientoEfluentes;
    }

    public void setTratamientoEfluentes(String tratamientoEfluentes) {
        this.tratamientoEfluentes = tratamientoEfluentes;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}