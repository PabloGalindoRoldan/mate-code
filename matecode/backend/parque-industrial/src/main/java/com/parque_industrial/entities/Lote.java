package com.parque_industrial.entities;

import java.time.LocalDate;
import java.sql.Date;

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
    private Double montoVenta;

    public Lote( int identificacion, double superficie, String estado, LocalDate fechaVenta, Double montoVenta)throws Exception  {
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
        this.montoVenta = 0.0;
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
        if (this.estado != DISPONIBLE) {
            throw new Exception("El lote " + identificacion + " no está disponible para reservar.");
        }
        this.estado = RESERVADO;

    }
    public void vender(double monto)throws Exception {
        if (this.estado != RESERVADO) {
            throw new Exception("El lote " + identificacion + " no puede ser vendido en su estado actual.");
        }
        if (monto <= 0) {
            throw new Exception("El monto de venta debe ser positivo.");
        }
        this.estado = VENDIDO;
        this.montoVenta = monto;
        this.fechaVenta = LocalDate.now();
    }
    public void cancelarReserva() throws Exception {
        if(this.estado != Lote.RESERVADO){
            throw new Exception("No se puede cancelar la reserva del lote");
        }
        this.estado = Lote.DISPONIBLE;
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
    public Date FechaVentaSQL() {

        return (this.fechaVenta == null) ? null : new Date(this.fechaVenta.getYear(), this.fechaVenta.getMonthValue(), this.fechaVenta.getDayOfMonth());
    }
    public LocalDate getFechaVenta(){
        return this.fechaVenta;
    }
    public double getMontoVenta() {
        return montoVenta;
    }

}
