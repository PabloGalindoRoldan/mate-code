package com.parque_industrial.dto.empresa;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Year;

public class CargarConsumoRequest {

    @Min(value = 1, message = "El mes debe ser entre 1 y 12")
    @Max(value = 12, message = "El mes debe ser entre 1 y 12")
    private int mes;

    private int ano;

    @NotNull(message = "El consumo de gas es obligatorio")
    @Min(value = 0, message = "El consumo de gas no puede ser negativo")
    private BigDecimal gas;

    @NotNull(message = "El consumo de luz es obligatorio")
    @Min(value = 0, message = "El consumo de luz no puede ser negativo")
    private BigDecimal luz;

    @NotNull(message = "El consumo de agua es obligatorio")
    @Min(value = 0, message = "El consumo de agua no puede ser negativo")
    private BigDecimal agua;

    @Min(value = 0, message = "La cantidad de empleados no puede ser negativa")
    private int empleados;

    @Min(value = 0, message = "La cantidad de vehículos no puede ser negativa")
    private int vehiculos;

    // Constructor vacío para Jackson
    public CargarConsumoRequest() {
    }

    @AssertTrue(message = "El año debe ser el actual o hasta dos años anteriores")
    public boolean isAnoValido() {
        int anoActual = Year.now().getValue();
        return this.ano >= (anoActual - 2) && this.ano <= anoActual;
    }

    // Getters y Setters
    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public BigDecimal getGas() {
        return gas;
    }

    public void setGas(BigDecimal gas) {
        this.gas = gas;
    }

    public BigDecimal getLuz() {
        return luz;
    }

    public void setLuz(BigDecimal luz) {
        this.luz = luz;
    }

    public BigDecimal getAgua() {
        return agua;
    }

    public void setAgua(BigDecimal agua) {
        this.agua = agua;
    }

    public int getEmpleados() {
        return empleados;
    }

    public void setEmpleados(int empleados) {
        this.empleados = empleados;
    }

    public int getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(int vehiculos) {
        this.vehiculos = vehiculos;
    }
}