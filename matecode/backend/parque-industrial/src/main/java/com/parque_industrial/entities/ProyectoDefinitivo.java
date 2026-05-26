package com.parque_industrial.entities;



public class ProyectoDefinitivo extends Proyecto {

    private String linkViabilidadFinanciera;
    private String linkEstudioMercado;
    private String linkImpactoAmbiental;
    private String linkHabilitacionMunicipal;
    private String linkCertificadoInhibiciones;

    public ProyectoDefinitivo() {
        super();
        this.estado = PENDIENTE;
    }

    public void validarDefinitivo() throws Exception {

        // valida campos heredados del proyecto
        validar();

        if (linkViabilidadFinanciera == null
                || linkViabilidadFinanciera.isBlank()) {
            throw new Exception(
                    "El link de viabilidad financiera no puede estar vacío.");
        }

        if (linkEstudioMercado == null
                || linkEstudioMercado.isBlank()) {
            throw new Exception(
                    "El link del estudio de mercado no puede estar vacío.");
        }

        if (linkImpactoAmbiental == null
                || linkImpactoAmbiental.isBlank()) {
            throw new Exception(
                    "El link del impacto ambiental no puede estar vacío.");
        }

        if (linkHabilitacionMunicipal == null
                || linkHabilitacionMunicipal.isBlank()) {
            throw new Exception(
                    "El link de habilitación municipal no puede estar vacío.");
        }

        if (linkCertificadoInhibiciones == null
                || linkCertificadoInhibiciones.isBlank()) {
            throw new Exception(
                    "El link del certificado de inhibiciones no puede estar vacío.");
        }

        //validaciones con regex para los links
//        validarUrl(linkViabilidadFinanciera,
//                "viabilidad financiera");
//
//        validarUrl(linkEstudioMercado,
//                "estudio de mercado");
//
//        validarUrl(linkImpactoAmbiental,
//                "impacto ambiental");
//
//        validarUrl(linkHabilitacionMunicipal,
//                "habilitación municipal");
//
//        validarUrl(linkCertificadoInhibiciones,
//                "certificado de inhibiciones");
//
    }

//    private void validarUrl(String url,
//                            String documento)
//            throws Exception {
//
//        String regexUrl =
//                "^(https?:\\/\\/).+$";
//
//        if (!url.matches(regexUrl)) {
//            throw new Exception(
//                    "El link de "
//                            + documento
//                            + " no tiene un formato válido.");
//        }
//    }


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