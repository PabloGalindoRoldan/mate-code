package com.parque_industrial.entities;



public class ProyectoDefinitivo extends Proyecto {

    private String linkViabilidadFinanciera;
    private String linkEstudioMercado;
    private String linkImpactoAmbiental;
    private String linkHabilitacionMunicipal;
    private String linkCertificadoInhibiciones;

    public ProyectoDefinitivo() {
        super();
        this.estado = REVISION;
    }

    public void validarDefinitivo() throws Exception {

        // validaciones mínimas heredadas
        validar();

        // -------- CAMPOS HEREDADOS OBLIGATORIOS --------

        validarCampo(
                nombre,
                "El nombre no puede estar vacío");

        validarCampo(
                descripcion,
                "La descripción no puede estar vacía");

        validarCampo(
                actividadSecundaria,
                "La actividad secundaria no puede estar vacía");

        validarCampo(
                telefono,
                "El teléfono no puede estar vacío");

        validarCampo(
                rubro,
                "El rubro no puede estar vacío");

        validarCampo(
                descripcionServicio,
                "La descripción del servicio no puede estar vacía");

        validarCampo(
                materiasPrimas,
                "Las materias primas no pueden estar vacías");

        validarCampo(
                destinoProduccion,
                "El destino de producción no puede estar vacío");

        validarNumero(
                superficieTrabajo,
                "La superficie de trabajo debe ser mayor a cero");

        validarNumero(
                superficieDeposito,
                "La superficie de depósito debe ser mayor a cero");

        validarNumero(
                superficieCubierta,
                "La superficie cubierta debe ser mayor a cero");

        validarNumero(
                superficieEstacionamiento,
                "La superficie de estacionamiento debe ser mayor a cero");

        validarCampo(
                tensionAlimentacion,
                "La tensión de alimentación no puede estar vacía");

        validarNumero(
                potenciaInstalada,
                "La potencia instalada debe ser mayor a cero");

        validarNumero(
                aguaMensual,
                "El consumo de agua debe ser mayor a cero");

        validarNumero(
                gasMensual,
                "El consumo de gas debe ser mayor a cero");

        validarCampo(
                residuosTipo,
                "El tipo de residuos no puede estar vacío");

        validarNumero(
                residuosCantidad,
                "La cantidad de residuos debe ser mayor a cero");

        validarCampo(
                tratamientoEfluentes,
                "El tratamiento de efluentes no puede estar vacío");

        validarCampo(
                tipoEmpresa,
                "El tipo de empresa no puede estar vacío");

        validarCampo(
                direccion,
                "La dirección no puede estar vacía");

        validarCampo(
                pretensionTraslado,
                "La pretensión de traslado no puede estar vacía");

        validarCampo(
                emplazamientoActual,
                "El emplazamiento actual no puede estar vacío");

        validarCampo(
                tiempoRadicacion,
                "El tiempo de radicación no puede estar vacío");

        // -------- CAMPOS SI / NO --------

        validarBooleanTexto(
                tienePlanos,
                "tiene planos");

        validarBooleanTexto(
                balanzaPublica,
                "balanza pública");

        validarBooleanTexto(
                comedor,
                "comedor");

        validarBooleanTexto(
                sumCoworking,
                "SUM / coworking");

        // -------- LINKS --------

        validarCampo(
                linkPlanos,
                "El link de planos no puede estar vacío");

        validarCampo(
                linkViabilidadFinanciera,
                "El link de viabilidad financiera no puede estar vacío");

        validarCampo(
                linkEstudioMercado,
                "El link del estudio de mercado no puede estar vacío");

        validarCampo(
                linkImpactoAmbiental,
                "El link del impacto ambiental no puede estar vacío");

        validarCampo(
                linkHabilitacionMunicipal,
                "El link de habilitación municipal no puede estar vacío");

        validarCampo(
                linkCertificadoInhibiciones,
                "El link del certificado de inhibiciones no puede estar vacío");

        // -------- VALIDACIÓN DE URL --------

        validarUrl(
                linkPlanos,
                "planos");

        validarUrl(
                linkViabilidadFinanciera,
                "viabilidad financiera");

        validarUrl(
                linkEstudioMercado,
                "estudio de mercado");

        validarUrl(
                linkImpactoAmbiental,
                "impacto ambiental");

        validarUrl(
                linkHabilitacionMunicipal,
                "habilitación municipal");

        validarUrl(
                linkCertificadoInhibiciones,
                "certificado de inhibiciones");
    }


    public String getLinkViabilidadFinanciera() {
        return linkViabilidadFinanciera;
    }

    public void setLinkViabilidadFinanciera(String linkViabilidadFinanciera) {
        this.linkViabilidadFinanciera = linkViabilidadFinanciera;
    }

    public String getLinkEstudioMercado() {
        return linkEstudioMercado;
    }

    public void setLinkEstudioMercado(String linkEstudioMercado) {
        this.linkEstudioMercado = linkEstudioMercado;
    }

    public String getLinkImpactoAmbiental() {
        return linkImpactoAmbiental;
    }

    public void setLinkImpactoAmbiental(String linkImpactoAmbiental) {
        this.linkImpactoAmbiental = linkImpactoAmbiental;
    }

    public String getLinkHabilitacionMunicipal() {
        return linkHabilitacionMunicipal;
    }

    public void setLinkHabilitacionMunicipal(String linkHabilitacionMunicipal) {
        this.linkHabilitacionMunicipal = linkHabilitacionMunicipal;
    }

    public String getLinkCertificadoInhibiciones() {
        return linkCertificadoInhibiciones;
    }

    public void setLinkCertificadoInhibiciones(String linkCertificadoInhibiciones) {
        this.linkCertificadoInhibiciones = linkCertificadoInhibiciones;
    }
}