package com.parque_industrial.entities;

import java.time.LocalDate;

public class PartidaPresupuestaria {
    private String nombre;
    private String descripcion;
    private double monto;
    private LocalDate fecha;

    public PartidaPresupuestaria(String nombre, String descripcion, double monto, LocalDate fecha) throws Exception {
        validar(nombre, monto, fecha);
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
    }

    private void validar(String nombre, double monto, LocalDate fecha) throws Exception {
        if (nombre == null || nombre.isBlank()) {
            throw new Exception("El nombre de la partida no puede estar vacío");
        }
        if (monto < 0) {
            throw new Exception("El monto no puede ser negativo");
        }
        if (fecha == null) {
            throw new Exception("La fecha es obligatoria");
        }
    }

}
