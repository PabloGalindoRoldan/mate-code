package com.parque_industrial.entities;

import java.time.LocalDate;

public class Lote {
    private final String DISPONIBLE = "disponible";
    private final String RESERVADO = "reservado";
    private final String VENDIDO= "vendido";
    // Las hice asi ya que eran solo tres tipos de superficie no recuerdo cuanto metros cuadrados abarcaba cada una, mas adelante los vamos a usar
    public static final int SUPERFICIE_MENOR= 1;
    public static final int SUPERFICIE_MEDIO= 1;
    public static final int SUPERFICIE_MAYOR= 1;

    private int superficie;
    private String identificacion;
    private String estado;
    private LocalDate fechaVenta;
    private double montoVenta;

    public Lote(int superficie, String identificacion)throws Exception  {
        validar(superficie, identificacion);
        this.superficie = superficie;
        this.identificacion = identificacion;
        this.estado = DISPONIBLE; // por defecto le ponemos disponible y con precio 0
        this.montoVenta = 0.0;
    }
    private void validar(double superficie, String identificacion) throws Exception  {
        if (superficie <= 0) {
            throw new Exception("La superficie debe ser un valor positivo");
        }
        if (identificacion == null || identificacion.isBlank()) {
            throw new Exception("La identificación del lote no puede estar vacía");
        }
    }
    public void reservar()throws Exception  {
        if (this.estado == DISPONIBLE) {
            this.estado = RESERVADO;
        } else {
            throw new Exception("El lote " + identificacion + " no está disponible para reservar.");
        }
    }
    public void vender(double monto)throws Exception {
        if (this.estado == RESERVADO || this.estado == DISPONIBLE) {
            if (monto <= 0) {
                throw new Exception("El monto de venta debe ser positivo.");
            }
            this.estado = VENDIDO;
            this.montoVenta = monto;
            this.fechaVenta = LocalDate.now();
        } else {
            throw new Exception("El lote " + identificacion + " no puede ser vendido en su estado actual.");
        }
    }
}
