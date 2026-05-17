package com.parque_industrial.services;

import com.parque_industrial.controllers.dtos.empresa.CargarConsumoRequest;
import com.parque_industrial.dto.empresa.ConsumoResponseDTO;
import java.util.List;

public interface GestorConsumos {

    // Procesa el alta de consumo, validando que no esté duplicado el período
    void registrarConsumoMensual(String cuitEmpresa, CargarConsumoRequest request);

    // Devuelve el historial de consumos de una empresa formateado como DTO
    List<ConsumoResponseDTO> obtenerHistorialEmpresa(String cuitEmpresa);

    // Devuelve los consumos de todo el parque de un año para las métricas del Admin
    List<ConsumoResponseDTO> obtenerReporteGlobalPorAno(int ano);
}