package com.parque_industrial.services;

import com.parque_industrial.dto.proyecto.CrearRequestDTO;
import com.parque_industrial.dto.proyecto.CrearRequestDefinitivoDTO;
import com.parque_industrial.dto.proyecto.ProyectosDTO;

import java.util.List;

public interface GestorProyectos {
    void crearProyectoPreliminar(CrearRequestDTO dto);

    void crearProyectoDefinitivo(CrearRequestDefinitivoDTO request);

    ProyectosDTO listarProyectos();

    ProyectosDTO listarProyectosPorCuit(String cuit);
}