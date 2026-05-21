package com.parque_industrial.services;

import com.parque_industrial.dto.inventario.BajaElementoRequestDTO;
import com.parque_industrial.dto.inventario.ElementoRequestDTO;
import com.parque_industrial.dto.inventario.ElementoResponseDTO;

import java.util.List;

public interface InventarioService {
    ElementoResponseDTO crearElemento(ElementoRequestDTO request);

    ElementoResponseDTO buscarPorId(Integer id);

    List<ElementoResponseDTO> obtenerTodosLosElementos();

    List<ElementoResponseDTO> obtenerElementosActivos();

    ElementoResponseDTO procesarBajaLogica(Integer id, BajaElementoRequestDTO request);
}