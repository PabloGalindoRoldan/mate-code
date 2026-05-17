package com.parque_industrial.dto.empresa;

import com.parque_industrial.entities.Consumo;
import java.math.BigDecimal;

public class ConsumoResponseDTO {
    private Long id;
    private int mes;
    private int ano;
    private BigDecimal gas;
    private BigDecimal luz;
    private BigDecimal agua;
    private int empleados;
    private int vehiculos;
    private String fechaCarga; // La pasamos como String formateado para que React la dibuje fácil

    // Constructor rápido a partir de la Entidad
    public ConsumoResponseDTO(Consumo consumo) {
        this.id = consumo.getId();
        this.mes = consumo.getMes();
        this.ano = consumo.getAno();
        this.gas = consumo.getGas();
        this.luz = consumo.getLuz();
        this.agua = consumo.getAgua();
        this.empleados = consumo.getEmpleados();
        this.vehiculos = consumo.getVehiculos();
        this.fechaCarga = (consumo.getFechaCarga() != null) ? consumo.getFechaCarga().toString() : "";
    }

    // Getters (Jackson solo necesita los getters para serializar a JSON)
    public Long getId() {
        return id;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public BigDecimal getGas() {
        return gas;
    }

    public BigDecimal getLuz() {
        return luz;
    }

    public BigDecimal getAgua() {
        return agua;
    }

    public int getEmpleados() {
        return empleados;
    }

    public int getVehiculos() {
        return vehiculos;
    }

    public String getFechaCarga() {
        return fechaCarga;
    }
}