package com.parque_industrial.persistence.dtos;

public record ConsumosDTO(int id,
                          int año,
                          int mes,
                          float luz,
                          float gas,
                          float agua,
                          int residuos,
                          int cantEmpleados,
                          int cantVehiculos,
                          int idEmpresa) {
        public ConsumosDTO(float luz, float gas, float agua, int residuos, int cantEmpleados, int cantVehiculos, int idEmpresa) {
            //le pongo cero xq la bd le asigna el id automaticamente y el año y mes se asignan al momento de cargar el consumo
            this(0, 0, 0, luz, gas, agua, residuos, cantEmpleados, cantVehiculos, idEmpresa);
        }

       public  ConsumosDTO{
           if (cantVehiculos < 0 ) {
               throw new IllegalArgumentException("La cantidad de vehículos no puede ser negativa");
           }
           if (cantEmpleados < 0 ) {
               throw new IllegalArgumentException("La cantidad de empleados no puede ser negativa");
           }
           if (residuos < 0 ) {
               throw new IllegalArgumentException("El consumo de residuos no puede ser negativo");
           }
           if (agua < 0 ) {
               throw new IllegalArgumentException("El consumo de agua no puede ser negativo");
           }
           if (gas < 0 ) {
               throw new IllegalArgumentException("El consumo de gas no puede ser negativo");
           }
           if (luz < 0 ) {
               throw new IllegalArgumentException("El consumo de luz no puede ser negativo");
           }
        }
}
//Consumos:{id(PK)(AUTO), mes, año, luz, gas, agua, residuos, cant_empleados, cant_vehiculos, cuit_empresa(FK)}