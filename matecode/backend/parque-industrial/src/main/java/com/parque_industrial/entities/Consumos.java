package com.parque_industrial.entities;

public class Consumos {
    private int id;
    private int año;
    private int mes;
    private float luz;
    private float gas;
    private float agua;
    private int residuos;
    private int cantEmpleados;
    private int cantVehiculos;
    private int idEmpresa;

    public Consumos(float luz, float gas, float agua, int residuos, int cantEmpleados, int cantVehiculos, int idEmpresa) {
        validarLuz(luz);
        validarGas(gas);
        validarAgua(agua);
        validarResiduos(residuos);
        validarCantEmpleados(cantEmpleados);
        validarCantVehiculos(cantVehiculos);
        this.luz = luz;
        this.gas = gas;
        this.agua = agua;
        this.residuos = residuos;
        this.cantEmpleados = cantEmpleados;
        this.cantVehiculos = cantVehiculos;
        this.idEmpresa = idEmpresa;
    }

    private void validarLuz(float luz) {
        if (luz < 0 ) {
            throw new IllegalArgumentException("El consumo de luz no puede ser negativo");
        }
    }
    private void validarGas(float gas) {
        if (gas < 0 ) {
            throw new IllegalArgumentException("El consumo de gas no puede ser negativo");
        }
    }
    private  void validarAgua(float agua) {
        if (agua < 0 ) {
            throw new IllegalArgumentException("El consumo de agua no puede ser negativo");
        }
    }
    private void validarResiduos(int residuos) {
        if (residuos < 0 ) {
            throw new IllegalArgumentException("El consumo de residuos no puede ser negativo");
        }
    }
    private void validarCantEmpleados(int cantEmpleados) {
        if (cantEmpleados < 0 ) {
            throw new IllegalArgumentException("La cantidad de empleados no puede ser negativa");
        }
    }
    private void validarCantVehiculos(int cantVehiculos) {
        if (cantVehiculos < 0 ) {
            throw new IllegalArgumentException("La cantidad de vehículos no puede ser negativa");
        }
    }


    public int getId() {
        return id;
    }

    public int getAño() {
        return año;
    }

    public int getMes() {
        return mes;
    }

    public float getLuz() {
        return luz;
    }

    public float getGas() {
        return gas;
    }

    public float getAgua() {
        return agua;
    }

    public int getResiduos() {
        return residuos;
    }

    public int getCantEmpleados() {
        return cantEmpleados;
    }

    public int getCantVehiculos() {
        return cantVehiculos;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

}
