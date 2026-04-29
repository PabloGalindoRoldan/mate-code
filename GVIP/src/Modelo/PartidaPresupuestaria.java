package Modelo;

import java.time.LocalDate;

public class PartidaPresupuestaria {
    private String nombre;
    private String descripcion;
    private double monto;
    private LocalDate fecha;

    public PartidaPresupuestaria(String nombre, String descripcion, double monto, LocalDate fecha) {
        validar(nombre, descripcion, monto, fecha);
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
    }

    private void validar(String nombre, String descripcion, double monto, LocalDate fecha) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la partida no puede estar vacío");
        }
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
    }

}
