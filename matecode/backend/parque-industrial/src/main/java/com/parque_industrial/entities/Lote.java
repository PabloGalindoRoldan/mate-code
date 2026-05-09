package com.parque_industrial.entities;

import java.time.LocalDate;

public class Lote {
    public static final String DISPONIBLE = "disponible";
    public static final String RESERVADO = "reservado";
    public static final String VENDIDO= "vendido";
    // Las hice asi ya que eran solo tres tipos de superficie no recuerdo cuanto metros cuadrados abarcaba cada una, mas adelante los vamos a usar
    public static final int SUPERFICIE_MENOR= 1;
    public static final int SUPERFICIE_MEDIO= 1;
    public static final int SUPERFICIE_MAYOR= 1;

    private double superficie;
    private int identificacion;
    private String estado;
    private LocalDate fechaVenta;
    private double montoVenta;

    public Lote( int identificacion, double superficie, String estado, LocalDate fechaVenta, double montoVenta)throws Exception  {
        validar(superficie, identificacion);
        this.superficie = superficie;
        this.identificacion = identificacion;
        this.estado = estado;
        this.montoVenta = montoVenta;
    }
    public Lote( int identificacion, double superficie)throws Exception  {
        validar(superficie, identificacion);
        this.superficie = superficie;
        this.identificacion = identificacion;
        this.estado = DISPONIBLE;
        this.montoVenta = 0;
    }

    private void validar(double superficie, int identificacion) throws Exception  {
        if (superficie <= 0) {
            throw new Exception("La superficie debe ser un valor positivo");
        }
        if (identificacion < 0) {
            throw new Exception("La identificación del lote es un numero postivo");
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
    public String getEstado() {
        return estado;
    }
    public double getSuperficie() {
        return superficie;
    }
    public int getIdentificacion() {
        return identificacion;
    }
    public LocalDate getFechaVenta() {
        return fechaVenta;
    }
    public double getMontoVenta() {
        return montoVenta;
    }

}
