package com.parque_industrial.services;

import com.parque_industrial.dto.proyecto.CrearRequestDTO;
import com.parque_industrial.dto.proyecto.CrearRequestDefinitivoDTO;

import java.util.List;

public interface GestorProyectos {
    void crearProyectoPreliminar(CrearRequestDTO dto);

    void crearProyectoDefinitivo(CrearRequestDefinitivoDTO request);

    List<CrearRequestDefinitivoDTO> listarProyectos();
}