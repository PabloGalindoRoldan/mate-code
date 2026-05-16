package com.parque_industrial.services;

import com.parque_industrial.persistence.dtos.ConsumosDTO;

import java.util.List;

public interface ConsumosDAO {
    public void cargarConsumosDeEmpresa(ConsumosDTO consumosDTO);

    public void asignarCantVheiculos(int cuitEmoresa, int cant);
    public void asignarCantEmpleados(int cuitEmoresa, int cant);

    public void asignarConsumoGas(int cuitEmoresa, float gas);
    public void asignarConsumoLuz(int cuitEmoresa, float luz);
    public void asignarConsumoResiduos(int cuitEmoresa, int kilosResiduos);


    public List<ConsumosDTO> generarReporteConsumoTotalParque();
    public List<ConsumosDTO> generarReporteConsumoTotalEmpresa(String cuitEmpresa);
    public ConsumosDTO generarReporteConsumoEmpresa(String cuitEmpresa);
}
// Consumos:{id(PK)(AUTO), mes, año, luz, gas, agua, residuos, cant_empleados, cant_vehiculos, cuit_empresa(FK)}
