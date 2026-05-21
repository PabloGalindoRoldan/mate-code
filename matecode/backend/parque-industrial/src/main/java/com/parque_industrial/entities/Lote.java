package com.parque_industrial.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Date;

public class Lote {

    public static final String DISPONIBLE = "disponible";
    public static final String RESERVADO = "reservado";
    public static final String VENDIDO = "vendido";

    public static final String PARQUE_NUEVO = "nuevo";
    public static final String PARQUE_VIEJO = "viejo";

    private int identificacion;
    private String nroLote;
    private double superficie;
    private String estado;
    private LocalDate fechaVenta;
    private BigDecimal montoVenta;
    private String nc;
    private String parque;

    private String coordinates;

    public Lote(
            int identificacion,
            String nroLote,
            double superficie,
            String estado,
            LocalDate fechaVenta,
            BigDecimal montoVenta,
            String nc,
            String parque,
            String coordinates
    ) {

        validar(superficie, identificacion);
        validarParque(parque);

        this.identificacion = identificacion;
        this.nroLote = nroLote;
        this.superficie = superficie;
        this.estado = estado;
        this.fechaVenta = fechaVenta;
        this.montoVenta = montoVenta;
        this.nc = nc;
        this.parque = parque;
        this.coordinates = coordinates;
    }

    public void reservar() {

        if (!estado.equals(DISPONIBLE)) {
            throw new IllegalArgumentException(
                    "El lote no está disponible"
            );
        }

        estado = RESERVADO;
    }

    public void cancelarReserva() {

        if (!estado.equals(RESERVADO)) {
            throw new IllegalArgumentException(
                    "El lote no está reservado"
            );
        }

        estado = DISPONIBLE;
    }

    public void vender(
            BigDecimal monto,
            LocalDate fechaVenta
    ) {

        if (!estado.equals(RESERVADO)) {
            throw new IllegalArgumentException(
                    "El lote debe estar reservado"
            );
        }

        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Monto inválido"
            );
        }

        if (fechaVenta == null) {
            throw new IllegalArgumentException(
                    "Fecha inválida"
            );
        }

        estado = VENDIDO;
        montoVenta = monto;
        this.fechaVenta = fechaVenta;
    }
    private void validar(
            double superficie,
            int identificacion
    ) {

        if (superficie <= 0) {
            throw new IllegalArgumentException(
                    "Superficie inválida"
            );
        }

        if (identificacion < 0) {
            throw new IllegalArgumentException(
                    "Identificación inválida"
            );
        }
    }

    private void validarParque(String parque) {

        if (!parque.equals(PARQUE_NUEVO)
                && !parque.equals(PARQUE_VIEJO)) {

            throw new IllegalArgumentException(
                    "Parque inválido"
            );
        }
    }

    public int getIdentificacion() {
        return identificacion;
    }

    public String getNroLote() {
        return nroLote;
    }

    public double getSuperficie() {
        return superficie;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public BigDecimal getMontoVenta() {
        return montoVenta;
    }

    public String getNc() {
        return nc;
    }

    public String getParque() {
        return parque;
    }

    public String getTipo() {
        return "lote";
    }

    public Date getFechaVentaSQL() {

        return fechaVenta == null
                ? null
                : Date.valueOf(fechaVenta);
    }

    public String getCoordinates() {
        return coordinates;
    }
}