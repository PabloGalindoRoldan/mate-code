package com.parque_industrial.services;

import com.parque_industrial.dto.proyecto.CrearRequestDTO;

public interface GestorProyectos {
    void crearProyectoPreliminar(CrearRequestDTO dto);
}