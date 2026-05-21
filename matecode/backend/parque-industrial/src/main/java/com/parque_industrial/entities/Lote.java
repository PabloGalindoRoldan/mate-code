package com.parque_industrial.entities;

import java.time.LocalDate;
import java.sql.Date;

public class Lote {
    public static final String DISPONIBLE = "disponible";
    public static final String RESERVADO = "reservado";
    public static final String VENDIDO= "vendido";
    public static final String PARQUE_NUEVO ="nuevo";
    public static final String PARQUE_VIEJO ="viejo";

    // Las hice asi ya que eran solo tres tipos de superficie no recuerdo cuanto metros cuadrados abarcaba cada una, mas adelante los vamos a usar
    public static final int SUPERFICIE_MENOR= 1;
    public static final int SUPERFICIE_MEDIO= 1;
    public static final int SUPERFICIE_MAYOR= 1;

    private double superficie;
    private int identificacion;
    private String estado;
    private LocalDate fechaVenta;
    private Double montoVenta;
    private String nc;
    private String tipo ;
    private String parque;
    public Lote( int identificacion, double superficie, String estado, LocalDate fechaVenta, Double montoVenta, String nc,  String parque)  {
        validar(superficie, identificacion);
        validarParque(parque);
        this.superficie = superficie;
        this.identificacion = identificacion;
        this.estado = estado;
        this.montoVenta = montoVenta;
        this.fechaVenta = fechaVenta;
        this.nc = nc;
        this.tipo = "lote";
        this.parque = parque;
    }

    public Lote(int identificacion, double superficie, String nc,String parque)  {
        validar(superficie, identificacion);
        validarParque(parque);
        this.superficie = superficie;
        this.identificacion = identificacion;
        this.estado = DISPONIBLE;
        this.montoVenta = 0.0;
        this.fechaVenta = null;
        this.nc = nc;
        this.tipo = "lote";
        this.parque = parque;

    }

    public void reservar()  {
        if (!this.estado.equals(DISPONIBLE)) {
            throw new IllegalArgumentException("El lote " + identificacion + " no está disponible para reservar.");
        }
        this.estado = RESERVADO;
    }
    public void vender(double monto) {
        if (!this.estado.equals(RESERVADO)) {
            throw new IllegalArgumentException("El lote " + identificacion + " no puede ser vendido en su estado actual.");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto de venta debe ser positivo.");
        }
        this.estado = VENDIDO;
        this.montoVenta = monto;
        this.fechaVenta = LocalDate.now();
    }
    public void cancelarReserva()  {
        if(!this.estado.equals(RESERVADO)){
            throw new IllegalArgumentException("No se puede cancelar la reserva del lote");
        }
        this.estado = Lote.DISPONIBLE;
    }
    private void validar(double superficie, int identificacion){
        if (superficie <= 0) {
            throw new IllegalArgumentException("La superficie debe ser un valor positivo");
        }
        if (identificacion < 0) {
            throw new IllegalArgumentException("La identificación del lote es un numero postivo");
        }
    }
    public void validarParque(String parque)  {
        if (!parque.equals(PARQUE_NUEVO) && !parque.equals(PARQUE_VIEJO)){
            throw new IllegalArgumentException("El parque debe ser nuevo o viejo");
        }
    }
    public void marcarComoDisponible(){
        this.estado = DISPONIBLE;
    }

    public String getTipo() {
        return tipo;
    }
    public String getNc() {
        return nc;
    }
    public String getParque() {
        return parque;
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

    public void cambiarEstado(String e) {
        if(e.equals(DISPONIBLE) || e.equals(RESERVADO) || e.equals(VENDIDO)){
            this.estado = e;
        } else {
            throw new IllegalArgumentException("El estado debe ser disponible, reservado o vendido");
        }

    }
}
