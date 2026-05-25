package com.parque_industrial.persistence.proyecto;

import com.parque_industrial.dto.proyecto.CrearRequestDefinitivoDTO;

import java.util.List;

public class ProyectoDefinitivoJDBC implements ProyectoDefinitivoDAO{
    @Override
    public List<CrearRequestDefinitivoDTO> listarDefinitivosPorCuit(String cuit) {
        return List.of();
    }
}
