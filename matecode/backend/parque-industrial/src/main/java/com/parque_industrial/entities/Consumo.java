package com.parque_industrial.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Consumo {
    private Long id;
    private String cuitEmpresa;
    private int mes;
    private int ano;
    private BigDecimal gas;
    private BigDecimal luz;
    private BigDecimal agua;
    private int empleados;
    private int vehiculos;
    private LocalDateTime fechaCarga;

    public Consumo() {
    }

    public Consumo(Long id, String cuitEmpresa, int mes, int ano, BigDecimal gas, BigDecimal luz,
            BigDecimal agua, int empleados, int vehiculos, LocalDateTime fechaCarga) {
        this.id = id;
        this.cuitEmpresa = cuitEmpresa;
        this.mes = mes;
        this.ano = ano;
        this.gas = gas;
        this.luz = luz;
        this.agua = agua;
        this.empleados = empleados;
        this.vehiculos = vehiculos;
        this.fechaCarga = fechaCarga;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCuitEmpresa() {
        return cuitEmpresa;
    }

    public void setCuitEmpresa(String cuitEmpresa) {
        this.cuitEmpresa = cuitEmpresa;
    }

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

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }
}