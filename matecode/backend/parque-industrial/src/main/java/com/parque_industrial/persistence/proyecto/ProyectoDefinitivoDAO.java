package com.parque_industrial.persistence.proyecto;

import com.parque_industrial.dto.proyecto.CrearRequestDefinitivoDTO;

import java.util.List;

public interface ProyectoDefinitivoDAO {

    List<CrearRequestDefinitivoDTO> listarDefinitivosPorCuit(String cuit);

    List<CrearRequestDefinitivoDTO> listarProyectos();
}

