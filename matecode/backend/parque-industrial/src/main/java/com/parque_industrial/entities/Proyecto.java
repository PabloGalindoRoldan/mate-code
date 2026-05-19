package com.parque_industrial.entities;

public abstract class Proyecto {
   public static final String APROBADO = "aprobado";
   public static final String PENDIENTE = "pendiente";
   public static final String RECHAZADO = "rechazado";
    public static final String RECTIFICADO = "rectificado";
    public static final String BORRADOR = "borrador";
    protected String cuitEmpresaAsociada;// seria la misma que de la empresa, el cuit
    protected String actividadPrincipal;
    protected String referente;
    protected int superficieRequerida;
    protected double energiaRequerida;
    protected int personalAOcupar;
    protected String estado;

    public Proyecto(String cuitEmpresaAsociada, String actividadPrincipal, String referente, int superficieRequerida, double energiaRequerida, int personalAOcupar) {
        validarCuit(cuitEmpresaAsociada);
        this.cuitEmpresaAsociada = cuitEmpresaAsociada;
        this.actividadPrincipal = actividadPrincipal;
        this.referente = referente;
        this.superficieRequerida = superficieRequerida;
        this.energiaRequerida = energiaRequerida;
        this.personalAOcupar = personalAOcupar;
        this.estado = BORRADOR; // por defecto una vez q se crea queda en borrador
    }

    public abstract void listoParaRevision() throws Exception;
    public void aprobar(){
        if (!estado.equals(PENDIENTE)) {
            throw new IllegalStateException("Solo se puede aprobar un proyecto pendiente");
        }
        this.estado = APROBADO;
    }
    public void rechazar(){
        if (!estado.equals(PENDIENTE)) {
            throw new IllegalStateException("Solo se puede rechazar un proyecto pendiente");
        }
        this.estado = RECHAZADO;
    }
    public void rectificar(){
        if (!estado.equals(PENDIENTE)) {
            throw new IllegalStateException("Solo se puede rectificar un proyecto pendiente");
        }

        this.estado = RECTIFICADO;
    }
    // quien deba aprobar rechazar un proyecto debe ser un admin

    public void validar() throws Exception{
        if (cuitEmpresaAsociada == null || cuitEmpresaAsociada.isBlank()) {
            throw new Exception("La identificación del proyecto no puede estar vacía");
        }
        if (actividadPrincipal == null || actividadPrincipal.isBlank()) {
            throw new Exception("La actividad principal no puede estar vacía");
        }
        if (referente == null || referente.isBlank()) {
            throw new Exception("El referente no puede estar vacío");
        }
        if (superficieRequerida <= 0) {
            throw new Exception("La superficie requerida debe ser mayor que cero");
        }
        if (energiaRequerida <= 0) {
            throw new Exception("La energía requerida debe ser mayor que cero");
        }
        if (personalAOcupar <= 0) {
            throw new Exception("El personal a ocupar debe ser mayor que cero");
        }
    }

    private void validarCuit(String cuit) {
        if (cuit == null || cuit.isBlank()) {
            throw new IllegalArgumentException("El CUIT no puede estar vacío.");
        }
        // formato debe ser XX-XXXXXXXX-X
        // Esto valida que sean 2 dígitos, un guion, 8 dígitos, un guion y 1 dígito.
        String regexCuit = "^\\d{2}-\\d{8}-\\d{1}$";
        if (!cuit.matches(regexCuit)) {
            throw new IllegalArgumentException("El formato del CUIT es inválido. Debe ser XX-XXXXXXXX-X.");
        }
    }

    public String getCuitEmpresaAsociada() {
        return cuitEmpresaAsociada;
    }

    public String getActividadPrincipal() {
        return actividadPrincipal;
    }

    public String getReferente() {
        return referente;
    }

    public int getSuperficieRequerida() {
        return superficieRequerida;
    }


    public double getEnergiaRequerida() {
        return energiaRequerida;
    }


    public int getPersonalAOcupar() {
        return personalAOcupar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
