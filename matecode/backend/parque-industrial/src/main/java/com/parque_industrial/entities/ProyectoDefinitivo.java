package com.parque_industrial.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProyectoDefinitivo extends Proyecto {

    private String linkViabilidadFinanciera;
    private String linkEstudioMercado;
    private String linkImpactoAmbiental;
    private String linkHabilitacionMunicipal;
    private String certificadoInhibiciones;

    public ProyectoDefinitivo() {
        super();
        this.estado = PENDIENTE;
    }

    private void validarFechas(LocalDate inicio,
                               LocalDate fin) throws Exception {

        if (inicio == null) {
            throw new Exception(
                    "La fecha de inicio de obra no puede ser nula.");
        }

        if (fin == null) {
            throw new Exception(
                    "La fecha de fin de obra no puede ser nula.");
        }

        if (inicio.isBefore(LocalDate.now())) {
            throw new Exception(
                    "La fecha de inicio de obra no puede ser anterior a la actualidad.");
        }

        if (fin.isBefore(inicio)) {
            throw new Exception(
                    "La fecha de fin de obra no puede ser anterior a la de inicio.");
        }
    }
}