package com.parque_industrial.services;

import com.parque_industrial.persistence.dtos.ConsumosDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GestorConsumos {
    private ConsumosDAO dao;
    public GestorConsumos(ConsumosDAO dao) {
        this.dao = dao;
    }
    public void registrarConsumos(ConsumosDTO consumosDTO) {
        dao.cargarConsumosDeEmpresa(consumosDTO);
    }
     public void asignarCantVheiculos(long cuitEmoresa, int cant) {
         if (cant < 0 ) {
             throw new IllegalArgumentException("La cantidad de vehículos no puede ser negativa");
         }
        dao.asignarCantVheiculos(cuitEmoresa, cant);
    }
    public void asignarCantEmpleados(long cuitEmoresa, int cant) {
        if (cant < 0 ) {
            throw new IllegalArgumentException("La cantidad de empleados no puede ser negativa");
        }
        dao.asignarCantEmpleados(cuitEmoresa, cant);
    }
    public void asignarConsumoGas(long cuitEmoresa, float gas) {
        if (gas < 0 ) {
            throw new IllegalArgumentException("El consumo de gas no puede ser negativo");
        }
        dao.asignarConsumoGas(cuitEmoresa, gas);
    }
    public void asignarConsumoLuz(long cuitEmoresa, float luz) {
        if (luz < 0 ) {
            throw new IllegalArgumentException("El consumo de luz no puede ser negativo");
        }
        dao.asignarConsumoLuz(cuitEmoresa, luz);
    }
    public  void asignarConsumoResiduos(long cuitEmoresa, int kilosResiduos) {
        if (kilosResiduos < 0 ) {
            throw new IllegalArgumentException("El consumo de residuos no puede ser negativo");
        }
        dao.asignarConsumoResiduos(cuitEmoresa, kilosResiduos);
    }
    public void asignarConsumoAgua(long cuitEmoresa, float agua) {
        if (agua < 0 ) {
            throw new IllegalArgumentException("El consumo de agua no puede ser negativo");
        }
        dao.asignarConsumoAgua(cuitEmoresa, agua);
    }
    public List<ConsumosDTO> generarReporteConsumoTotalParque() {
        return dao.generarReporteConsumoTotalParque();
    }
    public List<ConsumosDTO> generarReporteConsumoTotalEmpresa(long cuitEmpresa) {
        return dao.generarReporteConsumoTotalEmpresa(cuitEmpresa);
    }
    public  ConsumosDTO generarReporteConsumoEmpresa(long cuitEmpresa) {
        return dao.generarReporteConsumoEmpresa(cuitEmpresa);
    }

}