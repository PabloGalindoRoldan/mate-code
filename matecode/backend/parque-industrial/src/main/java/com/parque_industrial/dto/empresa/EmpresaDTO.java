package com.parque_industrial.dto.empresa;

import com.parque_industrial.entities.Empresa;

public record EmpresaDTO(String identificacion, String razonSocial, boolean esRadicada, Integer idlote) {
    public EmpresaDTO {
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("La identificación no puede estar vacía");
        }
        String regexCuit = "^\\d{2}-\\d{8}-\\d{1}$" ;
        if (!identificacion.matches(regexCuit)) {
            throw new IllegalArgumentException("El formato del CUIT es inválido. Debe ser XX-XXXXXXXX-X.");
        }
        if (razonSocial == null || razonSocial.isBlank()) {
            throw new IllegalArgumentException("La razón social no puede estar vacía");
        }
    }

    public EmpresaDTO(String identificacion, String razonSocial, boolean esRadicada) {
        this(identificacion, razonSocial, esRadicada, null);
    }

    public  Empresa entidad() {
            return new Empresa(
                    this.identificacion,
                    this.razonSocial,
                    this.esRadicada,
                    this.idlote
            );
        }

        public static EmpresaDTO dto(Empresa empresa) {
            return new EmpresaDTO(
                    empresa.getIdentificacion(),
                    empresa.getRazonSocial(),
                    empresa.isEsRadicada(),
                    empresa.getLote()
            );
        }

}
