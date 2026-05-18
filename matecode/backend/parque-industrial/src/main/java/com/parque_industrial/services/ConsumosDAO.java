package com.parque_industrial.services;

import com.parque_industrial.persistence.dtos.ConsumosDTO;

import java.util.List;

public interface ConsumosDAO {
    public void cargarConsumosDeEmpresa(ConsumosDTO consumosDTO);

    public void asignarCantVheiculos(long cuitEmoresa, int cant);
    public void asignarCantEmpleados(long cuitEmoresa, int cant);

    public void asignarConsumoGas(long cuitEmoresa, float gas);
    public void asignarConsumoLuz(long cuitEmoresa, float luz);
    public void asignarConsumoAgua(long cuitEmoresa, float agua);
    public void asignarConsumoResiduos(long cuitEmoresa, int kilosResiduos);


    public List<ConsumosDTO> generarReporteConsumoTotalParque();
    public List<ConsumosDTO> generarReporteConsumoTotalEmpresa(long cuitEmpresa);
    public ConsumosDTO generarReporteConsumoEmpresa(long cuitEmpresa);
}
// Consumos:{id(PK)(AUTO), mes, año, luz, gas, agua, residuos, cant_empleados, cant_vehiculos, cuit_empresa(FK)}
